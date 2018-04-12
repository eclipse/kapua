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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleMessage;

import java.util.Date;

/**
 * {@link DeviceLifecycleMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 * <p>
 * The application message is sent by the {@link org.eclipse.kapua.service.device.registry.Device} to update the platform knowledge about its available features.
 * </p>
 */
public class KuraAppsMessage extends AbstractKuraAppsBirthMessage<KuraAppsChannel, KuraAppsPayload> implements DeviceLifecycleMessage<KuraAppsChannel, KuraAppsPayload> {

    /**
     * Constructor
     *
     * @param channel
     * @param timestamp
     * @param payload
     */
    public KuraAppsMessage(KuraAppsChannel channel, Date timestamp, KuraAppsPayload payload) {
        super(channel, timestamp, payload);
    }

}
