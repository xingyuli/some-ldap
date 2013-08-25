swordess-ldap
=============

An ODM(i.e., object-directory mapping) project which supports lazy loading and commit effective changes.

Configuration
---------
Currently, there are two ways to configure the swordess-ldap framework.  
### [ClassPathPropertiesSessionFactory](https://github.com/xingyuli/swordess-ldap/blob/master/src/main/java/org/swordess/ldap/odm/core/ClassPathPropertiesSessionFactory.java)
You need to provide a dedicated [odm.properties](https://github.com/xingyuli/swordess-ldap/blob/master/src/test/java/odm.properties) under your classpath.

#### odm.properties
```
# Note: this file should be placed under you classpath.
#
# 1. Connection Pool
# You need to configure all pool related system properties. And all the keys
# are standard JNDI system properties. If you are not quite familiar with
# that, see:
# http://docs.oracle.com/javase/jndi/tutorial/ldap/connect/config.html
#
# 2. Authentication
# This part is about all authentication related environment variables. And all
# the keys are standard JNDI environment keys.

#####################
## connection pool ##
#####################

# connection pool enabled
com.sun.jndi.ldap.connect.pool = true

# if not specified, the default initsize is 1
com.sun.jndi.ldap.connect.pool.initsize = 5

com.sun.jndi.ldap.connect.pool.maxsize = 20

# idle connection in pool which exceed 5mins will be closed and removed
com.sun.jndi.ldap.connect.pool.timeout = 3000000

com.sun.jndi.ldap.connect.pool.debug = all

#####################
## authentication ##
#####################

java.naming.provider.url = ldap://...:389
java.naming.security.authentication = simple
java.naming.security.principal = ...
java.naming.security.credentials = ...
java.naming.ldap.version = 3
```

### [RawContextWrapperSessionFactory](https://github.com/xingyuli/swordess-ldap/blob/master/src/main/java/org/swordess/ldap/odm/core/RawContextWrapperSessionFactory.java)
You need to pass an implementation of *org.swordess.ldap.odm.core.RawContextWrapperSessionFactory.ContextProvider* to the factory.

Demonstrations
--------------
All demos are provided under [/src/test/java](https://github.com/xingyuli/swordess-ldap/tree/master/src/test/java) directory.

#### [SessionDemo](https://github.com/xingyuli/swordess-ldap/blob/master/src/test/java/SessionDemo.java)
```java
    /**
    * Demonstrate how to read an entry as an object with all defined attributes
    * (i.e., all attributes which declared in your POJO).
    */
    public void testReadAllDefinedAttribues() {
    
        SessionFactory sessionFactory = ClassPathPropertiesSessionFactory.getInstance();
        Session session = null;
        
        try {
            session = sessionFactory.openSession();
            GroupDemo group = session.read(GroupDemo.class, DnHelper.build("foo", GroupDemo.class));
            assertNotNull(group.getDN());
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    /**
    * Demonstrate how to read partial attributes of an entity.
    */
    public void testReadSomeAttributes() {
        SessionFactory sessionFactory = ClassPathPropertiesSessionFactory.getInstance();
        try {
            Map<String, Object> group = sessionFactory.getCurrentSession().read(
                GroupDemo.class,
                DnHelper.build("foo", GroupDemo.class),
                new String[] { "longName", "gid" }
            );
            assertNotNull(group.get("longName"));
            assertNotNull(group.get("gid"));
        } finally {
            sessionFactory.getCurrentSession().close();
        }
    }
```

#### [GroupDemo](https://github.com/xingyuli/swordess-ldap/blob/master/src/test/java/GroupDemo.java)
```java
@Entry(
objectClasses = { "group", "structrual", "top" },
context = "ou=groups,ou=example,o=com"
)
public class GroupDemo implements Distinguishable {

    private String dn;
    private String cn;
    private String name;
    
    @Override
    public void setDN(String dn) {
        this.dn = dn;
    }
    
    @Override
    @Transient
    public String getDN() {
        return dn;
    }
    
    @Id
    public String getCn() {
        return cn;
    }
    
    public void setCn(String cn) {
        this.cn = cn;
    }
    
    @Attribute(name = "longName")
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

}
```

Anything not clear, please contact me via:
xingyulliiuu@gmail.com or xingyu_liu@qq.com
