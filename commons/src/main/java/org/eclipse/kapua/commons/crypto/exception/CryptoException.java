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
package org.eclipse.kapua.commons.crypto.exception;

import org.eclipse.kapua.KapuaException;

import javax.validation.constraints.NotNull;

/**
 * {@link CryptoException} definition.
 * <p>
 * Base class for {@code kapua-commons} {@link KapuaException}s of {@code org.eclipse.kapua.commons.crypto} package.
 *
 * @since 2.0.0
 */
public abstract class CryptoException extends KapuaException {

    private static final String CRYPTO_ERROR_MESSAGES = "crypto-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link CryptoErrorCodes} associated with the {@link CryptoException}
     * @since 2.0.0
     */
    protected CryptoException(@NotNull CryptoErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link CryptoErrorCodes} associated with the {@link CryptoException}
     * @param arguments The arguments associated with the {@link CryptoException}.
     * @since 2.0.0
     */
    protected CryptoException(@NotNull CryptoErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link CryptoErrorCodes} associated with the {@link CryptoException}.
     * @param cause     The original cause associated with the {@link CryptoException}.
     * @param arguments The arguments associated with the {@link CryptoException}.
     * @since 2.0.0
     */
    protected CryptoException(@NotNull CryptoErrorCodes code, @NotNull Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return CRYPTO_ERROR_MESSAGES;
    }
}
