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
package org.eclipse.kapua.service.device.call.message.kura.app.response;

import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppMessage;

import java.util.Date;

/**
 * {@link DeviceResponseMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 */
public class KuraResponseMessage extends KuraAppMessage<KuraResponseChannel, KuraResponsePayload> implements DeviceResponseMessage<KuraResponseChannel, KuraResponsePayload> {

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
