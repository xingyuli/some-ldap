package org.swordess.ldap.bean;

import java.lang.reflect.Method;

/**
 * Getter concept in java bean specification. A standard getter should have a
 * signature like:
 * <ul>
 * <li>public String getFoo(), or</li>
 * <li>public boolean isFoo()</li>
 * </ul>
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 * 
 */
public class Getter {

    private final Method getter;
    
    public Getter(Method getter) {
        this.getter = getter;
    }
    
    public Object get(Object target) {
        try {
            return getter.invoke(target);
        } catch (Exception e) {
            throw new ReflectionException("failed to invoke getter", e);
        }
    }
    
    public String getMethodName() {
        return getter.getName();
    }
    
}
