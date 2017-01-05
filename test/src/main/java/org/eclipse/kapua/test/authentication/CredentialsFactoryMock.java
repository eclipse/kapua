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

package org.eclipse.kapua.test.authentication;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;

@TestService
@KapuaProvider
public class CredentialsFactoryMock implements CredentialsFactory {

    @Override
    public UsernamePasswordCredentials newUsernamePasswordCredentials(String username, char[] password) {
        return new UsernamePasswordCredentialsMock(username, password);
    }

    @Override
    public JwtCredentials newJwtCredentials(String jwt) {
        return new JwtCredentialsMock(jwt);
    }

    @Override
    public ApiKeyCredentials newApiKeyCredentials(String apiKey) {
        return new ApiKeyCredentialsMock(apiKey);
    }

    @Override
    public AccessTokenCredentials newAccessTokenCredentials(String tokenId) {
        return new AccessTokenCredentialsMock(tokenId);
    }

}
