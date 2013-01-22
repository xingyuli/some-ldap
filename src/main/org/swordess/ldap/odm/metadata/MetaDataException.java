package org.swordess.ldap.odm.metadata;

import org.swordess.ldap.odm.ODMException;


@SuppressWarnings("serial")
public class MetaDataException extends ODMException {

    public MetaDataException(String message) {
        super(message);
    }
    
    public MetaDataException(String message, Throwable reason) {
        super(message, reason);
    }

}
