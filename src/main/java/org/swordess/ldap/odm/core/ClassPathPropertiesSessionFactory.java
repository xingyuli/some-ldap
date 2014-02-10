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
package org.swordess.ldap.odm.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;

import org.swordess.ldap.Session;
import org.swordess.ldap.odm.ODMException;


/**
 * Factory for providing {@link Session}s.
 * <p/>
 * 
 * Currently, only one factory is supported in an application. And it should be
 * configured by providing a properties file named <b>odm.properties</b> under
 * classpath.
 * <p/>
 * 
 * A sample properties file is:
 * 
 * <pre>
 * #####################
 * ## connection pool ##
 * #####################
 * 
 * # connection pool enabled
 * com.sun.jndi.ldap.connect.pool = true
 * 
 * # if not specified, the default initsize is 1
 * com.sun.jndi.ldap.connect.pool.initsize = 5
 * 
 * com.sun.jndi.ldap.connect.pool.maxsize = 20
 * 
 * # idle connection in pool which exceed 5mins will be closed and removed
 * com.sun.jndi.ldap.connect.pool.timeout = 3000000
 * 
 * com.sun.jndi.ldap.connect.pool.debug = all
 * 
 * #####################
 * ##  authentication ##
 * #####################
 * 
 * java.naming.provider.url = ldap://...:389
 * java.naming.security.authentication = simple
 * java.naming.security.principal = ...
 * java.naming.security.credentials = ...
 * java.naming.ldap.version = 3
 * 
 * </pre>
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 * 
 */
public class ClassPathPropertiesSessionFactory extends AbstractThreadLocalSessionFactory {

    private static final String DEFAULT_CONFIGURATION_FILENAME = "odm";
    private static final String DEFAULT_CONFIGURATION_FILE_TYPE = ".properties";
    
    private static Map<String, ClassPathPropertiesSessionFactory> factories = new HashMap<String, ClassPathPropertiesSessionFactory>();
    private static ClassPathPropertiesSessionFactory defaultFactory = null;
    
    private Hashtable<String, String> env = new Hashtable<String, String>();
    
    public ClassPathPropertiesSessionFactory(String configurationFileNameUnderClassPath) {
        Properties configuration = new Properties();
        InputStream in = ClassPathPropertiesSessionFactory.class.getResourceAsStream("/" + configurationFileNameUnderClassPath);
        try {
            configuration.load(in);
            in.close();
            
            for (Object key : configuration.keySet()) {
                String keyStr = (String) key;
                /*
                 * TODO Solve multiple factory instances issue, so that each
                 * pool related of this factory is isolated with other
                 * factories?? 
                 */
                if (isPoolConfiguration(keyStr)) {
                    System.setProperty(keyStr, configuration.getProperty(keyStr));
                } else {
                    env.put(keyStr, configuration.getProperty(keyStr));
                }
            }
            
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            
        } catch (IOException e) {
            throw new ODMException("Unalbe to instantiate SessionFactory", e);
        }
    }
    
    @Override
    protected InitialLdapContext getContext() throws NamingException {
        return new InitialLdapContext(env, new Control[0]);
    }
    
    /**
     * Return the default session factory (i.e., using odm.properties as
     * configuration file).
     * 
     * @return
     */
    public static ClassPathPropertiesSessionFactory getInstance() {
        if (null == defaultFactory) {
            return getInstance(DEFAULT_CONFIGURATION_FILENAME);
        }
        return defaultFactory;
    }
    
    /**
     * Get the session factory using the specified configuration filename.
     * 
     * NOTE: The filename should not include the file type as we use
     * ".properties" as the file type internally.
     * 
     * e.g., if the <tt>configureFilenameWithoutExtension</tt> is passed as
     * "myOdm", this will return the factory using myOdm.properties as its
     * configuration file.
     *  
     * @param configureFilenameWithoutExtension
     * @return
     */
    public static ClassPathPropertiesSessionFactory getInstance(String configureFilenameWithoutExtension) {
    	ClassPathPropertiesSessionFactory factory = factories.get(configureFilenameWithoutExtension);
    	if (factory == null) {
    		factory = new ClassPathPropertiesSessionFactory(configureFilenameWithoutExtension + DEFAULT_CONFIGURATION_FILE_TYPE);
        	factories.put(configureFilenameWithoutExtension, factory);
        	if (DEFAULT_CONFIGURATION_FILENAME.equals(configureFilenameWithoutExtension)) {
        		defaultFactory = factory;
        	}
    	}
    	return factory;
    }
    
    private static boolean isPoolConfiguration(String key) {
        if (POOL_AUTHENTICATION.equals(key)
         || POOL_DEBUG.equals(key)
         || POOL_INITSIZE.equals(key)
         || POOL_MAXSIZE.equals(key)
         || POOL_PREFSIZE.equals(key)
         || POOL_PROTOCOL.equals(key)
         || POOL_TIMEOUT.equals(key)) {
            return true;
        }
        return false;
    }
    
}
