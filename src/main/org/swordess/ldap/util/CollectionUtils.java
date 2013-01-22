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
