/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others
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
package org.eclipse.kapua.sso.provider.generic.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum GenericSsoSettingKeys implements SettingKey {

    SSO_OPENID_SERVER_ENDPOINT_AUTH("sso.generic.openid.server.endpoint.auth"), //
    SSO_OPENID_SERVER_ENDPOINT_TOKEN("sso.generic.openid.server.endpoint.token"), //
    SSO_OPENID_SERVER_ENDPOINT_LOGOUT("sso.generic.openid.server.endpoint.logout"),
    SSO_OPENID_JWT_AUDIENCE_ALLOWED("sso.generic.openid.jwt.audience.allowed"),
    SSO_OPENID_JWT_ISSUER_ALLOWED("sso.generic.openid.jwt.issuer.allowed"),
    ;

    private final String key;

    GenericSsoSettingKeys(final String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
