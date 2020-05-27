/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.sso.provider.keycloak;

import org.eclipse.kapua.sso.JwtProcessor;
import org.eclipse.kapua.sso.SingleSignOnService;
import org.eclipse.kapua.sso.exception.SsoException;
import org.eclipse.kapua.sso.provider.SingleSignOnProvider.ProviderLocator;
import org.eclipse.kapua.sso.provider.keycloak.jwt.KeycloakJwtProcessor;

/**
 * The Keycloak SingleSignOn service provider locator.
 */
public class KeycloakSingleSignOnLocator implements ProviderLocator {

    private static JwtProcessor jwtProcessorInstance;
    private static SingleSignOnService ssoServiceInstance;

    @Override
    public SingleSignOnService getService() {
        if (ssoServiceInstance == null) {
            synchronized (KeycloakSingleSignOnLocator.class) {
                if (ssoServiceInstance == null) {
                    ssoServiceInstance = new KeycloakSingleSignOnService();
                }
            }
        }
        return ssoServiceInstance;
    }

    @Override
    public JwtProcessor getProcessor() throws SsoException {
        if (jwtProcessorInstance == null) {
            synchronized (KeycloakSingleSignOnLocator.class) {
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
