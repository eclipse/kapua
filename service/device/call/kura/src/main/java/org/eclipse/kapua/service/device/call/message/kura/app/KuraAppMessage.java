/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.app;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;

import java.util.Date;

/**
 * {@link DeviceAppMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @param <C> The {@link KuraAppChannel} type.
 * @param <P> The {@link KuraAppPayload} type.
 * @since 1.0.0
 */
public abstract class KuraAppMessage<C extends KuraAppChannel, P extends KuraAppPayload> extends KuraMessage<C, P> implements DeviceAppMessage<C, P> {

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public KuraAppMessage() {
        super();
    }

    /**
     * Constructor.
     *
     * @param channel   The {@link KuraAppChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link KuraAppPayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 1.0.0
     */
    public KuraAppMessage(C channel, Date timestamp, P payload) {
        super(channel, timestamp, payload);
    }
}
