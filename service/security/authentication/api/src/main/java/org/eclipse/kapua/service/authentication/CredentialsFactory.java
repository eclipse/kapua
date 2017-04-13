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
 *******************************************************************************/
package org.eclipse.kapua.service.authentication;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link CredentialsFactory} factory definition.
 * 
 * @since 1.0
 * 
 */
public interface CredentialsFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link UsernamePasswordCredentials} instance based on provided username and password
     * 
     * @param username
     * @param password
     * @return
     * 
     * @since 1.0
     */
    public UsernamePasswordCredentials newUsernamePasswordCredentials(String username, char[] password);

    /**
     * Creates a new {@link ApiKeyCredentials} instance based on provided api key
     * 
     * @param apiKey
     * @return
     * 
     * @since 1.0
     */
    public ApiKeyCredentials newApiKeyCredentials(String apiKey);

    /**
     * Creates a new {@link JwtCredentials} instance based on provided Json Web Token
     * 
     * @param jwt
     * @return
     * 
     * @since 1.0
     */
    public JwtCredentials newJwtCredentials(String jwt);

    /**
     * Creates a new {@link AccessTokenCredentials} instance based on provided tokenId
     * 
     * @param tokenId
     * @return
     * 
     * @since 1.0
     */
    public AccessTokenCredentials newAccessTokenCredentials(String tokenId);
    
    /**
     * Creates a new {@link RefreshTokenCredentials} instance based on provided tokenId and refresh token
     * 
     * @param tokenId
     * @return
     * 
     * @since 1.0
     */
    public RefreshTokenCredentials newRefreshTokenCredentials(String tokenId, String refreshToken);

}
