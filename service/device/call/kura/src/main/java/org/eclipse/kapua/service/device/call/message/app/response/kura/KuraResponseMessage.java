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
package org.eclipse.kapua.service.device.call.message.app.response.kura;

import java.util.Date;

import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;

/**
 * Kura command response message.
 */
public class KuraResponseMessage extends KuraMessage<KuraResponseChannel, KuraResponsePayload> implements DeviceResponseMessage<KuraResponseChannel, KuraResponsePayload> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * 
     * @param channel
     * @param timestamp
     * @param payload
     */
    public KuraResponseMessage(KuraResponseChannel channel, Date timestamp, KuraResponsePayload payload) {
        super(channel, timestamp, payload);
    }
}
