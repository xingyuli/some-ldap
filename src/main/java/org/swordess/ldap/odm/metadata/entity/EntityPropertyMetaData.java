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
package org.swordess.ldap.odm.metadata.entity;

import java.lang.reflect.Method;

import org.swordess.ldap.odm.annotation.Attribute;
import org.swordess.ldap.odm.annotation.Id;
import org.swordess.ldap.odm.annotation.Syntax;
import org.swordess.ldap.odm.annotation.Syntax.StringSyntaxer;
import org.swordess.ldap.odm.annotation.Syntax.Syntaxer;
import org.swordess.ldap.odm.metadata.RawPropertyMetaData;
import org.swordess.ldap.util.LogUtils;


public class EntityPropertyMetaData extends RawPropertyMetaData {

	private String ldapPropName;
	private Syntaxer syntaxer;
    private boolean isId;
    private boolean isReadonly;
    
    public EntityPropertyMetaData(Method getterMethod) {
        super(getterMethod);

    	// initialize ldapPropName
        determineLdapPropName(getterMethod);
        
        // initialize syntaxer 
        determineSyntaxer(getterMethod);
        
        isId = getterMethod.isAnnotationPresent(Id.class);
        if (getterMethod.isAnnotationPresent(Attribute.class)) {
            isReadonly = getterMethod.getAnnotation(Attribute.class).readonly();
        }
    }

	private void determineSyntaxer(Method getterMethod) {
		Syntax syntax = getterMethod.getAnnotation(Syntax.class);
		if (null != syntax) {
        	try {
				syntaxer = syntax.value().newInstance();
			} catch (Throwable t) {
				LogUtils.error(log, "unable to instantiate Syntaxer " + syntax.value(), t);
			}
        } else {
        	syntaxer = new StringSyntaxer();
        }
	}
    
    private void determineLdapPropName(Method getterMethod) {
        if (getterMethod.isAnnotationPresent(Id.class)) {
            String nameOnId = getterMethod.getAnnotation(Id.class).name();
            if (!"".equals(nameOnId)) {
                ldapPropName = nameOnId;
            }
            
        } else if (getterMethod.isAnnotationPresent(Attribute.class)) {
            String nameOnAttribute = getterMethod.getAnnotation(Attribute.class).name();
            if (!"".equals(nameOnAttribute)) {
                ldapPropName = nameOnAttribute;
            }
        }
        
        /*
         * using java bean specification as the property name if it is neither
         * specified by @Id not @Attribute
         */
        if (null == ldapPropName) {
            ldapPropName = getJavaBeanPropName();
        }
    }
    
    public String getLdapPropName() {
		return ldapPropName;
	}
    
    public Syntaxer getSyntaxer() {
    	return syntaxer;
    }

	public boolean isId() {
        return isId;
    }
    
    public boolean isReadonly() {
        return isReadonly;
    }
    
    @Override
    public String toString() {
        return String.format("ldapPropName=%s | javaBeanPropName=%s | valueClass=%s | syntaxer=%s | isId=%s | isMultiple=%s | isReference=%s | isReadonly=%s",
                getLdapPropName(), getJavaBeanPropName(), getValueClass(), syntaxer.getName(), isId(), isMultiple(), isReference(), isReadonly());
    }
    
}