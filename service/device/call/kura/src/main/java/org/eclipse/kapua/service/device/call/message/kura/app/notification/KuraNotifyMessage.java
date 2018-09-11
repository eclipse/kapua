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
package org.eclipse.kapua.service.device.call.message.kura.app.notification;

import org.eclipse.kapua.service.device.call.message.app.notification.DeviceNotifyMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppMessage;

import java.util.Date;

/**
 * {@link DeviceNotifyMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 * <p>
 * The missing message is sent by the {@link org.eclipse.kapua.service.device.registry.Device} to notify the platform about a task progress.
 * </p>
 */
public class KuraNotifyMessage extends KuraAppMessage<KuraNotifyChannel, KuraNotifyPayload> implements DeviceNotifyMessage<KuraNotifyChannel, KuraNotifyPayload> {

    /**
     * Constructor
     */
    public KuraNotifyMessage() {
        super();
    }

    /**
     * Constructor
     *
     * @param channel
     * @param timestamp
     * @param payload
     */
    public KuraNotifyMessage(KuraNotifyChannel channel, Date timestamp, KuraNotifyPayload payload) {
        super(channel, timestamp, payload);
    }

}
