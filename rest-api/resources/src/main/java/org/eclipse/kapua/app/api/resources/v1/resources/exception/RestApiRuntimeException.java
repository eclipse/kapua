/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources.exception;

import org.eclipse.kapua.KapuaRuntimeException;

public class RestApiRuntimeException extends KapuaRuntimeException {

    /**
     * Constructor
     *
     * @param message
     * @param throwable
     */
    private RestApiRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Builds a new {@link RestApiRuntimeException} instance based on the supplied {@link RestApiErrorCodes}.
     *
     * @param code
     */
    public RestApiRuntimeException(RestApiErrorCodes code) {
        this(code, (Object[]) null);
    }

    /**
     * Builds a new {@link RestApiRuntimeException} instance based on the supplied {@link RestApiErrorCodes}
     * and optional arguments for the associated exception message.
     *
     * @param code
     * @param arguments
     */
    public RestApiRuntimeException(RestApiErrorCodes code, Object... arguments) {
        this(code, null, arguments);
    }

    /**
     * Builds a new {@link KapuaRuntimeException} instance based on the supplied {@link RestApiErrorCodes},
     * an Throwable cause, and optional arguments for the associated exception message.
     *
     * @param code
     * @param cause
     * @param arguments
     */
    public RestApiRuntimeException(RestApiErrorCodes code, Throwable cause, Object... arguments) {
        super(cause);
        this.code = code;
        args = arguments;
    }
}
