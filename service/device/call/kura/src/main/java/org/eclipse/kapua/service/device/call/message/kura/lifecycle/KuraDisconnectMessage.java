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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import java.util.Date;

import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;

/**
 * Kura device disconnect message implementation.<br>
 * The disconnect message is sent by the device to notify to the platform that it is no more available.
 *
 * @since 1.0
 *
 */
public class KuraDisconnectMessage extends KuraMessage<KuraDisconnectChannel, KuraDisconnectPayload> {

    /**
     * Constructor
     */
    public KuraDisconnectMessage() {
        super();
    }

    /**
     * Constructor
     *
     * @param channel
     * @param timestamp
     * @param payload
     */
    public KuraDisconnectMessage(KuraDisconnectChannel channel,
            Date timestamp,
            KuraDisconnectPayload payload) {
        this.channel = channel;
        this.timestamp = timestamp;
        this.payload = payload;
    }

}
