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

    private final CredentialsFactory credentialsFactory = KapuaLocator.getInstance().getFactory(CredentialsFactory.class);

    /**
     * Creates a new {@link UsernamePasswordCredentials} instance
     *
     * @return
     */
    public UsernamePasswordCredentials newUsernamePasswordCredentials() {
        return credentialsFactory.newUsernamePasswordCredentials();
    }

    /**
     * Creates a new {@link ApiKeyCredentials} instance
     *
     * @return
     */
    public ApiKeyCredentials newApiKeyCredentials() {
        return credentialsFactory.newApiKeyCredentials(null);
    }

    /**
     * Creates a new {@link JwtCredentials} instance
     *
     * @return
     */
    public JwtCredentials newJwtCredentials() {
        return credentialsFactory.newJwtCredentials(null, null);
    }

    /**
     * Creates a new {@link AccessTokenCredentials} instance
     *
     * @return
     */
    public AccessTokenCredentials newAccessTokenCredentials() {
        return credentialsFactory.newAccessTokenCredentials(null);
    }

    /**
     * Creates a new {@link RefreshTokenCredentials} instance
     *
     * @return
     */
    public RefreshTokenCredentials newRefreshTokenCredentials() {
        return credentialsFactory.newRefreshTokenCredentials(null, null);
    }
}
