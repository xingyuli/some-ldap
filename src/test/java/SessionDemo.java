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
import java.util.Map;

import junit.framework.TestCase;

import org.swordess.ldap.Session;
import org.swordess.ldap.SessionFactory;
import org.swordess.ldap.odm.core.ClassPathPropertiesSessionFactory;
import org.swordess.ldap.odm.core.DnHelper;


public class SessionDemo extends TestCase {

	/**
	 * Demonstrate how to read an entry as an object with all defined attributes
	 * (i.e., all attributes which declared in your POJO).
	 */
	public void testReadAllDefinedAttribues() {
		/*
		 * Get the default SessionFactory which relies on odm.properties.
		 * 
		 * More details, please refer to
		 * org.swordess.ldap.odm.core.DefaultSessionFactory
		 */
		SessionFactory sessionFactory = ClassPathPropertiesSessionFactory.getInstance();
		
		/*
		 * We recommend to use try-finally block to release resources occupied
		 * by Session correctly.
		 * 
		 * More details, please refer to org.swordess.ldap.Session#close()
		 */
		Session session = null;
		try {
			session = sessionFactory.openSession();
			
			/*
			 * Get group entity with all defined attributes. As you could see
			 * that currently we only have cn and longName defined in POJO
			 * GroupDemo. Thus only these two attributes will be fetched and
			 * returned.
			 */
			GroupDemo group = session.read(GroupDemo.class, DnHelper.build("foo", GroupDemo.class));
			
			/*
			 * The dn string will never be null if we call getDn() from an entity.
			 * 
			 * More details, please refer to
			 * org.swordess.ldap.odm.Distinguishable
			 */
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
		/*
		 * Use the default SessionFactory.
		 */
		SessionFactory sessionFactory = ClassPathPropertiesSessionFactory.getInstance();
		
		/*
		 * An alternative approach to release resources of a Session.
		 * 
		 * More details, please refer to org.swordess.ldap.Session#close()
		 */
		try {
			/*
			 * Fetch some attributes only, note that its not limited to fetch
			 * defined attributes. That means you could fetch attributes which
			 * do exist in LDAP side, but not defined in POJO.
			 * 
			 * More details, please refer to
			 * org.swordess.ldap.Session#read(Class, String, String[])
			 */
			Map<String, Object> group = sessionFactory.getCurrentSession().read(
					GroupDemo.class,
					DnHelper.build("foo", GroupDemo.class),
					new String[] { "longName", "gid" }
				);
			assertNotNull(group.get("longName"));
			assertNotNull(group.get("gid"));
		} finally {
			/*
			 * Use current session approach to release resources. 
			 */
			sessionFactory.getCurrentSession().close();
		}
	}
	
}
