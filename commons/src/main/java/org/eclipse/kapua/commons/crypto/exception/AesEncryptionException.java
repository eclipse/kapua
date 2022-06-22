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

import org.eclipse.kapua.commons.crypto.CryptoUtil;

/**
 * {@link CryptoException} to {@code throw} when {@link CryptoUtil#encryptAes(String)} errors.
 *
 * @since 2.0.0
 */
public class AesEncryptionException extends CryptoException {

    /**
     * Constructor.
     *
     * @param cause The original cause of the error.
     * @since 2.0.0
     */
    public AesEncryptionException(Throwable cause) {
        super(CryptoErrorCodes.AES_ENCODE_ERROR, cause);
    }
}
