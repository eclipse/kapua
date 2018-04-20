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
package org.eclipse.kapua.service.device.call.message.kura.app.request;

import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppMessage;

import java.util.Date;

/**
 * {@link DeviceRequestMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 */
public class KuraRequestMessage extends KuraAppMessage<KuraRequestChannel, KuraRequestPayload> implements DeviceRequestMessage<KuraRequestChannel, KuraRequestPayload> {

    /**
     * Constructor
     */
    public KuraRequestMessage() {
        super();
    }

    /**
     * Constructor
     *
     * @param channel
     * @param timestamp
     * @param payload
     */
    public KuraRequestMessage(KuraRequestChannel channel, Date timestamp, KuraRequestPayload payload) {
        super(channel, timestamp, payload);
    }

}
