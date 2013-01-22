package org.swordess.ldap.bean;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

/**
 * A helper class which helps with following things:
 * <ul>
 * <li>whether a method is a {@link Setter} or {@link Getter}</li>
 * <li>find out the {@link Setter} or {@link Getter} by java bean property name</li>
 * <li>find out the java bean property name of a {@link Setter} or {@link Getter}</li>
 * </ul>
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 * 
 */
public class Specification {

    public static boolean isSetter(Method method) {
        if (method.getParameterTypes().length != 1) {
            return false;
        }
        /*
         * here we allow a setter has a return type to enable chained
         * invocation, so we didn't check for the return type
         */
        return method.getName().startsWith("set");
    }
    
    public static boolean isGetter(Method method) {
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        if (method.getReturnType() == null) {
            return false;
        }
        return method.getName().startsWith("get") || method.getName().startsWith("is");
    }
    
    public static Method getSetter(Class<?> clazz, String propertyName, Class<?> paramType) {
        try {
            return clazz.getDeclaredMethod("set" + StringUtils.capitalize(propertyName), paramType);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    public static Method getGetter(Class<?> clazz, String propertyName) {
        try {
            return clazz.getDeclaredMethod("get" + StringUtils.capitalize(propertyName));
        } catch (NoSuchMethodException e) {
            try {
                // continue to find method isXxx()
                return clazz.getDeclaredMethod("is" + StringUtils.capitalize(propertyName));
            } catch (NoSuchMethodException e1) {
                return null;
            }
        }
    }
    
    public static String getPropertyName(Method method) {
        if (isSetter(method)) {
            return StringUtils.uncapitalize(method.getName().substring(3));
            
        } else if (isGetter(method)) {
            if (method.getName().startsWith("get")) {
                return StringUtils.uncapitalize(method.getName().substring(3));
            } else if (method.getName().startsWith("is")) {
                return StringUtils.uncapitalize(method.getName().substring(2));
            } else {
                /* should not occur */
            }
        }
        
        return null;
    }
    
    private Specification() {
    }
    
}
