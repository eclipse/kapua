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
package org.eclipse.kapua.commons.configuration.exception;

import org.eclipse.kapua.KapuaException;

/**
 * Configuration exception.
 *
 * @since 1.0
 */
public class KapuaConfigurationException extends KapuaException {

    private static final long serialVersionUID = -5191015333552703367L;

    private static final String KAPUA_ERROR_MESSAGES = "kapua-configuration-service-error-messages";

    /**
     * Builds a new KapuaException instance based on the supplied KapuaErrorCode.
     *
     * @param code
     */
    public KapuaConfigurationException(KapuaConfigurationErrorCodes code) {
        super(code);
    }

    /**
     * Builds a new KapuaException instance based on the supplied KapuaErrorCode
     * and optional arguments for the associated exception message.
     *
     * @param code
     * @param arguments
     */
    public KapuaConfigurationException(KapuaConfigurationErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Builds a new KapuaAccountException instance based on the supplied KapuaAccountErrorCode,
     * an Throwable cause, and optional arguments for the associated exception message.
     *
     * @param code
     * @param cause
     * @param arguments
     */
    public KapuaConfigurationException(KapuaConfigurationErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    /**
     * Factory method to build an KapuaAccountException with the KapuaAccountErrorCode.INTERNAL_ERROR,
     * and optional arguments for the associated exception message.
     *
     * @param message
     * @return
     */
    public static KapuaConfigurationException internalError(String message) {
        return new KapuaConfigurationException(KapuaConfigurationErrorCodes.INTERNAL_ERROR, null, message);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
