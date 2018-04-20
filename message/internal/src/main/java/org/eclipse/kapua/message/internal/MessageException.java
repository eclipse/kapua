/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.KapuaException;

/**
 * Message exception.
 * 
 * @since 1.0
 *
 */
public class MessageException extends KapuaException {

    private static final long serialVersionUID = -6207605695086240243L;

    private static final String KAPUA_ERROR_MESSAGES = "message-error-messages";

    /**
     * Constructor
     * 
     * @param code
     */
    public MessageException(MessageErrorCodes code) {
        super(code);
    }

    /**
     * Constructor
     * 
     * @param code
     * @param arguments
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
     */
    public MessageException(MessageErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
