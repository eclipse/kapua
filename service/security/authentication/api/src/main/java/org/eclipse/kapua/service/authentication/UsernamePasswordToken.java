/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication;

/**
 * Username and password {@link AuthenticationCredentials} definition.
 * 
 * @since 1.0
 * 
 */
public interface UsernamePasswordToken extends AuthenticationCredentials
{
    /**
     * return the username
     * 
     * @return
     */
    public String getUsername();

    /**
     * Set the username
     * 
     * @param username
     */
    public void setUsername(String username);

    /**
     * return the password
     * 
     * @return
     */
    public char[] getPassword();

    /**
     * Set the password
     * 
     * @param password
     */
    public void setPassword(char[] password);
}
