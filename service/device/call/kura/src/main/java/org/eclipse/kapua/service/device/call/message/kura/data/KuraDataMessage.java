/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.data;

import org.eclipse.kapua.service.device.call.message.data.DeviceDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;

import java.util.Date;

/**
 * {@link DeviceDataMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 */
public class KuraDataMessage extends KuraMessage<KuraDataChannel, KuraDataPayload> implements DeviceDataMessage<KuraDataChannel, KuraDataPayload> {

    /**
     * Constructor
     */
    public KuraDataMessage() {
        super();
    }

    /**
     * Constructor
     *
     * @param channel
     * @param timestamp
     * @param payload
     */
    public KuraDataMessage(KuraDataChannel channel, Date timestamp, KuraDataPayload payload) {
        this();
        this.channel = channel;
        this.timestamp = timestamp;
        this.payload = payload;
    }

}
