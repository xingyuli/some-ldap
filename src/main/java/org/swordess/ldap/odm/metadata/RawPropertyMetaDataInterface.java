package org.swordess.ldap.odm.metadata;

import org.swordess.ldap.bean.Getter;
import org.swordess.ldap.bean.Setter;

public interface RawPropertyMetaDataInterface {

	public abstract Getter getter();

	public abstract Setter setter();

	public abstract String getJavaBeanPropName();

	public abstract Class<?> getValueClass();

	public abstract boolean isMultiple();

	public abstract boolean isReference();

}