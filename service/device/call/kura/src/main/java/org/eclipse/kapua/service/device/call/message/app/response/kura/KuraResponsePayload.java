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
package org.eclipse.kapua.service.device.call.message.app.response.kura;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponsePayload;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;

public class KuraResponsePayload extends KuraPayload implements DeviceResponsePayload
{
    @SuppressWarnings("unchecked")
    @Override
    public KuraResponseCode getResponseCode()
    {
        return KuraResponseCode.valueOf((String) getMetrics().get("response.code"));
    }

    @Override
    public String getExceptionMessage()
    {
        return (String) getMetrics().get("response.exception.message");
    }

    @Override
    public String getExceptionStack()
    {
        return (String) getMetrics().get("response.exception.stack");
    }

    @Override
    public byte[] getResponseBody()
    {
        return getBody();
    }

    @Override
    public byte[] toByteArray()
    {
        return super.toByteArray();
    }

    @Override
    public void readFromByteArray(byte[] rawPayload)
        throws KapuaException
    {
        super.readFromByteArray(rawPayload);
    }
}
