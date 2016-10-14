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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.commons.exception;

import org.eclipse.kapua.KapuaException;

public class DeviceManagementException extends KapuaException
{
    private static final long   serialVersionUID     = -6207605695086240243L;

    private static final String KAPUA_ERROR_MESSAGES = "device-management-service-error-messages";

    public DeviceManagementException(DeviceManagementErrorCodes code)
    {
        super(code);
    }

    public DeviceManagementException(DeviceManagementErrorCodes code, Object... arguments)
    {
        super(code, arguments);
    }

    public DeviceManagementException(DeviceManagementErrorCodes code, Throwable cause, Object... arguments)
    {
        super(code, cause, arguments);
    }

    protected String getKapuaErrorMessagesBundle()
    {
        return KAPUA_ERROR_MESSAGES;
    }
}
