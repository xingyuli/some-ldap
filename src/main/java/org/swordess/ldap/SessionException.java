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
