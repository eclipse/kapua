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
    SSO_OPENID_CLIENT_ID("sso.generic.openid.client.id"), //
    SSO_OPENID_CLIENT_SECRET("sso.generic.openid.client.secret");

    private final String key;

    private GenericSsoSettingKeys(final String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
