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
package org.eclipse.kapua.service.authorization.exception;

import org.eclipse.kapua.KapuaException;

/**
 * {@link KapuaException} for `kapua-authorization-api` module.
 *
 * @since 1.0.0
 */
public class KapuaAuthorizationException extends KapuaException {

    private static final long serialVersionUID = -6207605695086240243L;

    private static final String ERROR_MESSAGE_RESOURCE_BUNDLE = "kapua-service-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link KapuaAuthorizationErrorCodes} associated with the {@link KapuaAuthorizationException}.
     * @since 1.0.0
     */
    public KapuaAuthorizationException(KapuaAuthorizationErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaAuthorizationErrorCodes} associated with the {@link KapuaAuthorizationException}.
     * @param arguments The arguments associated with the {@link KapuaAuthorizationException}.
     * @since 1.0.0
     */
    public KapuaAuthorizationException(KapuaAuthorizationErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaAuthorizationErrorCodes} associated with the {@link KapuaAuthorizationException}.
     * @param cause     The original {@link Throwable}.
     * @param arguments The arguments associated with the {@link KapuaAuthorizationException}.
     * @since 1.0.0
     */
    public KapuaAuthorizationException(KapuaAuthorizationErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    public String getKapuaErrorMessagesBundle() {
        return ERROR_MESSAGE_RESOURCE_BUNDLE;
    }
}
