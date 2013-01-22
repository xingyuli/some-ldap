package org.swordess.ldap.odm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should only presents in {@link Entry} annotated class. An
 * {@link Transient} annotated method means this method has no relationship will
 * LDAP server, and should not be regarded as an attribute.
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transient {
}
