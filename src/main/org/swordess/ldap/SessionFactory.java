package org.swordess.ldap;

import org.swordess.ldap.odm.core.DefaultSessionFactory;

/**
 * Currently we have only one implementation, {@link DefaultSessionFactory}.
 * 
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

}
