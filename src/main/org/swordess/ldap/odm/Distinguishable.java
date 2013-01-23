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
