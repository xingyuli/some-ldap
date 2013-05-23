package org.swordess.ldap.odm.metadata.indirections;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.swordess.ldap.bean.Getter;
import org.swordess.ldap.bean.Setter;
import org.swordess.ldap.odm.annotation.Indirections.Indirection;
import org.swordess.ldap.odm.annotation.Indirections.TheOther;
import org.swordess.ldap.odm.metadata.MetaDataException;
import org.swordess.ldap.odm.metadata.RawPropertyMetaData;
import org.swordess.ldap.odm.metadata.RawPropertyMetaDataInterface;

public class TheOtherMetaData implements RawPropertyMetaDataInterface, Iterable<IndirectionDescriber> {

	private final RawPropertyMetaDataInterface delegation;
	private final List<IndirectionDescriber> describers = new ArrayList<IndirectionDescriber>();
	
	TheOtherMetaData(Method getterMethod) {
		delegation = new RawPropertyMetaData(getterMethod);
		if (!delegation.isMultiple() || String.class != delegation.getValueClass()) {
			throw new MetaDataException(String.format("%s annotation must be annotated on a method with return type List<String>",
					TheOther.class));
		}
		for (Indirection indirection : getterMethod.getAnnotation(TheOther.class).value()) {
			describers.add(new IndirectionDescriber(indirection));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Getter<List<String>> getter() {
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
	public Class<String> getValueClass() {
		return String.class;
	}

	@Override
	public boolean isMultiple() {
		return true;
	}

	@Override
	public boolean isReference() {
		return false;
	}

	public List<IndirectionDescriber> getDescribers() {
		return describers;
	}

	public String dnToIndirectionAttr(String dn) {
		if (StringUtils.isEmpty(dn)) {
			return null;
		}
		
		for (IndirectionDescriber each : this) {
			if (dn.toLowerCase().endsWith(each.getContext().toLowerCase())) {
				return each.getIndirectionAttr();
			}
		}
		return null;
	}
	
	@Override
	public Iterator<IndirectionDescriber> iterator() {
		return describers.iterator();
	}
	
	@Override
    public String toString() {
		StringBuilder describersStr = new StringBuilder();
		describersStr.append("[\n");
		for (IndirectionDescriber describer : this) {
			describersStr.append(describer);
			describersStr.append("\n");
		}
		describersStr.append("]");
        return String.format("javaBeanPropName=%s | valueClass=%s | isMultiple=%s | describers=%s",
                getJavaBeanPropName(), getValueClass(), isMultiple(), describersStr);
    }
	
}
