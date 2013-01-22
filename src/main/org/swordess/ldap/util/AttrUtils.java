package org.swordess.ldap.util;

import java.util.*;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;


/**
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 */
public class AttrUtils {

    public static <T> Attribute create(String id, Object value, Evaluator<T> evaluator) {
        if (null == value) {
            return null;
        }
        
        if (null == evaluator) {
            return new BasicAttribute(id, value); 
        } else {
            T evaled = evaluator.eval(value);
            return null != evaled ? new BasicAttribute(id, evaled) : null;
        }
    }
    
    /**
     * Construct an attribute who has multiple values.
     * 
     * @param id
     *            attribute id
     * @param values
     *            attribute values
     * @return
     * 
     * @see {@link #create(String, Object[])}
     */
    public static Attribute create(String id, Collection<?> values) {
        return create(id, values, null);
    }
    
    public static <T> Attribute create(String id, Collection<?> values, Evaluator<T> evaluator) {
        if (StringUtils.isEmpty(id) || CollectionUtils.isEmpty(values)) {
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
        
        return hasOneNotNullAtLeast ? attr : null;
    }

    /**
     * Construct an attribute who has multiple values.
     * 
     * @param id
     *            attribute id
     * @param values
     *            attribute values
     * @return
     */
    public static Attribute create(String id, Object[] values) {
        return create(id, values, null);
    }
    
    public static <T> Attribute create(String id, Object[] values, Evaluator<T> evaluator) {
        if (StringUtils.isEmpty(id) || ArrayUtils.isEmpty(values)) {
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
        
        return hasOneNotNullAtLeast ? attr : null;
    }

    /**
     * Construct attributes with the same value.
     * 
     * @param ids
     *            id of each attribute
     * @param value
     * @return
     */
    public static List<Attribute> create(String[] ids, Object value) {
        if (null == value) {
            return null;
        }
        
        List<Attribute> attrs = new ArrayList<Attribute>();
        for (String id : ids) {
            attrs.add(new BasicAttribute(id, value));
        }
        return attrs;
    }
    
    /**
     * Construct attributes whose ids and values are specified in ordering.
     * <p/>
     * NOTE: The length of <tt>ids</tt> and <tt>values</tt> must be equal, otherwise a RuntimeException will be thrown.
     * 
     * @param ids
     *            ids in ordering
     * @param values
     *            values in ordering
     * @return
     */
    public static Attributes create(String[] ids, Object[] values) {
        if (ArrayUtils.isEmpty(ids) || ArrayUtils.isEmpty(values) || ids.length != values.length) {
            throw new RuntimeException("length of ids and values are not match");
        }
        
        Attributes attrs = new BasicAttributes();
        for (int i = 0; i < ids.length; i++) {
            attrs.put(ids[i], values[i]);
        }
        return attrs;
    }

    /**
     * Get values from the given <tt>attr</tt>.
     * 
     * @param attr
     * @return
     * @throws NamingException
     *             If a naming exception was encountered while retrieving the value.
     */
    public static List<Object> values(Attribute attr) throws NamingException {
        List<Object> values = new ArrayList<Object>();
        for (NamingEnumeration<?> all = attr.getAll(); all.hasMore();) {
            values.add(all.next());
        }
        return values;
    }
    
    /**
     * Get values from the given <tt>attr</tt>.
     * <p/>
     * The difference between this method and {@link #values(Attribute)} are:
     * <ul>
     * <li>if the given attr has one single value, then a normal object will be returned</li>
     * <li>if the given attr has multiple values, then a List will be returned</li>
     * </ul>
     * 
     * @param attr
     * @return the value Object, or {@code null} if no value exists
     * @throws NamingException
     *             If a naming exception was encountered while retrieving the value.
     */
    public static Object valuesAsObject(Attribute attr) throws NamingException {
        List<Object> values = values(attr);
        if (0 == values.size()) {
            return null;
        } else if (1 == values.size()) {
            return values.get(0);
        } else {
            return values;
        }
    }

    /**
     * Add several attributes with a same value into <tt>attrs</tt>.
     * 
     * @param attrs
     * @param ids
     *            ids of each attribute
     * @param value
     */
    public static void put(Attributes attrs, String[] ids, Object value) {
        if (null == attrs) {
            return;
        }
        for (Attribute toPut : create(ids, value)) {
            attrs.put(toPut);
        }
    }
    
    public static void putIfNotNull(Attributes attrs, Attribute toPut) {
        if (null != toPut) {
            attrs.put(toPut);
        }
    }
    
    private AttrUtils() {
    }

}
