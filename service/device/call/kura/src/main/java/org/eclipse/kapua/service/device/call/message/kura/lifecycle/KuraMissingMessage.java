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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleMessage;

import java.util.Date;

/**
 * {@link DeviceLifecycleMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 * <p>
 * The missing message is sent by the platform to notify that a device is no more available (likely due to network error).
 *
 * @since 1.0.0
 */
public class KuraMissingMessage extends KuraMessage<KuraMissingChannel, KuraMissingPayload> implements DeviceLifecycleMessage<KuraMissingChannel, KuraMissingPayload> {

    /**
     * Constructor.
     *
     * @param channel   The {@link KuraMissingChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link KuraMissingPayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 1.0.0
     */
    public KuraMissingMessage(KuraMissingChannel channel, Date timestamp, KuraMissingPayload payload) {
        super(channel, timestamp, payload);
    }

}
