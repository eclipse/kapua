/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link KapuaErrorCode}s for {@link CryptoException}s
 *
 * @since 2.0.0
 */
public enum CryptoErrorCodes implements KapuaErrorCode {

    /**
     * @see AesDecryptionException
     * @since 2.0.0
     */
    AES_DECODE_ERROR,

    /**
     * @see AesEncryptionException
     * @since 2.0.0
     */
    AES_ENCODE_ERROR,

    /**
     * @see AlgorihmNotAvailableRuntimeException
     * @since 2.0.0
     */
    ALGORITHM_NOT_AVAILABLE,

    /**
     * @see DefaultSecretKeyDetectedRuntimeException
     * @since 2.0.0
     */
    DEFAULT_SECRET_KEY_DETECTED,

    /**
     * @see InvalidSecretKeyRuntimeException
     * @since 2.0.0
     */
    INVALID_SECRET_KEY

}
