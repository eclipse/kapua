/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.provider.generic.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum GenericSsoSettingKeys implements SettingKey {

    SSO_OPENID_SERVER_ENDPOINT_AUTH("sso.generic.openid.server.endpoint.auth"), //
    SSO_OPENID_SERVER_ENDPOINT_TOKEN("sso.generic.openid.server.endpoint.token"), //
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
