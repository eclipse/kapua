/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
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

    CIPHER_KEY("cipher.key"),
    ;

    private String key;

    private KapuaCryptoSettingKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
