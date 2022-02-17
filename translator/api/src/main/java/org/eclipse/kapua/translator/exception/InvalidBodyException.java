/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.message.Payload;

/**
 * Base {@link TranslatorException} for all error occurring when translating {@link Payload}' body.
 *
 * @since 1.5.0
 */
public abstract class InvalidBodyException extends TranslatorException {

    private static final long serialVersionUID = 2354951312528327605L;

    /**
     * Constructor.
     *
     * @param code The {@link TranslatorErrorCodes}.
     * @since 1.5.0
     */
    public InvalidBodyException(TranslatorErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link TranslatorErrorCodes}.
     * @param arguments Additional argument associated with the {@link InvalidBodyException}.
     * @since 1.5.0
     */
    public InvalidBodyException(TranslatorErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link TranslatorErrorCodes}.
     * @param cause     The root {@link Throwable} of this {@link InvalidBodyException}.
     * @param arguments Additional argument associated with the {@link InvalidBodyException}.
     * @since 1.5.0
     */
    public InvalidBodyException(TranslatorErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }
}
