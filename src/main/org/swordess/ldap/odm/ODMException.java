package org.swordess.ldap.odm;

@SuppressWarnings("serial")
public class ODMException extends RuntimeException {

    public ODMException(String message) {
        super(message);
    }

    public ODMException(String message, Throwable e) {
        super(message, e);
    }
    
}
