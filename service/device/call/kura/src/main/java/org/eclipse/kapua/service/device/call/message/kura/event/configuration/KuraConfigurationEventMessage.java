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
package org.eclipse.kapua.service.device.call.message.kura.event.configuration;

import org.eclipse.kapua.service.device.call.kura.Kura;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfiguration;
import org.eclipse.kapua.service.device.call.message.app.event.DeviceManagementEventMessage;
import org.eclipse.kapua.service.device.call.message.app.notification.DeviceNotifyMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;
import org.eclipse.kapua.service.device.registry.Device;

import java.util.Date;

/**
 * {@link KuraDeviceConfiguration} {@link DeviceNotifyMessage} {@link Kura} implementation.
 * <p>
 * The {@link KuraConfigurationEventMessage} is sent by the {@link Device} to notify the platform about a {@link KuraDeviceConfiguration} update.
 *
 * @since 2.0.0
 */
public class KuraConfigurationEventMessage extends KuraMessage<KuraConfigurationEventChannel, KuraConfigurationEventPayload> implements DeviceManagementEventMessage<KuraConfigurationEventChannel, KuraConfigurationEventPayload> {

    /**
     * Constructor.
     *
     * @param channel   The {@link KuraConfigurationEventChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link KuraConfigurationEventPayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 2.0.0
     */
    public KuraConfigurationEventMessage(KuraConfigurationEventChannel channel, Date timestamp, KuraConfigurationEventPayload payload) {
        super(channel, timestamp, payload);
    }

}
