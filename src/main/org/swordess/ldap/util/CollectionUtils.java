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
package org.swordess.ldap.util;

import java.util.Collection;


public class CollectionUtils {

    public static boolean isEmpty(Collection<?> c) {
        return null == c || c.isEmpty();
    }
    
    public static <T> void addIfNotNull(Collection<T> c, T element) {
        if (null != c && null != element) {
            c.add(element);
        }
    }
    
    public static <T> void addIfNotEmpty(Collection<T> c, Collection<T> elements) {
        if (null != c && null != elements && !elements.isEmpty()) {
            c.addAll(elements);
        }
    }
    
    private CollectionUtils() {
    }
    
}
