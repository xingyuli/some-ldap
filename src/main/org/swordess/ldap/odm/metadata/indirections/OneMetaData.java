package org.swordess.ldap.odm.metadata.indirections;

import java.lang.reflect.Method;

import org.swordess.ldap.bean.Getter;
import org.swordess.ldap.bean.Setter;
import org.swordess.ldap.odm.annotation.Indirections.One;
import org.swordess.ldap.odm.metadata.MetaDataException;
import org.swordess.ldap.odm.metadata.RawPropertyMetaData;
import org.swordess.ldap.odm.metadata.RawPropertyMetaDataInterface;

public class OneMetaData implements RawPropertyMetaDataInterface {
	
	private final RawPropertyMetaDataInterface delegation;
	private final IndirectionDescriber describer;

	OneMetaData(Method getterMethod) {
		delegation = new RawPropertyMetaData(getterMethod);
		if (delegation.isMultiple() || String.class != delegation.getValueClass()) {
			throw new MetaDataException(String.format("%s annotation must be annotated on a method with return type %s",
					One.class, String.class));
		}
		describer = new IndirectionDescriber(getterMethod.getAnnotation(One.class).value());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Getter<String> getter() {
		return delegation.getter();
	}

	@Override
	public Setter setter() {
		return delegation.setter();
	}

	@Override
	public String getJavaBeanPropName() {
		return delegation.getJavaBeanPropName();
	}

	@Override
	public Class<?> getValueClass() {
		return String.class;
	}

	@Override
	public boolean isMultiple() {
		return false;
	}

	@Override
	public boolean isReference() {
		return false;
	}
	
	public IndirectionDescriber getDescriber() {
		return describer;
	}

	public String getContext() {
		return describer.getContext();
	}

	public String getIdAttr() {
		return describer.getIdAttr();
	}

	public String getIndirectionAttr() {
		return describer.getIndirectionAttr();
	}
	
	@Override
    public String toString() {
        return String.format("javaBeanPropName=%s | valueClass=%s | isMultiple=%s | describer=%s",
                getJavaBeanPropName(), getValueClass(), isMultiple(), describer);
    }
	
}
