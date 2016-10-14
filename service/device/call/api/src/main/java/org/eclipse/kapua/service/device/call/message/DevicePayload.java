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
package org.eclipse.kapua.service.device.call.message;

import java.util.Date;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.Payload;

public interface DevicePayload extends Payload
{
    public Date getTimestamp();

    public DevicePosition getPosition();

    public Map<String, Object> getMetrics();

    public byte[] getBody();

    //
    // Encode/Decode stuff
    //
    public byte[] toByteArray();

    public void readFromByteArray(byte[] rawPayload)
        throws KapuaException;
}
