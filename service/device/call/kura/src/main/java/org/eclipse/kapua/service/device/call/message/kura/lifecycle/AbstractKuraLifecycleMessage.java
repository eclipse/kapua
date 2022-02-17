/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.kura.Kura;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleMessage;

import java.util.Date;

/**
 * {@code abstract} base class for {@link Kura} {@link DeviceLifecycleMessage}
 *
 * @since 1.2.0
 */
public class AbstractKuraLifecycleMessage<C extends AbstractKuraLifecycleChannel, P extends AbstractKuraLifecyclePayload> extends KuraMessage<C, P> implements DeviceLifecycleMessage<C, P> {

    /**
     * Constructor.
     *
     * @param channel   The {@link AbstractKuraLifecycleChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link AbstractKuraLifecyclePayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 1.2.0
     */
    public AbstractKuraLifecycleMessage(C channel, Date timestamp, P payload) {
        super(channel, timestamp, payload);
    }

}
