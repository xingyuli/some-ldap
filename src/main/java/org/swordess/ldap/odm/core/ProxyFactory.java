package org.swordess.ldap.odm.core;

import org.swordess.ldap.odm.ODMException;

import net.sf.cglib.proxy.Factory;

abstract class ProxyFactory {

	protected final Class<?> unproxiedClass;
	protected final Class<?> proxyClass;
    protected final Factory factory;
    
    protected ProxyFactory(Class<?> unproxiedClass) {
    	this.unproxiedClass = unproxiedClass;
    	this.proxyClass = createProxyClass();
    	
        try {
            factory = (Factory) proxyClass.newInstance();
        } catch (Throwable t) {
            throw new ODMException("Unable to build CGLIB Factory instance", t);
        }
	}
    
    protected abstract Class<?> createProxyClass();
    
    protected static String cglibSpec(String fieldName) {
        return "$cglib_prop_" + fieldName;
    }
	
}
