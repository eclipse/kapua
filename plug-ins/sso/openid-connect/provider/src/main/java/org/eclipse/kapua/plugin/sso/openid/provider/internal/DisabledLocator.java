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
package org.eclipse.kapua.plugin.sso.openid.provider.internal;

import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.OpenIDService;
import org.eclipse.kapua.plugin.sso.openid.provider.OpenIDProvider.ProviderLocator;
import org.jose4j.jwt.consumer.JwtContext;

import javax.json.JsonObject;
import java.net.URI;

/**
 * A dummy locator to return when the providerId (on the ProviderOpenIDLocator) is null.
 */
public class DisabledLocator implements ProviderLocator {

    public static final ProviderLocator INSTANCE = new DisabledLocator();

    private static final OpenIDService SERVICE = new OpenIDService() {

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public String getLoginUri(final String state, final URI redirectUri) {
            return null;
        }

        @Override
        public String getLogoutUri(String idTokenHint, URI postLogoutRedirectUri, String state) {
            return null;
        }

        @Override
        public JsonObject getTokens(final String authCode, final URI redirectUri) {
            return null;
        }

        @Override
        public JsonObject getUserInfo(String authCode) {
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

        @Override
        public String getExternalIdClaimName() {
            return null;
        }

        @Override
        public String getExternalUsernameClaimName() {
            return null;
        }
    };

    private DisabledLocator() {
    }

    @Override
    public OpenIDService getService() {
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
