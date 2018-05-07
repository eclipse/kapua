/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
        return CREDENTIALS_FACTORY.newJwtCredentials(null);
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
