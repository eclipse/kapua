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
package org.eclipse.kapua.service.device.call.kura.exception;

import org.eclipse.kapua.KapuaException;

/**
 * Kura-mqtt device call exception.
 * 
 * @since 1.0
 *
 */
public class KuraMqttDeviceCallException extends KapuaException
{
    private static final long   serialVersionUID     = -6207605695086240243L;

    private static final String KAPUA_ERROR_MESSAGES = "device-call-service-error-messages";

    /**
     * Constructor
     * 
     * @param code
     */
    public KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes code)
    {
        super(code);
    }

    /**
     * Constructor
     * 
     * @param code
     * @param arguments
     */
    public KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes code, Object... arguments)
    {
        super(code, arguments);
    }

    /**
     * Constructor
     * 
     * @param code
     * @param cause
     * @param arguments
     */
    public KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes code, Throwable cause, Object... arguments)
    {
        super(code, cause, arguments);
    }

    protected String getKapuaErrorMessagesBundle()
    {
        return KAPUA_ERROR_MESSAGES;
    }
}
