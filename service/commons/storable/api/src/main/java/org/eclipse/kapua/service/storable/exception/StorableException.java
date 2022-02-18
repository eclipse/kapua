/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.storable.exception;

import org.eclipse.kapua.KapuaException;

/**
 * {@link StorableException} definition.
 * <p>
 * This is the base {@link KapuaException} for all detailed {@link Exception} of this module.
 *
 * @since 1.3.0
 */
public class StorableException extends KapuaException {

    private static final String ELASTICSEARCH_CLIENT_ERROR_MESSAGES = "storable-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link StorableErrorCodes}.
     * @since 1.3.0
     */
    public StorableException(StorableErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link StorableErrorCodes}.
     * @param arguments Additional argument associated with the {@link StorableException}.
     * @since 1.3.0
     */
    public StorableException(StorableErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link StorableErrorCodes}.
     * @param cause     The root {@link Throwable} of this {@link StorableException}.
     * @param arguments Additional argument associated with the {@link StorableException}.
     * @since 1.3.0
     */
    public StorableException(StorableErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }


    /**
     * Constructor
     *
     * @param code  The {@link StorableErrorCodes}.
     * @param cause The root {@link Throwable} of this {@link StorableException}.
     * @since 1.3.0
     */
    public StorableException(StorableErrorCodes code, Throwable cause) {
        super(code, cause);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return ELASTICSEARCH_CLIENT_ERROR_MESSAGES;
    }
}
