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
 * Indicates the class is an LDAP model.
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entry {
    
    /**
     * <tt>objectclass</tt> values this model should be saved.
     * 
     * @return
     */
    public String[] objectClasses();

    /**
     * Under which context that instances of this class should be saved.
     * 
     * @return
     */
    public String context();
    
}
