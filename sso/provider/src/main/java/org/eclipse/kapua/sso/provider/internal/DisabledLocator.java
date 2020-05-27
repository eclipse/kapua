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
package org.eclipse.kapua.sso.provider.internal;

import org.eclipse.kapua.sso.JwtProcessor;
import org.eclipse.kapua.sso.SingleSignOnService;
import org.eclipse.kapua.sso.provider.SingleSignOnProvider.ProviderLocator;
import org.jose4j.jwt.consumer.JwtContext;

import javax.json.JsonObject;
import java.net.URI;

/**
 * A dummy locator to return when the providerId (on the ProviderSingleSignOnLocator) is null.
 */
public class DisabledLocator implements ProviderLocator {

    public static final ProviderLocator INSTANCE = new DisabledLocator();

    private static final SingleSignOnService SERVICE = new SingleSignOnService() {

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public String getLoginUri(final String state, final URI redirectUri) {
            return null;
        }

        @Override
        public JsonObject getAccessToken(final String authCode, final URI redirectUri) {
            return null;
        }

        @Override
        public String getLogoutUri(String idTokenHint, URI postLogoutRedirectUri, String state) {
            return null;
        }
    };

    /**
     * A dummy JwtProcessor.
     */
    private static final JwtProcessor PROCESSOR = new JwtProcessor() {

        @Override
        public void close() throws Exception {

        }

        @Override
        public boolean validate(String jwt) {
            return false;
        }

        @Override
        public JwtContext process(String jwt) {
            return null;
        }
    };

    private DisabledLocator() {
    }

    @Override
    public SingleSignOnService getService() {
        return SERVICE;
    }

    @Override
    public JwtProcessor getProcessor() {
        return PROCESSOR;
    }

    @Override
    public void close() throws Exception {
    }
}
