/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.provider.internal;

import java.net.URI;

import javax.json.JsonObject;

import org.eclipse.kapua.sso.SingleSignOnService;
import org.eclipse.kapua.sso.provider.SingleSignOnProvider.ProviderLocator;

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
    };

    private DisabledLocator() {
    }

    @Override
    public SingleSignOnService getService() {
        return SERVICE;
    }

    @Override
    public void close() throws Exception {
    }
}
