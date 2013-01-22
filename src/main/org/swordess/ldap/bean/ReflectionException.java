package org.swordess.ldap.bean;

import org.swordess.ldap.odm.ODMException;


@SuppressWarnings("serial")
public class ReflectionException extends ODMException {

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable e) {
        super(message, e);
    }
    
}
