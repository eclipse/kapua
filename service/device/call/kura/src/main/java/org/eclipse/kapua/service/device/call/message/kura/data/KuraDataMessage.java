/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
 *
 * @since 1.0.0
 */
public class KuraDataMessage extends KuraMessage<KuraDataChannel, KuraDataPayload> implements DeviceDataMessage<KuraDataChannel, KuraDataPayload> {

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public KuraDataMessage() {
        super();
    }

    /**
     * Constructor.
     *
     * @param channel   The {@link KuraDataChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link KuraDataPayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 1.0.0
     */
    public KuraDataMessage(KuraDataChannel channel, Date timestamp, KuraDataPayload payload) {
        this();
        this.channel = channel;
        this.timestamp = timestamp;
        this.payload = payload;
    }

}
