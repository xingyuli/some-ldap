package org.swordess.ldap.odm.metadata.indirections;

import org.apache.commons.lang.StringUtils;
import org.swordess.ldap.odm.annotation.Indirections.Indirection;
import org.swordess.ldap.odm.metadata.MetaDataException;

public class IndirectionDescriber {

	private final String context;
	private final String idAttr;
	private final String indirectionAttr;

	IndirectionDescriber(Indirection indirection) {
		if (StringUtils.isEmpty(indirection.context())) {
			throw new MetaDataException("context mustn't be empty string!");
		}
		if (StringUtils.isEmpty(indirection.id())) {
			throw new MetaDataException("id mustn't be empty string!");
		}
		if (StringUtils.isEmpty(indirection.attr())) {
			throw new MetaDataException("attr mustn't be empty string!");
		}
		
		this.context = indirection.context();
		this.idAttr = indirection.id();
		this.indirectionAttr = indirection.attr();
	}

	public String getContext() {
		return context;
	}

	public String getIdAttr() {
		return idAttr;
	}

	public String getIndirectionAttr() {
		return indirectionAttr;
	}
	
	@Override
	public String toString() {
		return String.format("[context=%s, idAttr=%s, indirectionAttr=%s]",
				getContext(), getIdAttr(), getIndirectionAttr());
	}

}
