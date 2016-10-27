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

import java.util.ArrayList;

import org.eclipse.kapua.service.device.call.DeviceCallback;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;

/**
 * Kura device callback implementation.
 * 
 * @since 1.0
 * 
 */
public class KuraDeviceResponseContainer extends ArrayList<KuraResponseMessage> implements DeviceCallback<KuraResponseMessage>
{
    private static final long serialVersionUID = -6909761350290400843L;

    /**
     * Constructor
     */
    public KuraDeviceResponseContainer()
    {
        super();
    }

    @Override
    public void responseReceived(KuraResponseMessage response)
    {
        synchronized (this) {
            add(response);
            notifyAll();
        }
    }

    @Override
    public void timedOut()
    {
        synchronized (this) {
            notifyAll();
        }
    }
}
