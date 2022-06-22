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
package org.eclipse.kapua.commons.crypto.setting;

import org.eclipse.kapua.commons.crypto.CryptoUtil;
import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * {@link SettingKey}s for {@link CryptoSettings}.
 *
 * @since 2.0.0
 */
public enum CryptoSettingKeys implements SettingKey {

    /**
     * The secret key for encryption/decryption operations.
     *
     * @since 2.0.0
     */
    CRYPTO_SECRET_KEY("crypto.secret.key"),

    /**
     * This is the old {@link SettingKey} which was used in org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils class
     * to encrypt and decrypt MfaOption.secretKey attribute.
     *
     * This has been ported here to be able to deprecate that class and use the {@link CryptoUtil} class.
     *
     * @since 2.0.0
     * @deprecated Since 2.0.0. This is kept for backward compatibility. Please start make use of {@link #CRYPTO_SECRET_KEY}
     */
    @Deprecated
    CIPHER_KEY("cipher.key"),

    /**
     * Whether to enforce the changing of the {@link #CRYPTO_SECRET_KEY}.
     *
     * @since 2.0.0
     */
    CRYPTO_SECRET_ENFORCE_CHANGE("crypto.secret.enforce.change"),
    ;


    /**
     * The key value of the {@link SettingKey}.
     *
     * @since 2.0.0
     */
    private final String key;

    /**
     * Constructor.
     *
     * @param key The key value of the {@link SettingKey}.
     * @since 2.0.0
     */
    CryptoSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
