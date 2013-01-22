package org.swordess.ldap.util;

import org.apache.commons.logging.Log;


public class LogUtils {

    public static void info(Log log, Object message) {
        if (log.isInfoEnabled()) {
            log.info(message);
        }
    }
    
    public static void info(Log log, Object message, Throwable t) {
        if (log.isInfoEnabled()) {
            log.info(message, t);
        }
    }
    
    public static void debug(Log log, Object message) {
        if (log.isDebugEnabled()) {
            log.debug(message);
        }
    }
    
    public static void debug(Log log, Object message, Throwable t) {
        if (log.isDebugEnabled()) {
            log.debug(message, t);
        }
    }
    
    public static void warn(Log log, Object message) {
        if (log.isWarnEnabled()) {
            log.warn(message);
        }
    }
    
    public static void warn(Log log, Object message, Throwable t) {
        if (log.isWarnEnabled()) {
            log.warn(message, t);
        }
    }
    
    public static void error(Log log, Object message) {
        if (log.isErrorEnabled()) {
            log.error(message);
        }
    }
    
    public static void error(Log log, Object message, Throwable t) {
        if (log.isErrorEnabled()) {
            log.error(message, t);
        }
    }
    
    private LogUtils() {
    }
    
}
