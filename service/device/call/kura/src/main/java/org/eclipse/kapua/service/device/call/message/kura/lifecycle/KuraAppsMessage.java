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

import java.util.Date;

import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;

/**
 * Kura device application message implementation.
 * <p>
 * The application message is sent by the device to update the platform knowledge about its available features.
 * </p>
 */
public class KuraAppsMessage extends KuraMessage<KuraAppsChannel, KuraAppsPayload> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public KuraAppsMessage() {
        super();
    }

    /**
     * Constructor
     *
     * @param channel
     * @param timestamp
     * @param payload
     */
    public KuraAppsMessage(KuraAppsChannel channel,
            Date timestamp,
            KuraAppsPayload payload) {
        this.channel = channel;
        this.timestamp = timestamp;
        this.payload = payload;
    }

}
