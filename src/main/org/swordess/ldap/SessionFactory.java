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
package org.swordess.ldap;


/**
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 */
public interface SessionFactory {

    /**
     * A list of space-separated authentication types of connections that may be
     * pooled. Valid types are "none", "simple", and "DIGEST-MD5".
     */
    public static final String POOL_AUTHENTICATION = "com.sun.jndi.ldap.connect.pool.authentication";

    /**
     * A string that indicates the level of debug output to produce. Valid
     * values are "fine" (trace connection creation and removal) and "all" (all
     * debugging information).
     */
    public static final String POOL_DEBUG = "com.sun.jndi.ldap.connect.pool.debug";

    /**
     * The string representation of an integer that represents the number of
     * connections per connection identity to create when initially creating a
     * connection for the identity.
     */
    public static final String POOL_INITSIZE = "com.sun.jndi.ldap.connect.pool.initsize";

    /**
     * The string representation of an integer that represents the maximum
     * number of connections per connection identity that can be maintained
     * concurrently.
     */
    public static final String POOL_MAXSIZE = "com.sun.jndi.ldap.connect.pool.maxsize";
    
    /**
     * The string representation of an integer that represents the preferred
     * number of connections per connection identity that should be maintained
     * concurrently.
     */
    public static final String POOL_PREFSIZE = "com.sun.jndi.ldap.connect.pool.prefsize";
    
    /**
     * A list of space-separated protocol types of connections that may be
     * pooled. Valid types are "plain" and "ssl".
     */
    public static final String POOL_PROTOCOL = "com.sun.jndi.ldap.connect.pool.protocol";
    
    /**
     * The string representation of an integer that represents the number of
     * milliseconds that an idle connection may remain in the pool without being
     * closed and removed from the pool.
     */
    public static final String POOL_TIMEOUT = "com.sun.jndi.ldap.connect.pool.timeout";
    
    /**
     * Always open a new session.
     * 
     * @return
     */
    public Session openSession();

    /**
     * Return the current session. If not exist, a new session will be created.
     * 
     * @return
     */
    public Session getCurrentSession();
    
    public void closeCurrentSession();

}
