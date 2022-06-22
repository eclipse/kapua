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
 * {@link CryptoRuntimeException} to {@code throw} when the {@link CryptoSettingKeys#CRYPTO_SECRET_KEY} is not valid.
 *
 * @since 2.0.0
 */
public class InvalidSecretKeyRuntimeException extends CryptoRuntimeException {

    private final String algorithmName;
    private final String secretKey;

    /**
     * Constructor.
     *
     * @param cause         The original cause of the error.
     * @param algorithmName The configured algorithm name.
     * @param secretKey     The configured secret key.
     * @since 2.0.0
     */
    public InvalidSecretKeyRuntimeException(Throwable cause, String algorithmName, String secretKey) {
        super(CryptoErrorCodes.INVALID_SECRET_KEY, cause, algorithmName, secretKey);

        this.algorithmName = algorithmName;
        this.secretKey = secretKey;
    }

    /**
     * Gets the configured algorithm name.
     *
     * @return The configured algorithm name.
     * @since 2.0.0
     */
    public String getAlgorithmName() {
        return algorithmName;
    }

    /**
     * Gets the configured secret key.
     *
     * @return The configured secret key.
     * @since 2.0.0
     */
    public String getSecretKey() {
        return secretKey;
    }
}
