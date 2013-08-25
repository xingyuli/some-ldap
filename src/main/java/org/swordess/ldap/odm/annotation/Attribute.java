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
package org.swordess.ldap.odm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should only presents in {@link Entry} annotated class. An
 * {@link Attribute} annotated method means it should be regard as a getter
 * method of a certain LDAP attribute.
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Attribute {
    
    /**
     * Attribute name in LDAP, if not specified, then the property name in java
     * bean specification will be used.
     * 
     * @return
     */
    public String name() default "";
    
    /**
     * Indicates whether this attribute should be treat as read-only or not.
     * When set to <tt>true</tt>, any changes to this attribute will be ignored
     * when update operations occur.
     * <p/>
     * 
     * <b>NOTE: </b> Read only attributes still can be summit to LDAP server
     * when create operations happens.
     * 
     * @return
     */
    public boolean readonly() default false;
    
}
