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
package org.swordess.ldap.odm.core;

import org.swordess.ldap.odm.metadata.EntityMetaData;


public class DnHelper {

    /**
     * Construct a dn string via the given value and an {@link Entry} annotated
     * class.
     * 
     * @param idValue
     *        value of id attribute
     * @param entryClass
     *        an {@link Entry} annotated class
     * @return
     */
    public static String build(String idValue, Class<?> entryClass) {
        EntityMetaData metaData = EntityMetaData.get(entryClass);
        String idName = metaData.getIdProperty().getLdapPropName();
        return idName + "=" + idValue + "," + metaData.context();
    }
    
    /**
     * Construct a dn string via the given entity.
     * 
     * @param entity
     *        a persistent entity
     * @return
     */
    public static String build(Object entity) {
        if (null == entity) {
            return null;
        }
        
        Class<?> actualClass = ClassHelper.actualClass(entity.getClass());
        EntityMetaData metaData = EntityMetaData.get(actualClass);
        String idName = metaData.getIdProperty().getLdapPropName();
        String idValue = metaData.getIdProperty().getter().get(entity).toString();
        return idName + "=" + idValue + "," + metaData.context();
    }
    
    private DnHelper() {
    }
    
}
