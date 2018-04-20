/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleMessage;

import java.util.Date;

public class AbstractKuraAppsBirthMessage<C extends AbstractKuraAppsBirthChannel, P extends AbstractKuraAppsBirthPayload> extends KuraMessage<C, P> implements DeviceLifecycleMessage<C, P> {

    /**
     * Constructor
     */
    public AbstractKuraAppsBirthMessage() {
        super();
    }

    /**
     * Constructor
     *
     * @param channel
     * @param timestamp
     * @param payload
     */
    public AbstractKuraAppsBirthMessage(C channel, Date timestamp, P payload) {
        super(channel, timestamp, payload);
    }

}
