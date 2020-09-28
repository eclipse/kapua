/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.LoginInfo;

/**
 * AuthenticationService exposes APIs to manage User object under an Account.<br>
 * It includes APIs to create, update, find, list and delete Users.<br>
 * Instances of the UserService can be acquired through the ServiceLocator.
 *
 * @since 1.0
 */
public interface AuthenticationService extends KapuaService {

    /**
     * Login the provided user login credentials on the system (if the credentials are valid)
     *
     * @param loginCredentials
     * @return
     * @throws KapuaException an exception is thrown if the credentials are not found on the system, are expired or are disabled
     */
    AccessToken login(LoginCredentials loginCredentials) throws KapuaException;

    /**
     * Login the provided user login credentials on the system (if the credentials are valid) and enable the trust key
     *
     * @param loginCredentials
     * @param enableTrust
     * @return
     * @throws KapuaException an exception is thrown if the credentials are not found on the system, are expired or are disabled
     */
    AccessToken login(LoginCredentials loginCredentials, boolean enableTrust) throws KapuaException;

    /**
     * FIXME: add javadoc
     *
     * @param sessionCredentials
     * @throws KapuaException an exception is thrown if the credentials are not found on the system, are expired or are disabled
     */
    void authenticate(SessionCredentials sessionCredentials) throws KapuaException;

    /**
     * Logout the current logged user
     *
     * @throws KapuaException
     */
    void logout() throws KapuaException;

    /**
     * Return the {@link AccessToken} identified by the provided token identifier. Expired {@link AccessToken}s are
     * excluded.
     *
     * @param tokenId           The ID of the {@link AccessToken}
     * @return                  The desired {@link AccessToken} object
     * @throws KapuaException if no {@link AccessToken} is found for that token identifier
     */
    AccessToken findAccessToken(String tokenId) throws KapuaException;

    /**
     * Return a Refreshable {@link AccessToken} identified by the provided token identifier. A Refreshable token may be
     * already expired or not, but its Refresh Token is still valid
     *
     * @param tokenId               The ID of the {@link AccessToken}
     * @return                      The desired {@link AccessToken} object
     * @throws KapuaException if no {@link AccessToken} is found for that token identifier
     */
    AccessToken findRefreshableAccessToken(String tokenId) throws KapuaException;

    AccessToken refreshAccessToken(String tokenId, String refreshToken) throws KapuaException;

    /**
     * Verifies the password of a user without logging him in, and thus create any kind of session
     *
     * @param loginCredentials
     * @throws KapuaException an exception is thrown if the credentials are not found on the system, are expired or are disabled
     */
    void verifyCredentials(LoginCredentials loginCredentials) throws KapuaException;

    /**
     * Return the {@link LoginInfo} related to the current session
     * @return the {@link LoginInfo} object containing all the permissions related to the current session and the current {@link AccessToken}
     * @throws KapuaException
     */
    LoginInfo getLoginInfo() throws KapuaException;

}
