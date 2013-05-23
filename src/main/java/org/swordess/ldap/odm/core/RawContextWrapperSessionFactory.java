package org.swordess.ldap.odm.core;

import java.util.HashMap;
import java.util.Map;

import javax.naming.ConfigurationException;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

public class RawContextWrapperSessionFactory extends AbstractThreadLocalSessionFactory {

	private static Map<ContextProvider, RawContextWrapperSessionFactory> factories = new HashMap<ContextProvider, RawContextWrapperSessionFactory>();

	private final ContextProvider provider;

	private RawContextWrapperSessionFactory(ContextProvider provider) {
		this.provider = provider;
	}

	@Override
	protected InitialLdapContext getContext() throws NamingException {
		if (null != provider) {
			return provider.getContext();
		}
		throw new ConfigurationException("ContextProvider is not configured");
	}

	public static interface ContextProvider {
		public InitialLdapContext getContext() throws NamingException;
	}

	public static RawContextWrapperSessionFactory getInstance(ContextProvider provider) {
		RawContextWrapperSessionFactory factory = factories.get(provider);
		if (null == factory) {
			factory = new RawContextWrapperSessionFactory(provider);
			factories.put(provider, factory);
		}
		return factory;
	}

}
