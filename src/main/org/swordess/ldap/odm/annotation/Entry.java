package org.swordess.ldap.odm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates the class is an LDAP model.
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entry {
    
    /**
     * <tt>objectclass</tt> values this model should be saved.
     * 
     * @return
     */
    public String[] objectClasses();

    /**
     * Under which context that instances of this class should be saved.
     * 
     * @return
     */
    public String context();
    
}
