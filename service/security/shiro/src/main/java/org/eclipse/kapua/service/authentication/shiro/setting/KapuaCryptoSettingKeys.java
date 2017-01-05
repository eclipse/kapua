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
 * Crypto setting key
 * 
 * @since 1.0
 *
 */
public enum KapuaCryptoSettingKeys implements SettingKey {
    CRYPTO_KEY("crypto.key"), //

    CRYPTO_BCRYPT_LOG_ROUNDS("crypto.bCrypt.logRounds"),

    CRYPTO_SHA_ALGORITHM("crypto.sha.algorithm"),

    CRYPTO_SHA_SALT_LENGTH("crypto.sha.salt.length"),
    ;

    private String key;

    private KapuaCryptoSettingKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
