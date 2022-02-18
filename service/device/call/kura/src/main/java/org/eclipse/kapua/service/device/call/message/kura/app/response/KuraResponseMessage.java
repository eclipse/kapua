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
package org.eclipse.kapua.service.device.call.message.kura.app.response;

import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppMessage;

import java.util.Date;

/**
 * {@link DeviceResponseMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @since 1.0.0
 */
public class KuraResponseMessage extends KuraAppMessage<KuraResponseChannel, KuraResponsePayload> implements DeviceResponseMessage<KuraResponseChannel, KuraResponsePayload> {

    /**
     * Constructor.
     *
     * @param channel   The {@link KuraResponseChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link KuraResponsePayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 1.0.0
     */
    public KuraResponseMessage(KuraResponseChannel channel, Date timestamp, KuraResponsePayload payload) {
        super(channel, timestamp, payload);
    }
}
