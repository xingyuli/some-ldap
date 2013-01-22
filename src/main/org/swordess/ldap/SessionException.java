package org.swordess.ldap;

@SuppressWarnings("serial")
public class SessionException extends RuntimeException {

    public SessionException(String message) {
        super(message);
    }

    public SessionException(String message, Throwable e) {
        super(message, e);
    }
    
}
