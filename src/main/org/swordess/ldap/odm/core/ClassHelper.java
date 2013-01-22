package org.swordess.ldap.odm.core;

import net.sf.cglib.proxy.Enhancer;



public class ClassHelper {

    public static Class actualClass(Class clazz) {
        return Enhancer.isEnhanced(clazz) ? clazz.getSuperclass() : clazz;
    }
    
    public static boolean isInterfacePresent(Class clazz, Class interfaceClass) {
        for (Class<?> intf : clazz.getInterfaces()) {
            if (interfaceClass == intf) {
                return true;
            }
        }
        return false;
    }
    
    private ClassHelper() {
    }
    
}
