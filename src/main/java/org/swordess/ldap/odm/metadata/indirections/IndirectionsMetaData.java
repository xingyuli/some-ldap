package org.swordess.ldap.odm.metadata.indirections;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.swordess.ldap.odm.annotation.Indirections;
import org.swordess.ldap.odm.annotation.Indirections.One;
import org.swordess.ldap.odm.annotation.Indirections.TheOther;
import org.swordess.ldap.odm.annotation.Transient;
import org.swordess.ldap.odm.metadata.MetaDataException;
import org.swordess.ldap.util.LogUtils;

public class IndirectionsMetaData {

	private static final Log LOG = LogFactory.getLog(IndirectionsMetaData.class);
	
	private static Map<Class<?>, IndirectionsMetaData> metaDataMap = new HashMap<Class<?>, IndirectionsMetaData>();
	
	private final Class<?> managedClass;
	
	private OneMetaData one;
	private TheOtherMetaData theOther;
	
	private IndirectionsMetaData(Class<?> clazz) {
		this.managedClass = clazz;
		
		LogUtils.debug(LOG, "Extracting metadata from " + clazz);
		
		Indirections indirections = clazz.getAnnotation(Indirections.class);
		if (null == indirections) {
			throw new MetaDataException(String.format("Class %s must have a class level %s annotation", clazz, Indirections.class));
		}
		
		for (Method m : clazz.getDeclaredMethods()) {
			m.setAccessible(true);
			
			if (Modifier.isStatic(m.getModifiers()) || m.isAnnotationPresent(Transient.class)) {
				continue;
			}
			
			if (m.isAnnotationPresent(One.class)) {
				if (null != one) {
					throw new MetaDataException(String.format("You must have only one method with the %s annotation in class %s",
							One.class, clazz));
				}
				try {
					one = new OneMetaData(m);
				} catch (MetaDataException e){
					throw new MetaDataException(One.class + " in " + clazz, e);
				}
				
			} else if (m.isAnnotationPresent(TheOther.class)) {
				if (null != theOther) {
					throw new MetaDataException(String.format("You must have only one method with the %s annotation in class %s",
							TheOther.class, clazz));
				}
				try {
					theOther = new TheOtherMetaData(m);
				} catch (MetaDataException e) {
					throw new MetaDataException(TheOther.class + " in " + clazz, e);
				}
			}
		}
		
		LogUtils.debug(LOG, String.format("Extracted metadata from %s as %s", clazz, this));
	}
	
	public Class<?> getManagedClass() {
		return managedClass;
	}
	
	public OneMetaData getOne() {
		return one;
	}

	public TheOtherMetaData getTheOther() {
		return theOther;
	}

	@Override
	public String toString() {
		return String.format("[%none=[%s]%ntheOther=[%s]", one, theOther);
	}
	
	public static IndirectionsMetaData get(Class<?> clazz) {
		IndirectionsMetaData metaData = metaDataMap.get(clazz);
		if (null == metaData) {
			metaData = new IndirectionsMetaData(clazz);
			metaDataMap.put(clazz, metaData);
		}
		return metaData;
	}
	
}
