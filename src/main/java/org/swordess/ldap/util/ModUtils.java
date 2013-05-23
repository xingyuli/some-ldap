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
package org.swordess.ldap.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.apache.commons.lang.ArrayUtils;

public class ModUtils {
    
    public static ModificationItem add(String id, Object value) {
        return create(DirContext.ADD_ATTRIBUTE, id, value, null);
    }
    
    public static <T> ModificationItem add(String id, Object value, Evaluator<T> evaluator) {
        return create(DirContext.ADD_ATTRIBUTE, id, value, evaluator);
    }

    public static ModificationItem add(String id, Object[] values) {
        return create(DirContext.ADD_ATTRIBUTE, id, values, null);
    }

    public static <T> ModificationItem add(String id, Object[] values, Evaluator<T> evaluator) {
        return create(DirContext.ADD_ATTRIBUTE, id, values, evaluator);
    }
    
    public static ModificationItem add(String id, Collection<?> values) {
        return create(DirContext.ADD_ATTRIBUTE, id, values, null);
    }
    
    public static <T> ModificationItem add(String id, Collection<?> values, Evaluator<T> evaluator) {
        return create(DirContext.ADD_ATTRIBUTE, id, values, evaluator);
    }

    public static ModificationItem replace(String id, Object value) {
        return create(DirContext.REPLACE_ATTRIBUTE, id, value, null);
    }
    
    public static <T> ModificationItem replace(String id, Object value, Evaluator<T> evaluator) {
        return create(DirContext.REPLACE_ATTRIBUTE, id, value, evaluator);
    }

    public static ModificationItem replace(String id, Object[] values) {
        return create(DirContext.REPLACE_ATTRIBUTE, id, values, null);
    }

    public static <T> ModificationItem replace(String id, Object[] values, Evaluator<T> evaluator) {
        return create(DirContext.REPLACE_ATTRIBUTE, id, values, evaluator);
    }
    
    public static ModificationItem replace(String id, Collection<?> values) {
        return create(DirContext.REPLACE_ATTRIBUTE, id, values, null);
    }

    public static <T> ModificationItem replace(String id, Collection<?> values, Evaluator<T> evaluator) {
        return create(DirContext.REPLACE_ATTRIBUTE, id, values, evaluator);
    }
    
    public static ModificationItem remove(String id) {
        return new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(id));
    }

    public static ModificationItem remove(String id, Object value) {
        return create(DirContext.REMOVE_ATTRIBUTE, id, value, null);
    }
    
    public static <T> ModificationItem remove(String id, Object value, Evaluator<T> evaluator) {
        return create(DirContext.REMOVE_ATTRIBUTE, id, value, evaluator);
    }

    public static ModificationItem remove(String id, Object[] values) {
        return create(DirContext.REMOVE_ATTRIBUTE, id, values, null);
    }
    
    public static <T> ModificationItem remove(String id, Object[] values, Evaluator<T> evaluator) {
        return create(DirContext.REMOVE_ATTRIBUTE, id, values, evaluator);
    }

    public static ModificationItem remove(String id, Collection<?> values) {
        return create(DirContext.REMOVE_ATTRIBUTE, id, values, null);
    }
    
    public static <T> ModificationItem remove(String id, Collection<?> values, Evaluator<T> evaluator) {
        return create(DirContext.REMOVE_ATTRIBUTE, id, values, evaluator);
    }

    public static List<ModificationItem> gatherModsForAdd(String fieldName, List<String> ldapValues, List<String> toCheck) {
        List<String> valuesToAdd = new ArrayList<String>(toCheck);
        valuesToAdd.removeAll(ldapValues);

        if (valuesToAdd.isEmpty()) {
            return Collections.emptyList();
        }

        List<ModificationItem> mods = new ArrayList<ModificationItem>();
        mods.add(ModUtils.add(fieldName, valuesToAdd));
        return mods;
    }

    public static List<ModificationItem> gatherModsForRemove(String fieldName, List<String> ldapValues, List<String> toCheck) {
        List<String> valuesToRemove = new ArrayList<String>(ldapValues);
        valuesToRemove.removeAll(toCheck);

        if (valuesToRemove.isEmpty()) {
            return Collections.emptyList();
        }

        List<ModificationItem> mods = new ArrayList<ModificationItem>();
        mods.add(ModUtils.remove(fieldName, valuesToRemove));
        return mods;
    }
    
    private static <T> ModificationItem create(int operationMod, String id, Object value, Evaluator<T> evaluator) {
        if (null == value) {
            return null;
        }
        
        if (null == evaluator) {
            return new ModificationItem(operationMod, new BasicAttribute(id, value));
        } else {
            T evaled = evaluator.eval(value);
            return null != evaled ? new ModificationItem(operationMod, new BasicAttribute(id, evaled)) : null;
        }
    }
    
    private static <T> ModificationItem create(int operationMod, String id, Object[] values, Evaluator<T> evaluator) {
        if (ArrayUtils.isEmpty(values)) {
            return null;
        }
        
        boolean hasOneNotNullAtLeast = false;
        Attribute attr = new BasicAttribute(id);
        
        if (null == evaluator) {
            for (Object value : values) {
                if (null != value) {
                    hasOneNotNullAtLeast = true;
                    attr.add(value);
                }
            }
            
        } else {
            for (Object value : values) {
                if (null == value) {
                    continue;
                }
                T evaled = evaluator.eval(value);
                if (null != evaled) {
                    hasOneNotNullAtLeast = true;
                    attr.add(evaled);
                }
            }
        }
        return hasOneNotNullAtLeast ? new ModificationItem(operationMod, attr) : null;
    }
    
    private static <T> ModificationItem create(int operationMod, String id, Collection<?> values, Evaluator<T> evaluator) {
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }
        
        boolean hasOneNotNullAtLeast = false;
        Attribute attr = new BasicAttribute(id);
        
        if (null == evaluator) {
            for (Object value : values) {
                if (null != value) {
                    hasOneNotNullAtLeast = true;
                    attr.add(value);
                }
            }
            
        } else {
            for (Object value : values) {
                if (null == value) {
                    continue;
                }
                T evaled = evaluator.eval(value);
                if (null != evaled) {
                    hasOneNotNullAtLeast = true;
                    attr.add(evaled);
                }
            }
        }
        return hasOneNotNullAtLeast ? new ModificationItem(operationMod, attr) : null;
    }
    
    private ModUtils() {
    }

}
