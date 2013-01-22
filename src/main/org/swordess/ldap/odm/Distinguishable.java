package org.swordess.ldap.odm;

import org.swordess.ldap.odm.annotation.Transient;

/**
 * Should be used together with {@link Entry} annotated class.
 * <p/>
 * 
 * A <b>Distinguishable</b> model need to provide a way to store the dn string.
 * And {@link #getDN()} should be &#064;Transient annotated to tell the
 * framework not treat this method as attribute getter. For example:
 * 
 * <pre>
 * &#064;Entry
 * public class Foo implements Distinguishable {
 * 
 *     private String dn;
 * 
 *     public void setDN(String dn) {
 *         this.dn = dn;
 *     }
 * 
 *     &#064;Transient
 *     public String getDN() {
 *         return dn;
 *     }
 * 
 * }
 * </pre>
 * 
 * The setter {@link #setDN(String)} will be called by ODM framework
 * automatically when constructing entity. This means the getter
 * {@link #getDN()} will always has not empty value of a certain entity.
 * <p/>
 * 
 * We recommend all models implement this interface, as it brings convenient for
 * you automatically.
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 */
public interface Distinguishable {

    public void setDN(String dn);

    /**
     * Should be annotated with {@link Transient} annotation.
     * 
     * @return
     */
    public String getDN();

}
