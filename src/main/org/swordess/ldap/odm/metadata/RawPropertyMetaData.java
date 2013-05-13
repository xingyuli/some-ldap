package org.swordess.ldap.odm.metadata;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.swordess.ldap.bean.Getter;
import org.swordess.ldap.bean.Setter;
import org.swordess.ldap.bean.Specification;
import org.swordess.ldap.odm.ODMException;
import org.swordess.ldap.odm.annotation.Entry;

public class RawPropertyMetaData implements RawPropertyMetaDataInterface {

	protected Log log = LogFactory.getLog(getClass());
	
	private Getter getter;
    private Setter setter;
    
    private String javaBeanPropName;
    private Class<?> valueClass;
    private boolean isMultiple;
    private boolean isReference;
    
    public RawPropertyMetaData(Method getterMethod) {
        // initialize propName, getter and setter
    	determineJavaBeanPropName(getterMethod);
        
        // initialize valueClass, isMultiple
        determineAttributeType(getterMethod);
        
        isReference = valueClass.isAnnotationPresent(Entry.class);
    }
    
	private void determineJavaBeanPropName(Method getterMethod) {
		if (Specification.isGetter(getterMethod)) {
            javaBeanPropName = Specification.getPropertyName(getterMethod);
        } else {
            throw new ODMException(getterMethod + " is not a getter");
        }
        
        this.getter = new Getter(getterMethod);
        Method setter = Specification.getSetter(getterMethod.getDeclaringClass(), javaBeanPropName, getterMethod.getReturnType());
        if (null != setter) {
            this.setter = new Setter(setter);
        } else {
            throw new ODMException("Unable to find setter for property " + javaBeanPropName);
        }
	}
    
    private void determineAttributeType(Method getterMethod) {
        Class<?> propertyType = getterMethod.getReturnType();
        if (Set.class.isAssignableFrom(propertyType)) {
            throw new MetaDataException(String.format("Only lists are allowed for multivlaued attributes, error in property %1$s in class %2$s", 
                    javaBeanPropName, getterMethod.getDeclaringClass()));
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
                throw new MetaDataException(String.format("Can't determine destination type for property %1$s in class %2$s", 
                        javaBeanPropName, getterMethod.getDeclaringClass()), e);
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
            throw new MetaDataException(String.format("Can't determine destination type for property %1$s in class %2$s", 
                    javaBeanPropName, getterMethod.getDeclaringClass()));
        }
    }
    
    /* (non-Javadoc)
	 * @see org.swordess.ldap.odm.metadata.RawPropertyMetaDataInterface#getter()
	 */
    @Override
	public Getter getter() {
        return getter;
    }
    
    /* (non-Javadoc)
	 * @see org.swordess.ldap.odm.metadata.RawPropertyMetaDataInterface#setter()
	 */
    @Override
	public Setter setter() {
        return setter;
    }
    
    /* (non-Javadoc)
	 * @see org.swordess.ldap.odm.metadata.RawPropertyMetaDataInterface#getJavaBeanPropName()
	 */
    @Override
	public String getJavaBeanPropName() {
        return javaBeanPropName;
    }

    /* (non-Javadoc)
	 * @see org.swordess.ldap.odm.metadata.RawPropertyMetaDataInterface#getValueClass()
	 */
    @Override
	public Class<?> getValueClass() {
        return valueClass;
    }
    
    /* (non-Javadoc)
	 * @see org.swordess.ldap.odm.metadata.RawPropertyMetaDataInterface#isMultiple()
	 */
    @Override
	public boolean isMultiple() {
        return isMultiple;
    }
    
    /* (non-Javadoc)
	 * @see org.swordess.ldap.odm.metadata.RawPropertyMetaDataInterface#isReference()
	 */
    @Override
	public boolean isReference() {
        return isReference;
    }
    
    @Override
    public String toString() {
        return String.format("javaBeanPropName=%s | valueClass=%s | isMultiple=%s | isReference=%s",
                getJavaBeanPropName(), getValueClass(), isMultiple(), isReference());
    }
	
}
