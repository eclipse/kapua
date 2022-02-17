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
package org.eclipse.kapua.plugin.sso.openid.provider.generic;

import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.OpenIDService;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.provider.OpenIDProvider.ProviderLocator;
import org.eclipse.kapua.plugin.sso.openid.provider.generic.jwt.GenericJwtProcessor;

/**
 * The generic OpenID Connect service provider locator.
 */
public class GenericOpenIDLocator implements ProviderLocator {

    private static JwtProcessor jwtProcessorInstance;
    private static OpenIDService openidServiceInstance;

    @Override
    public OpenIDService getService() {
        if (openidServiceInstance == null) {
            synchronized (GenericOpenIDLocator.class) {
                if (openidServiceInstance == null) {
                    openidServiceInstance = new GenericOpenIDService();
                }
            }
        }
        return openidServiceInstance;
    }

    @Override
    public JwtProcessor getProcessor() throws OpenIDException {
        if (jwtProcessorInstance == null) {
            synchronized (GenericOpenIDLocator.class) {
                if (jwtProcessorInstance == null) {
                    jwtProcessorInstance = new GenericJwtProcessor();
                }
            }
        }
        return jwtProcessorInstance;
    }

    @Override
    public void close() throws Exception {
    }

}
