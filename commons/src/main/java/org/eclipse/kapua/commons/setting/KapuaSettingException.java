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
package org.eclipse.kapua.commons.setting;

import org.eclipse.kapua.KapuaException;

/**
 * Setting exception.
 *
 * @since 1.0.0
 *
 */
public class KapuaSettingException extends KapuaException {

    private static final long serialVersionUID = -6207605695086240243L;

    private static final String KAPUA_ERROR_MESSAGES = "kapua-setting-service-error-messages";

    /**
     * Constructs the exception by error code
     *
     * @param code
     *            error code
     */
    public KapuaSettingException(KapuaSettingErrorCodes code) {
        super(code);
    }

    /**
     * Constructs the exception by error code and custom arguments
     *
     * @param code
     *            error code
     * @param arguments
     *            arguments
     */
    public KapuaSettingException(KapuaSettingErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructs the exception by error code, custom arguments and cause
     *
     * @param code
     *            error code
     * @param cause
     *            original cause
     * @param arguments
     *            arguments
     */
    public KapuaSettingException(KapuaSettingErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
