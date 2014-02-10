package org.swordess.ldap.odm.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.core.ClassGenerator;
import net.sf.cglib.core.DefaultGeneratorStrategy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import net.sf.cglib.transform.TransformingClassGenerator;
import net.sf.cglib.transform.impl.AddPropertyTransformer;

import org.objectweb.asm.Type;
import org.swordess.ldap.odm.ODMException;
import org.swordess.ldap.odm.core.SessionImpl.Persistent;
import org.swordess.ldap.odm.metadata.indirections.TheOtherMetaData;
import org.swordess.ldap.odm.metadata.indirections.OneMetaData;
import org.swordess.ldap.odm.metadata.indirections.IndirectionsMetaData;

public class IndirectionsProxyFactory extends ProxyFactory {

	private static final String ORIGINAL_ONE = "originalOne";
	private static final String ORIGINAL_THE_OTHER = "originalTheOther";
	
	private static Map<Class<?>, IndirectionsProxyFactory> factories = new HashMap<Class<?>, IndirectionsProxyFactory>();
	
	private Field originalOne;
	private Field originalTheOther;
	
	private IndirectionsProxyFactory(Class<?> clazz) {
    	super(clazz);
        try {
        	originalOne = proxyClass.getDeclaredField(cglibSpec(ORIGINAL_ONE));
        	originalOne.setAccessible(true);
        	
        	originalTheOther = proxyClass.getDeclaredField(cglibSpec(ORIGINAL_THE_OTHER));
        	originalTheOther.setAccessible(true);
        } catch (NoSuchFieldException e) {
        }
	}
	
	@Override
	protected Class<?> createProxyClass() {
		Enhancer en = new Enhancer();
    	en.setInterceptDuringConstruction(false);
    	en.setUseFactory(true);
    	
    	en.setSuperclass(unproxiedClass);
    	en.setInterfaces(new Class[] { Persistent.class });
    	en.setCallbackType(NoOp.class);
    	en.setStrategy(new DefaultGeneratorStrategy() {
			protected ClassGenerator transform(ClassGenerator cg) throws Exception {
				return new TransformingClassGenerator(cg, new AddPropertyTransformer(
						new String[] { ORIGINAL_ONE, ORIGINAL_THE_OTHER },
						new Type[] { Type.getType(String.class), Type.getType(List.class) }
					));
			}
    	});
    	
    	return en.createClass();
	}
	
    @SuppressWarnings("unchecked")
	static <T> T getProxiedIndirections(T unproxied) {
    	if (null == unproxied) {
    		return null;
    	}
    	
    	Class<?> actualClass = ClassHelper.actualClass(unproxied);
    	OneMetaData oneMetaData = IndirectionsMetaData.get(actualClass).getOne();
		TheOtherMetaData theOtherMetaData = IndirectionsMetaData.get(actualClass).getTheOther();
		
    	IndirectionsProxyFactory proxyFactory = getFactory(actualClass); 
    	try {
    		T indirections = (T) proxyFactory.factory.newInstance(NoOp.INSTANCE);
    		String originalOne = oneMetaData.getter().get(unproxied);
    		List<String> originalTheOther = theOtherMetaData.getter().get(unproxied);
    		
    		proxyFactory.originalOne.set(indirections, originalOne);
    		// make a copy, so that the coming changes will not impact it.
    		proxyFactory.originalTheOther.set(indirections, new ArrayList<String>(originalTheOther));
    		
    		oneMetaData.setter().set(indirections, originalOne);
    		theOtherMetaData.setter().set(indirections, originalTheOther);
    		
    		return indirections;
    		
    	} catch (Throwable t) {
    		throw new ODMException("Unable to instantiate proxy instance", t);
    	}
    }
    
    static String getOriginalOne(Object indirections) {
    	if (null == indirections) {
    		return null;
    	}
    	
    	try {
    		return (String) getFactory(ClassHelper.actualClass(indirections)).originalOne.get(indirections);
    	} catch (Throwable t) {
    		throw new ODMException("Can not read originalOne from " + indirections);
    	}
    }
    
    @SuppressWarnings("unchecked")
	static List<String> getOriginalTheOther(Object indirections) {
    	if (null == indirections) {
    		return null;
    	}
    	
    	try {
    		return (List<String>) getFactory(ClassHelper.actualClass(indirections)).originalTheOther.get(indirections);
    	} catch (Throwable t) {
    		throw new ODMException("Can not read originalTheOther from " + indirections);
    	}
    }
    
    static void refreshOriginals(Object indirections) {
    	if (null == indirections) {
    		return;
    	}
    	if (!(indirections instanceof Persistent)) {
    		return;
    	}
    	
    	Class<?> actualClass = ClassHelper.actualClass(indirections);
    	OneMetaData oneMetaData = IndirectionsMetaData.get(actualClass).getOne();
		TheOtherMetaData theOtherMetaData = IndirectionsMetaData.get(actualClass).getTheOther();
    	IndirectionsProxyFactory proxyFactory = getFactory(actualClass);
    	
    	try {
    		proxyFactory.originalOne.set(indirections, oneMetaData.getter().get(indirections));
    		proxyFactory.originalTheOther.set(indirections, new ArrayList<String>(theOtherMetaData.getter().get(indirections)));
    	} catch (Throwable t) {
    		throw new RuntimeException("failed to refresh originals", t);
    	}
    }
    
    static IndirectionsProxyFactory getFactory(Class<?> clazz) {
    	IndirectionsProxyFactory factory = factories.get(clazz);
        if (null == factory) {
            factory = new IndirectionsProxyFactory(clazz);
            factories.put(clazz, factory);
        }
        return factory;
    }
    
}
