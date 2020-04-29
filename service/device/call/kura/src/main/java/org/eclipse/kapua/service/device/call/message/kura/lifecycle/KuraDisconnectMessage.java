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

import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleMessage;

import java.util.Date;

/**
 * {@link DeviceLifecycleMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 * <p>
 * The {@link KuraDisconnectMessage} is sent by the {@link org.eclipse.kapua.service.device.registry.Device} to notify to the platform that it is no more available.
 *
 * @since 1.0.0
 */
public class KuraDisconnectMessage extends AbstractKuraLifecycleMessage<KuraDisconnectChannel, KuraDisconnectPayload> implements DeviceLifecycleMessage<KuraDisconnectChannel, KuraDisconnectPayload> {

    /**
     * Constructor.
     *
     * @param channel   The {@link KuraDisconnectChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link KuraDisconnectPayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 1.0.0
     */
    public KuraDisconnectMessage(KuraDisconnectChannel channel, Date timestamp, KuraDisconnectPayload payload) {
        super(channel, timestamp, payload);
    }

}
