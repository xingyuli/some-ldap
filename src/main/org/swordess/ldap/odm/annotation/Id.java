package org.swordess.ldap.odm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should only presents in {@link Entry} annotated class. An
 * {@link Id} annotated method means this attribute should be treat as the id
 * attribute (i.e., part of dn).
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Id {
    
    /**
     * Id attribute name in LDAP, if not specified, then the property name in
     * java bean specification will be used. And if both the name of {@link Id}
     * and {@link Attribute} are specified, the value of {@link Id} will be
     * used.
     * 
     * @return
     */
    public String name() default "";
    
}
