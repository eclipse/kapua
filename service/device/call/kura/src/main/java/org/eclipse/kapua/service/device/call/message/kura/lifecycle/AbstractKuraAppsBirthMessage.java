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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleMessage;

import java.util.Date;

/**
 * {@code abstract} base class for {@link KuraAppsMessage} and {@link KuraBirthMessage}.
 * <p>
 * {@link KuraAppsMessage} and {@link KuraBirthMessage} have the same format.
 *
 * @since 1.0.0
 */
public class AbstractKuraAppsBirthMessage<C extends AbstractKuraAppsBirthChannel, P extends AbstractKuraAppsBirthPayload> extends AbstractKuraLifecycleMessage<C, P> implements DeviceLifecycleMessage<C, P> {

    /**
     * Constructor.
     *
     * @param channel   The {@link AbstractKuraAppsBirthChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link AbstractKuraAppsBirthPayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 1.0.0
     */
    public AbstractKuraAppsBirthMessage(C channel, Date timestamp, P payload) {
        super(channel, timestamp, payload);
    }

}
