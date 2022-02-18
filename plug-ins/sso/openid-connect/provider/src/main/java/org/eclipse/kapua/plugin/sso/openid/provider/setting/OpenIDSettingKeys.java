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
package org.eclipse.kapua.plugin.sso.openid.provider.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum OpenIDSettingKeys implements SettingKey {
    SSO_OPENID_PROVIDER("sso.openid.provider"), //
    SSO_OPENID_CONF_PATH("sso.openid.conf.path"),
    SSO_OPENID_CLIENT_ID("sso.openid.client.id"), //
    SSO_OPENID_CLIENT_SECRET("sso.openid.client.secret"), //
    SSO_OPENID_JWT_PROCESSOR_TIMEOUT("sso.openid.jwt_processor_timeout"),  // the JwtProcessor expiration time
    ;

    private final String key;

    OpenIDSettingKeys(final String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
