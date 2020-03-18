/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.kura.others;

import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;

import java.util.Date;

/**
 * {@link DeviceMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 * <p>
 * The unmatched messages are messages that aren't processed from Kapua.
 * This happens when a {@link KuraMessage} does not match any of the listeners.
 *
 * @since 1.0.0
 */
public class KuraUnmatchedMessage extends KuraMessage<KuraUnmatchedChannel, KuraUnmatchedPayload> implements DeviceMessage<KuraUnmatchedChannel, KuraUnmatchedPayload> {

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public KuraUnmatchedMessage() {
        super();
    }

    /**
     * Constructor.
     *
     * @param channel   The {@link KuraUnmatchedChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link KuraUnmatchedPayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 1.0.0
     */
    public KuraUnmatchedMessage(KuraUnmatchedChannel channel, Date timestamp, KuraUnmatchedPayload payload) {
        super(channel, timestamp, payload);
    }

}
