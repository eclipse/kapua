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
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.KapuaException;

/**
 * {@link org.eclipse.kapua.message.Message} exception.
 *
 * @since 1.0.0
 */
public class MessageException extends KapuaException {

    private static final long serialVersionUID = -6207605695086240243L;

    private static final String KAPUA_ERROR_MESSAGES = "message-error-messages";

    /**
     * Constructor
     *
     * @param code
     * @since 1.0.0
     */
    public MessageException(MessageErrorCodes code) {
        super(code);
    }

    /**
     * Constructor
     *
     * @param code
     * @param arguments
     * @since 1.0.0
     */
    public MessageException(MessageErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor
     *
     * @param code
     * @param cause
     * @param arguments
     * @since 1.0.0
     */
    public MessageException(MessageErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
