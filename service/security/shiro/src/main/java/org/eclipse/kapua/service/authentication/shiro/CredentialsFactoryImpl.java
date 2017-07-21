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
package org.eclipse.kapua.service.authentication.shiro;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.RefreshTokenCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;

/**
 * {@link CredentialsFactory} factory implementation.
 * 
 * @since 1.0
 * 
 */
@KapuaProvider
public class CredentialsFactoryImpl implements CredentialsFactory {

    @Override
    public UsernamePasswordCredentials newUsernamePasswordCredentials(String username, String password) {
        return new UsernamePasswordCredentialsImpl(username, password);
    }

    @Override
    public ApiKeyCredentials newApiKeyCredentials(String apiKey) {
        return new ApiKeyCredentialsImpl(apiKey);
    }

    @Override
    public JwtCredentials newJwtCredentials(String jwt) {
        return new JwtCredentialsImpl(jwt);
    }

    @Override
    public AccessTokenCredentials newAccessTokenCredentials(String tokenId) {
        return new AccessTokenCredentialsImpl(tokenId);
    }

    @Override
    public RefreshTokenCredentials newRefreshTokenCredentials(String tokenId, String refreshToken) {
        return new RefreshTokenCredentialsImpl(tokenId, refreshToken);
    }

}
