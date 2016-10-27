/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura;

import org.eclipse.kapua.service.device.call.DeviceCallback;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;

/**
 * Kura device callback implementation.
 * 
 * @since 1.0
 *
 */
public class KuraDeviceCallback implements DeviceCallback<KuraResponseMessage>
{
    private KuraDeviceResponseContainer responseContainer;

    /**
     * Constructor
     * 
     * @param responseContainer
     */
    public KuraDeviceCallback(KuraDeviceResponseContainer responseContainer)
    {
        this.responseContainer = responseContainer;
    }

    @Override
    public void responseReceived(KuraResponseMessage response)
    {
        responseContainer.add(response);
    }

    @Override
    public void timedOut()
    {
        notifyAll();
    }
}
