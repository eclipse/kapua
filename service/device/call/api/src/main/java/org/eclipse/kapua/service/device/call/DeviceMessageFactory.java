/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.device.call.message.DeviceChannel;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.service.device.call.message.DevicePayload;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestPayload;

/**
 * Device message service factory definition.
 *
 * @since 1.0
 */
public interface DeviceMessageFactory extends KapuaObjectFactory {

    /**
     * Creates a new device message
     *
     * @return
     */
    DeviceMessage newMessage();

    /**
     * Creates a new device request message
     *
     * @return
     */
    DeviceRequestMessage newRequestMessage();

    /**
     * Creates a new device channel
     *
     * @return
     */
    DeviceChannel newChannel();

    /**
     * Creates a new device request channel
     *
     * @return
     */
    DeviceRequestChannel newRequestChannel();

    /**
     * Creates a new device payload
     *
     * @return
     */
    DevicePayload newPayload();

    /**
     * Creates a new device request payload
     *
     * @return
     */
    DeviceRequestPayload newRequestPayload();

}
