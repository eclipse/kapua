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
package org.eclipse.kapua.service.account.internal.exception;

import org.eclipse.kapua.KapuaException;

/**
 * {@code kapua-account internal} {@link KapuaException} definition.
 *
 * @since 1.0.0
 */
public class KapuaAccountException extends KapuaException {

    private static final long serialVersionUID = 6422745329878392484L;

    private static final String ACCOUNT_SERVICE_ERROR_MESSAGES_RESOURCE = "kapua-account-service-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link KapuaAccountErrorCodes} associated with the {@link KapuaAccountException}
     * @since 1.0.0
     */
    public KapuaAccountException(KapuaAccountErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaAccountErrorCodes} associated with the {@link KapuaAccountException}.
     * @param arguments The arguments associated with the {@link KapuaAccountException}.
     * @since 1.0.0
     */
    public KapuaAccountException(KapuaAccountErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaAccountErrorCodes} associated with the {@link KapuaAccountException}.
     * @param cause     The original {@link Throwable}.
     * @param arguments The arguments associated with the {@link KapuaAccountException}.
     * @since 1.0.0
     */
    public KapuaAccountException(KapuaAccountErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return ACCOUNT_SERVICE_ERROR_MESSAGES_RESOURCE;
    }
}
