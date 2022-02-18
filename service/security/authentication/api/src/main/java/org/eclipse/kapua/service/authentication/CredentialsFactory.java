/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.authentication;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link CredentialsFactory} factory definition.
 */
public interface CredentialsFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link UsernamePasswordCredentials} instance based on provided username and password
     *
     * @param username the name of the user
     * @param password the password of the user
     * @return the new credentials
     */
    UsernamePasswordCredentials newUsernamePasswordCredentials(String username, String password);

    /**
     * Creates a new {@link UsernamePasswordCredentials} instance based on username and password with no preset values
     *
     * @return the new, empty credentials instance
     */
    default UsernamePasswordCredentials newUsernamePasswordCredentials() {
        return newUsernamePasswordCredentials(null, null);
    }

    /**
     * Creates a new {@link ApiKeyCredentials} instance based on provided api key
     *
     * @param apiKey
     * @return
     */
    ApiKeyCredentials newApiKeyCredentials(String apiKey);

    /**
     * Creates a new {@link JwtCredentials} instance based on provided Json Web Token
     *
     * @param jwt
     * @param idToken the OpenID Connect idToken, used for the logout
     * @return
     */
    JwtCredentials newJwtCredentials(String jwt, String idToken);

    /**
     * Creates a new {@link AccessTokenCredentials} instance based on provided tokenId
     *
     * @param tokenId
     * @return
     */
    AccessTokenCredentials newAccessTokenCredentials(String tokenId);

    /**
     * Creates a new {@link RefreshTokenCredentials} instance based on provided tokenId and refresh token
     *
     * @param tokenId
     * @return
     */
    RefreshTokenCredentials newRefreshTokenCredentials(String tokenId, String refreshToken);

}
