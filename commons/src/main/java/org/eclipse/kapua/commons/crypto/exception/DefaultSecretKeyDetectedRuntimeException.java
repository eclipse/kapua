/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.crypto.exception;

import org.eclipse.kapua.commons.crypto.setting.CryptoSettingKeys;

/**
 * {@link CryptoRuntimeException} to {@code throw} when the {@link CryptoSettingKeys#CRYPTO_SECRET_KEY} has not been changed.
 *
 * @since 2.0.0
 */
public class DefaultSecretKeyDetectedRuntimeException extends CryptoRuntimeException {

    private final String defaultSecretKey;

    /**
     * Constructor.
     *
     * @param defaultSecretKey The default secret key value that needs to be changed.
     * @since 2.0.0
     */
    public DefaultSecretKeyDetectedRuntimeException(String defaultSecretKey) {
        super(CryptoErrorCodes.DEFAULT_SECRET_KEY_DETECTED, defaultSecretKey);

        this.defaultSecretKey = defaultSecretKey;
    }

    /**
     * Gets the default secret key value that needs to be changed.
     *
     * @return The default secret key value that needs to be changed.
     * @since 2.0.0
     */
    public String getDefaultSecretKey() {
        return defaultSecretKey;
    }
}
