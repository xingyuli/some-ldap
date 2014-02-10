package org.swordess.ldap.odm.core;

import java.util.AbstractMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import org.swordess.ldap.Session;
import org.swordess.ldap.SessionFactory;
import org.swordess.ldap.odm.ODMException;

public abstract class AbstractThreadLocalSessionFactory implements SessionFactory {

	private ThreadLocal<Map.Entry<Session, InitialLdapContext>> sessions = new ThreadLocal<Map.Entry<Session, InitialLdapContext>>();

	protected abstract InitialLdapContext getContext() throws NamingException;

	@Override
	public Session openSession() {
		try {
			return new SessionImpl(this, getContext(), false);
		} catch (NamingException e) {
			throw new ODMException("Cannot instantiate a session");
		}
	}

	/**
	 * Return the session which the current thread holds. If not exist, a new
	 * session will be created and bind to current thread.
	 * 
	 * @return
	 */
	@Override
	public Session getCurrentSession() {
		Map.Entry<Session, InitialLdapContext> current = sessions.get();
		if (null == current) {
			try {
				InitialLdapContext ldapConnection = getContext();
				Session session = new SessionImpl(this, ldapConnection, true);
				current = new AbstractMap.SimpleEntry<Session, InitialLdapContext>(
						session, ldapConnection);
			} catch (NamingException e) {
				throw new ODMException("Cannot instantiate a session", e);
			}
		}
		return current.getKey();
	}

	@Override
	public void closeCurrentSession() {
		Map.Entry<Session, InitialLdapContext> current = sessions.get();
		if (null != current) {
			closeLdapContext(current.getValue());
			sessions.set(null);
		}
	}

	/* package */void closeLdapContext(InitialLdapContext ctx) {
		if (null != ctx) {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}

}
