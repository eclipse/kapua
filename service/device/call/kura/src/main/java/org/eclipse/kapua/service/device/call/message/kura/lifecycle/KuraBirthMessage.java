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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleMessage;

import java.util.Date;

/**
 * {@link DeviceLifecycleMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 * <p>
 * The {@link KuraBirthMessage} is sent by the {@link org.eclipse.kapua.service.device.registry.Device} to notify to the platform that it is available.
 *
 * @since 1.0.0
 */
public class KuraBirthMessage extends AbstractKuraAppsBirthMessage<KuraBirthChannel, KuraBirthPayload> implements DeviceLifecycleMessage<KuraBirthChannel, KuraBirthPayload> {

    /**
     * Constructor.
     *
     * @param channel   The {@link KuraBirthChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link KuraBirthPayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 1.0.0
     */
    public KuraBirthMessage(KuraBirthChannel channel, Date timestamp, KuraBirthPayload payload) {
        super(channel, timestamp, payload);
    }

}
