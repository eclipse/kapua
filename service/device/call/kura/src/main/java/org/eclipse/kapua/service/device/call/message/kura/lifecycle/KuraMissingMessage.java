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
 * Kura device missing message implementation.
 * <p>
 * The missing message is sent by the platform to notify that a device is no more available (likely due to network error).
 * </p>
 */
public class KuraMissingMessage extends KuraMessage<KuraMissingChannel, KuraMissingPayload> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public KuraMissingMessage() {
        super();
    }

    /**
     * Constructor
     *
     * @param channel
     * @param timestamp
     * @param payload
     */
    public KuraMissingMessage(KuraMissingChannel channel,
            Date timestamp,
            KuraMissingPayload payload) {
        this.channel = channel;
        this.timestamp = timestamp;
        this.payload = payload;
    }

}
