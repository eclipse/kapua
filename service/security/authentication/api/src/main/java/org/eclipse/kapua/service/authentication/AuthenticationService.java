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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.KapuaService;

/**
 * AuthenticationService exposes APIs to manage User object under an Account.<br>
 * It includes APIs to create, update, find, list and delete Users.<br>
 * Instances of the UserService can be acquired through the ServiceLocator.
 * 
 * @since 1.0
 * 
 */
public interface AuthenticationService extends KapuaService
{
    /**
     * Login the provided user credentials on the system (if the credentials are valid)
     * 
     * @param authenticationToken
     * @return
     * @throws KapuaException an exception is thrown if the credentials are not found on the system, are expired or are disabled
     */
    public AccessToken login(AuthenticationCredentials authenticationToken)
        throws KapuaException;

    /**
     * Logout the current logged user
     * 
     * @throws KapuaException
     */
    public void logout()
        throws KapuaException;
    
    /**
     * Return the {@link AccessToken} identified by the provided token identifier
     * 
     * @param tokenId
     * @return
     * @throws KapuaException if no {@link AccessToken} is found for that token identifier
     */
    public AccessToken getToken(String tokenId) 
    	throws KapuaException;

}
