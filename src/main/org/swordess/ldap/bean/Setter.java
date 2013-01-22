package org.swordess.ldap.bean;

import java.lang.reflect.Method;

/**
 * Setter concept in java bean specification. A standard setter should have a
 * signature like:
 * <ul>
 * <li>public void setFoo(String), or</li>
 * <li>public void setFoo(boolean)</li>
 * </ul>
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 * 
 */
public class Setter {

    private final Method setter;
    
    public Setter(Method setter) {
        this.setter = setter;
    }
    
    public void set(Object target, Object value) {
        try {
            setter.invoke(target, value);
        } catch (Exception e) {
            throw new ReflectionException("failed to invoke setter", e);
        }
    }
    
    public String getMethodName() {
        return setter.getName();
    }
    
}
