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
package org.eclipse.kapua.translator.exception;

import org.eclipse.kapua.KapuaException;

/**
 * {@link org.eclipse.kapua.translator.Translator} {@link KapuaException}.
 *
 * @since 1.0.0
 */
public class TranslatorException extends KapuaException {

    private static final long serialVersionUID = -6207605695086240243L;

    private static final String TRANSLATOR_ERROR_MESSAGES = "translator-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link TranslatorErrorCodes}.
     * @since 1.0.0
     */
    public TranslatorException(TranslatorErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link TranslatorErrorCodes}.
     * @param arguments Additional argument associated with the {@link TranslatorException}.
     * @since 1.0.0
     */
    public TranslatorException(TranslatorErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link TranslatorErrorCodes}.
     * @param cause     The root {@link Throwable} of this {@link TranslatorException}.
     * @param arguments Additional argument associated with the {@link TranslatorException}.
     * @since 1.0.0
     */
    public TranslatorException(TranslatorErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return TRANSLATOR_ERROR_MESSAGES;
    }
}
