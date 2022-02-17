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
package org.eclipse.kapua.service.device.call.message.kura.app.notification;

import org.eclipse.kapua.service.device.call.message.app.notification.DeviceNotifyMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppMessage;

import java.util.Date;

/**
 * {@link DeviceNotifyMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 * <p>
 * The {@link KuraNotifyMessage} is sent by the {@link org.eclipse.kapua.service.device.registry.Device} to notify the platform about the DeviceManagementOperation progress.
 *
 * @since 1.0.0
 */
public class KuraNotifyMessage extends KuraAppMessage<KuraNotifyChannel, KuraNotifyPayload> implements DeviceNotifyMessage<KuraNotifyChannel, KuraNotifyPayload> {

    /**
     * Constructor.
     *
     * @param channel   The {@link KuraNotifyChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link KuraNotifyPayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 1.0.0
     */
    public KuraNotifyMessage(KuraNotifyChannel channel, Date timestamp, KuraNotifyPayload payload) {
        super(channel, timestamp, payload);
    }

}
