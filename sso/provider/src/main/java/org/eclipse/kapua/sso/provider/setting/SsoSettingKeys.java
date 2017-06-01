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
package org.eclipse.kapua.sso.provider.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum SsoSettingKeys implements SettingKey {
    SSO_PROVIDER("sso.provider"), //
    SSO_OPENID_REDIRECT_URI("sso.openid.redirect.uri"), //
    
    SSO_OPENID_CLIENT_ID("sso.openid.client.id"), //
    SSO_OPENID_CLIENT_SECRET("sso.openid.client.secret"), //
    ;

    private final String key;

    private SsoSettingKeys(final String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
