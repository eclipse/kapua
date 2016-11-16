/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Authentication setting key
 * 
 * @since 1.0
 *
 */
public enum KapuaAuthenticationSettingKeys implements SettingKey {
    AUTHENTICATION_KEY("authentication.key"), //

    AUTHENTICATION_TOKEN_EXPIRE_AFTER("authentication.token.expire.after"),//

    AUTHENTICATION_APIKEY_PRE_LENGTH("authentication.apiKey.pre.length"),//
    AUTHENTICATION_APIKEY_PRE_SEPARATOR("authentication.apiKey.pre.separator"),//

    AUTHENTICATION_APIKEY_KEY_LENGTH("authentication.apiKey.key.length"), //
    ;

    private String key;

    private KapuaAuthenticationSettingKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
