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

import org.eclipse.kapua.locator.KapuaLocator;

public class AuthenticationXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final CredentialsFactory CREDENTIALS_FACTORY = LOCATOR.getFactory(CredentialsFactory.class);

    /**
     * Creates a new {@link UsernamePasswordCredentials} instance
     *
     * @return
     */
    public UsernamePasswordCredentials newUsernamePasswordCredentials() {
        return CREDENTIALS_FACTORY.newUsernamePasswordCredentials();
    }

    /**
     * Creates a new {@link ApiKeyCredentials} instance
     *
     * @return
     */
    public ApiKeyCredentials newApiKeyCredentials() {
        return CREDENTIALS_FACTORY.newApiKeyCredentials(null);
    }

    /**
     * Creates a new {@link JwtCredentials} instance
     *
     * @return
     */
    public JwtCredentials newJwtCredentials() {
        return CREDENTIALS_FACTORY.newJwtCredentials(null, null);
    }

    /**
     * Creates a new {@link AccessTokenCredentials} instance
     *
     * @return
     */
    public AccessTokenCredentials newAccessTokenCredentials() {
        return CREDENTIALS_FACTORY.newAccessTokenCredentials(null);
    }

    /**
     * Creates a new {@link RefreshTokenCredentials} instance
     *
     * @return
     */
    public RefreshTokenCredentials newRefreshTokenCredentials() {
        return CREDENTIALS_FACTORY.newRefreshTokenCredentials(null, null);
    }
}
