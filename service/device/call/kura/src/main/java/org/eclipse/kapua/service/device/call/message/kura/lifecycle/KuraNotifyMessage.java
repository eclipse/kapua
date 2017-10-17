/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;

import java.util.Date;

/**
 * Kura device notify message implementation.
 * <p>
 * The missing message is sent by the device to notify the platform about a task progress.
 * </p>
 */
public class KuraNotifyMessage extends KuraMessage<KuraNotifyChannel, KuraNotifyPayload> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public KuraNotifyMessage() {
        super();
    }

    /**
     * Constructor
     *
     * @param channel
     * @param timestamp
     * @param payload
     */
    public KuraNotifyMessage(KuraNotifyChannel channel, Date timestamp, KuraNotifyPayload payload) {
        this.channel = channel;
        this.timestamp = timestamp;
        this.payload = payload;
    }

}
