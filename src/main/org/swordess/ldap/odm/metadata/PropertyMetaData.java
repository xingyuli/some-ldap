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
package org.swordess.ldap.odm.metadata;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import org.swordess.ldap.bean.Getter;
import org.swordess.ldap.bean.Setter;
import org.swordess.ldap.bean.Specification;
import org.swordess.ldap.odm.ODMException;
import org.swordess.ldap.odm.annotation.Attribute;
import org.swordess.ldap.odm.annotation.Entry;
import org.swordess.ldap.odm.annotation.Id;



public class PropertyMetaData {

    private Getter getter;
    private Setter setter;
    
    private String ldapPropName;
    private String javaBeanPropName;
    private Class<?> valueClass;
    private boolean isId;
    private boolean isMultiple;
    private boolean isReference;
    private boolean isReadonly;
    
    public PropertyMetaData(Method getterMethod) {
        // initialize propName, getter and setter
        determineAttributeName(getterMethod);
        
        // initialize valueClass, isMultiple
        determineAttributeType(getterMethod);
        
        isId = getterMethod.isAnnotationPresent(Id.class);
        isReference = valueClass.isAnnotationPresent(Entry.class);
        
        if (getterMethod.isAnnotationPresent(Attribute.class)) {
            isReadonly = getterMethod.getAnnotation(Attribute.class).readonly();
        }
    }
    
    private void determineAttributeName(Method getterMethod) {
        if (getterMethod.isAnnotationPresent(Id.class)) {
            String nameOnId = getterMethod.getAnnotation(Id.class).name();
            if (!"".equals(nameOnId)) {
                ldapPropName = nameOnId;
            }
            
        } else if (getterMethod.isAnnotationPresent(Attribute.class)) {
            String nameOnAttribute = getterMethod.getAnnotation(Attribute.class).name();
            if (!"".equals(nameOnAttribute)) {
                ldapPropName = nameOnAttribute;
            }
        }
        
        if (Specification.isGetter(getterMethod)) {
            javaBeanPropName = Specification.getPropertyName(getterMethod);
        } else {
            throw new ODMException(getterMethod + " is not a getter");
        }
        
        /*
         * using java bean specification as the property name if it is neither
         * specified by @Id not @Attribute
         */
        if (null == ldapPropName) {
            ldapPropName = javaBeanPropName;
        }
        
        this.getter = new Getter(getterMethod);
        Method setter = Specification.getSetter(getterMethod.getDeclaringClass(), javaBeanPropName, getterMethod.getReturnType());
        if (null != setter) {
            this.setter = new Setter(setter);
        } else {
            throw new ODMException("Unable to find setter for property " + ldapPropName);
        }
    }
    
    private void determineAttributeType(Method getterMethod) {
        Class<?> propertyType = getterMethod.getReturnType();
        if (Set.class.isAssignableFrom(propertyType)) {
            throw new MetaDataException(String.format("Only lists are allowed for multivlaued attributes, error in property %1$s in Entry class %2$s", 
                    ldapPropName, getterMethod.getDeclaringClass()));
        }
        isMultiple = List.class.isAssignableFrom(propertyType);
        
        valueClass = null;
        if (!isMultiple) {
            valueClass = propertyType;
        } else {
            ParameterizedType paramType;
            try {
                paramType = (ParameterizedType)getterMethod.getGenericReturnType();
            } catch (ClassCastException e) {
                throw new MetaDataException(String.format("Can't determine destination type for property %1$s in Entry class %2$s", 
                        ldapPropName, getterMethod.getDeclaringClass()), e);
            }
            Type[] actualParamArguments = paramType.getActualTypeArguments();
            if (actualParamArguments.length == 1) {
                if (actualParamArguments[0] instanceof Class) {
                    valueClass = (Class<?>) actualParamArguments[0];
                } else if (actualParamArguments[0] instanceof GenericArrayType) {
                    // Deal with arrays
                    Type type = ((GenericArrayType) actualParamArguments[0]).getGenericComponentType();
                    if (type instanceof Class) {
                        valueClass = Array.newInstance((Class<?>) type, 0).getClass();
                    }
                }
            }
        }
        
        // Check we have been able to determine the value class
        if (null == valueClass) {
            throw new MetaDataException(String.format("Can't determine destination type for property %1$s in Entry class %2$s", 
                    ldapPropName, getterMethod.getDeclaringClass()));
        }
    }
    
    public Getter getter() {
        return getter;
    }
    
    public Setter setter() {
        return setter;
    }
    
    public String getLdapPropName() {
        return ldapPropName;
    }

    public String getJavaBeanPropName() {
        return javaBeanPropName;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }
    
    public boolean isId() {
        return isId;
    }
    
    public boolean isMultiple() {
        return isMultiple;
    }
    
    public boolean isReference() {
        return isReference;
    }
    
    public boolean isReadonly() {
        return isReadonly;
    }
    
    @Override
    public String toString() {
        return String.format("ldapPropName=%s | javaBeanPropName=%s | valueClass=%s | isId=%s | isMultiple=%s | isReference=%s",
                getLdapPropName(), getJavaBeanPropName(), getValueClass(), isId(), isMultiple(), isReference());
    }
    
}