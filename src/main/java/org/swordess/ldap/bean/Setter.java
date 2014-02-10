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
