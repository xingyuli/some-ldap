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
import org.swordess.ldap.odm.Distinguishable;
import org.swordess.ldap.odm.annotation.Attribute;
import org.swordess.ldap.odm.annotation.Entry;
import org.swordess.ldap.odm.annotation.Id;
import org.swordess.ldap.odm.annotation.Transient;

/**
 * The following configuration correspond to entries which stored under branch
 * "ou=groups,ou=example,o=com" and each entry should have objectclass with
 * attribute values "group", "structrual" and "top".
 * <p/>
 * 
 * More details, please refer to
 * <code>org.swordess.ldap.odm.annotation.Entry</code>.
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 * 
 */
@Entry(
	objectClasses = { "group", "structrual", "top" },
	context = "ou=groups,ou=example,o=com"
)
public class GroupDemo implements Distinguishable {

	/**
	 * We use a field to store the dn. But you can choose some other way to
	 * achieve this goal, e.g, a key-value pair, in that case the setDN(String)
	 * would like info.set("dn", dn), and getDN() would like info.get("dn")
	 */
	private String dn;
	
	/**
	 * assume that "cn" is the id attribute in LDAP
	 */
	private String cn;
	
	/**
	 * this is an normal attribute in LDAP
	 */
	private String name;
	
	@Override
	public void setDN(String dn) {
		this.dn = dn;
	}

	/**
	 * Transient annotation should be used if DN should not be treat as an
	 * attribute.<p/>
	 * 
	 * More details, please refer to
	 * <code>org.swordess.ldap.odm.Distinguishable</code>.
	 */
	@Override
	@Transient
	public String getDN() {
		return dn;
	}

	/**
	 * "cn" will be treat as id attribute in LDAP, so the final dn will be
	 * something like: cn=foo,ou=groups,ou=example,o=com
	 * <p/>
	 * 
	 * More details, please refer to
	 * <code>org.swordess.ldap.odm.annotation.Id</code>.
	 * 
	 * @return
	 */
	@Id
	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	/**
	 * We can simplify the getter's name via specifying the name of Attribute.
	 * <p/>
	 * 
	 * More details, please refer to
	 * <code>org.swordess.ldap.odm.annotation.Attribute</code>.
	 * 
	 * @return
	 */
	@Attribute(name = "longName")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
