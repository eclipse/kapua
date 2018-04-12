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
package org.eclipse.kapua.service.device.call.message.kura.app;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;

import java.util.Date;

/**
 * {@link DeviceAppMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 */
public abstract class KuraAppMessage<C extends KuraAppChannel, P extends KuraAppPayload> extends KuraMessage<C, P> implements DeviceAppMessage<C, P> {

    public KuraAppMessage() {
        super();
    }

    public KuraAppMessage(C channel, Date timestamp, P payload) {
        super(channel, timestamp, payload);
    }
}
