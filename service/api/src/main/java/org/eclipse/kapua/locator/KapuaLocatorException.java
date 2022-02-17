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
package org.eclipse.kapua.locator;

import org.eclipse.kapua.KapuaException;

/**
 * Kapua locator exception.<br>
 * The exception is thrown if something wrong happens during service initialization operation done by the locator.
 *
 * @since 1.0
 *
 */
public class KapuaLocatorException extends KapuaException {

    private static final long serialVersionUID = -6207605695086240243L;

    private static final String KAPUA_ERROR_MESSAGES = "kapua-locator-service-error-messages";

    /**
     * Constructor
     *
     * @param code
     */
    public KapuaLocatorException(KapuaLocatorErrorCodes code) {
        super(code);
    }

    /**
     * Constructor
     *
     * @param code
     * @param arguments
     */
    public KapuaLocatorException(KapuaLocatorErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor
     *
     * @param code
     * @param cause
     * @param arguments
     */
    public KapuaLocatorException(KapuaLocatorErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
