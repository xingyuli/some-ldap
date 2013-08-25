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
