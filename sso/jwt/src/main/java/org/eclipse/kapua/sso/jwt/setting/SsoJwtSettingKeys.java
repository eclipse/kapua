/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.jwt.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum SsoJwtSettingKeys implements SettingKey {

    SSO_JWT_OPEN_ID_CONFIG_PATH("sso.jwt.openid.config.path"), //
    ;

    private final String key;

    private SsoJwtSettingKeys(final String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
