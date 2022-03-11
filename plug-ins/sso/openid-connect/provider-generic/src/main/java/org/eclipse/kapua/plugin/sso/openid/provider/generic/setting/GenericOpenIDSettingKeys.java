/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
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
package org.eclipse.kapua.plugin.sso.openid.provider.generic.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum GenericOpenIDSettingKeys implements SettingKey {

    SSO_OPENID_JWT_AUDIENCE_ALLOWED("sso.openid.generic.jwt.audience.allowed"),
    SSO_OPENID_JWT_ISSUER_ALLOWED("sso.openid.generic.jwt.issuer.allowed"),

    SSO_OPENID_SERVER_ENDPOINT_AUTH("sso.openid.generic.server.endpoint.auth"),
    SSO_OPENID_SERVER_ENDPOINT_LOGOUT("sso.openid.generic.server.endpoint.logout"),
    SSO_OPENID_SERVER_ENDPOINT_TOKEN("sso.openid.generic.server.endpoint.token"),
    SSO_OPENID_SERVER_ENDPOINT_USERINFO("sso.openid.generic.server.endpoint.userinfo"),
    ;

    private final String key;

    GenericOpenIDSettingKeys(final String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
