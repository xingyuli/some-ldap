package org.swordess.ldap.odm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Syntax {

	public Class<? extends Syntaxer> value() default StringSyntaxer.class;
	
	public static interface Syntaxer {
		public String getName();
		public String javaStringToLdapString(String javaString);
		public String ldapStringToJavaString(String ldapString);
	}
	
	public static class StringSyntaxer implements Syntaxer {
		public String getName() { return "string"; }
		public String javaStringToLdapString(String javaString) { return javaString; }
		public String ldapStringToJavaString(String ldapString) { return ldapString; }
	}
	
	public static class LowercaseStringSyntaxer implements Syntaxer {
		public String getName() { return "string_lowercase"; }
		public String javaStringToLdapString(String javaString) { return javaString.toLowerCase(); }
		public String ldapStringToJavaString(String ldapString) { return ldapString.toLowerCase(); }
	}
	
	public static class UppercaseStringSyntaxer implements Syntaxer {
		public String getName() { return "string_uppercase"; }
		public String javaStringToLdapString(String javaString) { return javaString.toUpperCase(); }
		public String ldapStringToJavaString(String ldapString) { return ldapString.toUpperCase(); }
	}
	
}
