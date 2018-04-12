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
package org.eclipse.kapua.service.device.call.message.kura.others;

import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;

import java.util.Date;

/**
 * {@link DeviceMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 * <p>
 * The unmatched messages aren't elaborated by the system.
 * In that category fall all the messages that aren't categorized in the others life cycle message groups.
 * </p>
 */
public class KuraUnmatchedMessage extends KuraMessage<KuraUnmatchedChannel, KuraUnmatchedPayload> implements DeviceMessage<KuraUnmatchedChannel, KuraUnmatchedPayload> {

    /**
     * Constructor
     */
    public KuraUnmatchedMessage() {
        super();
    }

    /**
     * Constructor
     *
     * @param channel
     * @param timestamp
     * @param payload
     */
    public KuraUnmatchedMessage(KuraUnmatchedChannel channel, Date timestamp, KuraUnmatchedPayload payload) {
        super(channel, timestamp, payload);
    }

}
