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

import org.eclipse.kapua.plugin.sso.openid.OpenIDService;

import javax.json.JsonObject;
import java.net.URI;

public class DisabledOpenIDService implements OpenIDService {

    public static final String DISABLED_ID = "disabled";

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

    @Override
    public String getId() {
        return DISABLED_ID;
    }
}
