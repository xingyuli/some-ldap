/*
 * Swordess-ldap, an Object-Directory Mapping tool. 
 * 
 * Copyright (c) 2013, 2013 Liu Xingyu.
 * 
 * Swordess-ldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Swordess-ldap is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Swordess-ldap. If not, see <http://www.gnu.org/licenses/>.
 */
package org.swordess.ldap.odm.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Type;
import org.swordess.ldap.bean.Specification;
import org.swordess.ldap.odm.Distinguishable;
import org.swordess.ldap.odm.ODMException;
import org.swordess.ldap.odm.core.SessionImpl.Persistent;

import net.sf.cglib.core.ClassGenerator;
import net.sf.cglib.core.DefaultGeneratorStrategy;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.LazyLoader;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import net.sf.cglib.transform.TransformingClassGenerator;
import net.sf.cglib.transform.impl.AddPropertyTransformer;



public class ProxyFactory {

    private static Map<Class<?>, ProxyFactory> factories = new HashMap<Class<?>, ProxyFactory>();

    private final Factory factory;
    
    /**
     * Whether the entry class implements {@link Distinguishable} interface or
     * not.
     */
    private boolean isDistinguishable;
    
    /**
     * An additional field(<code>HashSet&lt;String&gt;</code>) to track modified
     * property names which caused by setter.
     */
    private Field modifiedPropNames;
    
    private ProxyFactory(Class<?> clazz) {
        Enhancer en = new Enhancer();
        en.setInterceptDuringConstruction(false);
        en.setUseFactory(true);
        
        en.setSuperclass(clazz);
        if (isDistinguishable(clazz)) {
            isDistinguishable = true;
            en.setInterfaces(new Class[] { Persistent.class, Distinguishable.class });
            en.setCallbackTypes(new Class[] { MethodInterceptor.class, NoOp.class });
            en.setCallbackFilter(FINALIZE_AND_DISTINGUISHABLE_INTEFERCE_FILTER);
        } else {
            en.setInterfaces(new Class[] { Persistent.class });
            en.setCallbackType(SetterInterceptor.class);
        }
        
        // Add an additional track field.
        en.setStrategy(new DefaultGeneratorStrategy() {
            protected ClassGenerator transform(ClassGenerator cg) throws Exception {
                return new TransformingClassGenerator(cg, new AddPropertyTransformer(
                        new String[] { MODIFIED_PROP_NAMES },
                        new Type[] { Type.getType(Set.class) }
                    ));
            }
        });
        
        Class<?> proxyClass = en.createClass();
        try {
            modifiedPropNames = proxyClass.getDeclaredField(cglibSpec(MODIFIED_PROP_NAMES));
            modifiedPropNames.setAccessible(true);
        } catch (NoSuchFieldException e) {
        }
        
        try {
            factory = (Factory) proxyClass.newInstance();
        } catch (Throwable t) {
            throw new ODMException("Unable to build CGLIB Factory instance", t);
        }
    }
    
    static Map.Entry<Object, SetterInterceptor> getProxiedEntity(Class<?> entityClass) {
        ProxyFactory proxyFactory = getFactory(ClassHelper.actualClass(entityClass));
        
        try {
            Object entity = null;
            SetterInterceptor interceptor = proxyFactory.new SetterInterceptor();
            
            if (proxyFactory.isDistinguishable) {
                entity = proxyFactory.factory.newInstance(new Callback[] { interceptor, NoOp.INSTANCE });
            } else {
                entity = proxyFactory.factory.newInstance(interceptor);
            }
            
            proxyFactory.modifiedPropNames.set(entity, new HashSet<String>());
            interceptor.setEntity(entity);
            
            return new AbstractMap.SimpleEntry<Object, SetterInterceptor>(entity, interceptor);
            
        } catch (Throwable t) {
            throw new ODMException("Unable to instantiate proxy instance", t);
        }
    }
    
    static Object getLazyLoadingProxiedEntity(final Class<?> entityClass, final String dn) {
        Object entity = null;
        if (isDistinguishable(entityClass)) {
            entity = Enhancer.create(entityClass, new Class[] { Persistent.class, Distinguishable.class },
                    FINALIZE_AND_DISTINGUISHABLE_INTEFERCE_FILTER,
                    new Callback[] {
                        new LazyLoader() {
                            public Object loadObject() throws Exception {
                                return DefaultSessionFactory.getDefaultFactory().getCurrentSession().read(entityClass, dn);
                            }
                        },
                        NoOp.INSTANCE
                    }
                );
            ((Distinguishable)entity).setDN(dn);
            
        } else {
            entity = Enhancer.create(entityClass, new Class[] { Persistent.class }, FINALIZE_FILTER,
                    new Callback[] {
                        new LazyLoader() {
                            public Object loadObject() throws Exception {
                                return DefaultSessionFactory.getDefaultFactory().getCurrentSession().read(entityClass, dn);
                            }
                        },
                        NoOp.INSTANCE
                    }
                );
        }
        
        return entity;
    }
    
    static ProxyFactory getFactory(Class<?> clazz) {
        ProxyFactory factory = factories.get(clazz);
        if (null == factory) {
            factory = new ProxyFactory(clazz);
            factories.put(clazz, factory);
        }
        return factory;
    }
    
    static Set<String> getModifiedPropNames(Object entity) {
        return getFactory(ClassHelper.actualClass(entity.getClass())).modifiedPropNames(entity);
    }
    
    @SuppressWarnings("unchecked")
    private Set<String> modifiedPropNames(Object entity) {
        try {
            return (Set<String>) modifiedPropNames.get(entity);
        } catch (Throwable t) {
            throw new ODMException("Can not read modifiedPropNames from " + entity);
        }
    }
    
    private static boolean isDistinguishable(Class<?> clazz) {
        return ClassHelper.isInterfacePresent(clazz, Distinguishable.class);
    }
    
    private static String cglibSpec(String fieldName) {
        return "$cglib_prop_" + fieldName;
    }
    
    private static final String MODIFIED_PROP_NAMES = "modifiedPropNames";
    
    private static final CallbackFilter FINALIZE_FILTER = new CallbackFilter() {
        public int accept(Method method) {
            if ("finalize".equals(method.getName()) && method.getParameterTypes().length == 0) {
                return 1;
            }
            return 0;
        }
    };
    
    private static final CallbackFilter FINALIZE_AND_DISTINGUISHABLE_INTEFERCE_FILTER = new CallbackFilter() {
        public int accept(Method method) {
            String methodName = method.getName();
            int paramLength = method.getParameterTypes().length;
            if ("finalize".equals(methodName) && paramLength == 0) {
                return 1;
            } else if ("getDN".equals(methodName) && paramLength == 0) {
                return 1;
            } else if ("setDN".equals(methodName) && paramLength == 1 && method.getParameterTypes()[0] == String.class) {
                return 1;
            }
            return 0;
        }
    };
    
    class SetterInterceptor implements MethodInterceptor {
        
        Object entity;
        boolean turnedOn;
        
        // cache the reference which points to entity's modifiedPropNames field
        Set<String> modifiedNames;
        
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy mProxy) throws Throwable {
            if (turnedOn && Specification.isSetter(method)) {
                if (null == modifiedNames) {
                    modifiedNames = modifiedPropNames(entity);
                }
                modifiedNames.add(Specification.getPropertyName(method));
            }
            return mProxy.invokeSuper(proxy, args);
        }
        
        void setEntity(Object entity) {
            this.entity = entity;
        }
        
        public void turnOn() {
            turnedOn = true;
        }
        
    }
    
}
