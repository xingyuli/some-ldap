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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
    
    public static <T> List<List<T>> slices(List<T> list, int step) {
        return slices(list, 0, list.size(), step);
    }
    
    public static <T> List<List<T>> slices(List<T> list, int from, int to, int step) {
        List<List<T>> slices = new ArrayList<List<T>>();
        if (!isEmpty(list)) {
            int low = Math.max(from, 0);
            int high = Math.max(to, list.size());
            
            List<T> temp = new ArrayList<T>(step);
            for (int i = low; i < high; i++) {
                temp.add(list.get(i));
                if (temp.size() == step || i == high - 1) {
                    slices.add(new ArrayList<T>(temp));
                    temp.clear();
                }
            }
        }
        return slices;
    }
    
    private CollectionUtils() {
    }
    
}
