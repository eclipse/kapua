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
package org.eclipse.kapua.service.device.call.message.app.request.kura;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.kura.app.RequestMetrics;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestPayload;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;

public class KuraRequestPayload extends KuraPayload implements DeviceRequestPayload
{
    @Override
    public void setRequestId(String requestId)
    {
        getMetrics().put(RequestMetrics.REQ_METRIC_REQUEST_ID.getValue(), requestId);
    }

    @Override
    public String getRequestId()
    {
        return (String) getMetrics().get(RequestMetrics.REQ_METRIC_REQUEST_ID.getValue());
    }

    @Override
    public void setRequesterClientId(String requesterClientId)
    {
        getMetrics().put(RequestMetrics.REQ_METRIC_REQUESTER_CLIENT_ID.getValue(), requesterClientId);
    }

    @Override
    public String getRequesterClientId()
    {
        return (String) getMetrics().get(RequestMetrics.REQ_METRIC_REQUESTER_CLIENT_ID.getValue());
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
