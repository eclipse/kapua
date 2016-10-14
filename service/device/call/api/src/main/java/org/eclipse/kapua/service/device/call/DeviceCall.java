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
package org.eclipse.kapua.service.device.call;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseMessage;

@SuppressWarnings("rawtypes")
public interface DeviceCall<RQ extends DeviceRequestMessage, RS extends DeviceResponseMessage>
{
    public RS read(RQ requestMessage, Long timeout)
        throws KapuaException;

    public RS create(RQ requestMessage, Long timeout)
        throws KapuaException;

    public RS write(RQ requestMessage, Long timeout)
        throws KapuaException;

    public RS delete(RQ requestMessage, Long timeout)
        throws KapuaException;

    public RS execute(RQ requestMessage, Long timeout)
        throws KapuaException;

    public RS options(RQ requestMessage, Long timeout)
        throws KapuaException;

    public <M extends DeviceMessage> Class<M> getBaseMessageClass();
}
