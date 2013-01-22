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
