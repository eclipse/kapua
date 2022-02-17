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
package org.eclipse.kapua.translator.exception;

import org.eclipse.kapua.KapuaRuntimeException;

/**
 * {@link org.eclipse.kapua.translator.Translator} {@link KapuaRuntimeException}.
 *
 * @since 1.2.0
 */
public class TranslatorRuntimeException extends KapuaRuntimeException {

    private static final String TRANSLATOR_ERROR_MESSAGES = "translator-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link TranslatorErrorCodes}.
     * @since 1.2.0
     */
    public TranslatorRuntimeException(TranslatorErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link TranslatorErrorCodes}.
     * @param arguments Additional argument associated with the {@link TranslatorRuntimeException}.
     * @since 1.2.0
     */
    public TranslatorRuntimeException(TranslatorErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link TranslatorErrorCodes}.
     * @param cause     The root {@link Throwable} of this {@link TranslatorRuntimeException}.
     * @param arguments Additional argument associated with the {@link TranslatorRuntimeException}.
     * @since 1.2.0
     */
    public TranslatorRuntimeException(TranslatorErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return TRANSLATOR_ERROR_MESSAGES;
    }
}
