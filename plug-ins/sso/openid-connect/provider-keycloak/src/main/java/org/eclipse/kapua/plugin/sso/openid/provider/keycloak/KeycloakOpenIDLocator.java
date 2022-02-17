/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.plugin.sso.openid.provider.keycloak;

import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.OpenIDService;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.provider.OpenIDProvider.ProviderLocator;
import org.eclipse.kapua.plugin.sso.openid.provider.keycloak.jwt.KeycloakJwtProcessor;

/**
 * The Keycloak OpenID Connect service provider locator.
 */
public class KeycloakOpenIDLocator implements ProviderLocator {

    private static JwtProcessor jwtProcessorInstance;
    private static OpenIDService openIDServiceInstance;

    @Override
    public OpenIDService getService() {
        if (openIDServiceInstance == null) {
            synchronized (KeycloakOpenIDLocator.class) {
                if (openIDServiceInstance == null) {
                    openIDServiceInstance = new KeycloakOpenIDService();
                }
            }
        }
        return openIDServiceInstance;
    }

    @Override
    public JwtProcessor getProcessor() throws OpenIDException {
        if (jwtProcessorInstance == null) {
            synchronized (KeycloakOpenIDLocator.class) {
                if (jwtProcessorInstance == null) {
                    jwtProcessorInstance = new KeycloakJwtProcessor();
                }
            }
        }
        return jwtProcessorInstance;
    }

    @Override
    public void close() throws Exception {
    }

}
