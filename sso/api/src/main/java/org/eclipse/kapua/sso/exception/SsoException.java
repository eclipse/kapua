/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.exception;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.KapuaException;

public abstract class SsoException extends KapuaException {

    private static final String KAPUA_ERROR_MESSAGES = "sso-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link KapuaErrorCode} associated with the {@link Exception}
     * @since 1.0.0
     */
    public SsoException(KapuaErrorCode code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaErrorCode} associated with the {@link Exception}.
     * @param arguments The arguments associated with the {@link Exception}.
     * @since 1.0.0
     */
    public SsoException(KapuaErrorCode code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Builds a new KapuaException instance based on the supplied KapuaErrorCode,
     * an Throwable cause, and optional arguments for the associated exception message.
     *
     * @param code      The {@link KapuaErrorCode} associated with the {@link Exception}.
     * @param cause     The original {@link Throwable}.
     * @param arguments The arguments associated with the {@link Exception}.
     * @since 1.0.0
     */
    public SsoException(KapuaErrorCode code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
