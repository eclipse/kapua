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
package org.eclipse.kapua.plugin.sso.openid.provider;

import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.OpenIDLocator;
import org.eclipse.kapua.plugin.sso.openid.OpenIDService;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;

import javax.inject.Inject;

/**
 * The OpenID Connect service provider locator.
 */
public class OpenIDLocatorImpl implements OpenIDLocator {

    private final OpenIDService openIDService;
    private final JwtProcessor jwtProcessor;

    @Inject
    public OpenIDLocatorImpl(OpenIDService openIDService, JwtProcessor jwtProcessor) {
        this.openIDService = openIDService;
        this.jwtProcessor = jwtProcessor;
    }

    @Override
    public OpenIDService getService() {
        return openIDService;
    }

    @Override
    public JwtProcessor getProcessor() throws OpenIDException {
        return jwtProcessor;
    }
}
