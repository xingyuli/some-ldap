package org.swordess.ldap.odm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should only presents in {@link Entry} annotated class. An
 * {@link Attribute} annotated method means it should be regard as a getter
 * method of a certain LDAP attribute.
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Attribute {
    
    /**
     * Attribute name in LDAP, if not specified, then the property name in java
     * bean specification will be used.
     * 
     * @return
     */
    public String name() default "";
    
    /**
     * Indicates whether this attribute should be treat as read-only or not.
     * When set to <tt>true</tt>, any changes to this attribute will be ignored
     * when update operations occur.
     * <p/>
     * 
     * <b>NOTE: </b> Read only attributes still can be summit to LDAP server
     * when create operations happens.
     * 
     * @return
     */
    public boolean readonly() default false;
    
}
