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
package org.swordess.ldap.odm.metadata.entity;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.swordess.ldap.odm.annotation.Attribute;
import org.swordess.ldap.odm.annotation.Entry;
import org.swordess.ldap.odm.annotation.Id;
import org.swordess.ldap.odm.annotation.Transient;
import org.swordess.ldap.odm.metadata.MetaDataException;
import org.swordess.ldap.util.LogUtils;



public class EntityMetaData implements Iterable<EntityPropertyMetaData> {

    private static final Log LOG = LogFactory.getLog(EntityMetaData.class);

    private static Map<Class<?>, EntityMetaData> metaDataMap = new HashMap<Class<?>, EntityMetaData>();
    
    private final Class<?> managedClass;
    
    /**
     * LDAP branch the managed class should be saved to.
     */
    private String context;
    
    /**
     * Values of objectclass which will be saved to LDAP server.
     */
    private String[] objectClasses;
    
    private EntityPropertyMetaData idProperty;
    
    private Map<String, EntityPropertyMetaData> ldapPropNameToMetaData = new HashMap<String, EntityPropertyMetaData>();
    private Map<String, EntityPropertyMetaData> javaBeanPropNameToMetaData = new HashMap<String, EntityPropertyMetaData>();
    
    // cache all defined attribute names in order not to create the same array
    private String[] allDefinedAttrNames;
    
    private EntityMetaData(Class<?> clazz) {
        this.managedClass = clazz;
        
        LogUtils.debug(LOG, "Extracting metadata from " + clazz);
        
        Entry entry = clazz.getAnnotation(Entry.class);
        if (null == entry) {
            throw new MetaDataException(String.format("Class %s must have a class level %s annotation", clazz, Entry.class));
        }
        
        context = entry.context();
        objectClasses = entry.objectClasses();
        
        Set<String> allDefinedAttrNameSet = new HashSet<String>();
        for (Method m : clazz.getDeclaredMethods()) {
            m.setAccessible(true);
            
            // static methods and transient methods are not managed
            if (Modifier.isStatic(m.getModifiers()) || m.isAnnotationPresent(Transient.class)) {
                continue;
            }
            
            // methods without @Id or @Attribute are not managed
            if (!m.isAnnotationPresent(Id.class) && !m.isAnnotationPresent(Attribute.class)) {
                continue;
            }
            
            EntityPropertyMetaData currentPropertyMetaData = new EntityPropertyMetaData(m);
            if (currentPropertyMetaData.isId()) {
                if (null != idProperty) {
                    throw new MetaDataException(String.format("You must have only one method with the %s annotation in class %s",
                            Id.class, clazz));
                }
                idProperty = currentPropertyMetaData;
            }
            ldapPropNameToMetaData.put(currentPropertyMetaData.getLdapPropName(), currentPropertyMetaData);
            javaBeanPropNameToMetaData.put(currentPropertyMetaData.getJavaBeanPropName(), currentPropertyMetaData);
            allDefinedAttrNameSet.add(currentPropertyMetaData.getLdapPropName());
        }
        allDefinedAttrNames = allDefinedAttrNameSet.toArray(new String[0]);
        
        if (null == idProperty) {
            throw new MetaDataException(String.format("All Entry classes must define a property with the %s annotation, error in class %s",
                    Id.class, clazz));
        }
        
        LogUtils.debug(LOG, String.format("Extracted metadata from %s as %s", clazz, this));
    }

    @Override
    public Iterator<EntityPropertyMetaData> iterator() {
        return ldapPropNameToMetaData.values().iterator();
    }
    
    public Class<?> getManagedClass() {
        return managedClass;
    }
    
    public String context() {
        return context;
    }
    
    public String[] objectClasses() {
        return objectClasses;
    }
    
    public EntityPropertyMetaData getIdProperty() {
        return idProperty;
    }
    
    public EntityPropertyMetaData getProperty(String ldapPropName) {
        return ldapPropNameToMetaData.get(ldapPropName);
    }
    
    public EntityPropertyMetaData getPropertyByJavaBeanPropName(String javaBeanPropName) {
        return javaBeanPropNameToMetaData.get(javaBeanPropName);
    }
    
    @Override
    public String toString() {
        StringBuilder propertiesInfo = new StringBuilder();
        for (EntityPropertyMetaData propertyMetaData : this) {
            propertiesInfo.append("\n");
            propertiesInfo.append(propertyMetaData);
        }
        propertiesInfo.append("\n");
        return String.format("objectClasses=%s | idProperty=%s | properties=[%s]",
                Arrays.toString(objectClasses), idProperty.getLdapPropName(), propertiesInfo);
    }
 
    public static EntityMetaData get(Class<?> clazz) {
        EntityMetaData metaData = metaDataMap.get(clazz);
        if (null == metaData) {
            metaData = new EntityMetaData(clazz);
            metaDataMap.put(clazz, metaData);
        }
        return metaData;
    }
    
    public static String[] getDefinedAttrNames(Class<?> clazz) {
        return get(clazz).allDefinedAttrNames;
    }
    
}
