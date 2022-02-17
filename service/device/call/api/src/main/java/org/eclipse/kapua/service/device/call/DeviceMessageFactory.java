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
 * {@link DeviceMessageFactory} definition.
 *
 * @since 1.0.0
 */
public interface DeviceMessageFactory extends KapuaObjectFactory {

    /**
     * Instantiates a new {@link DeviceMessage}
     *
     * @return The newly instantiated {@link DeviceMessage}.
     * @since 1.0.0
     */
    DeviceMessage newMessage();

    /**
     * Instantiates a new {@link DeviceRequestMessage}
     *
     * @return The newly instantiated {@link DeviceRequestMessage}.
     * @since 1.0.0
     */
    DeviceRequestMessage newRequestMessage();

    /**
     * Instantiates a new {@link DeviceChannel}
     *
     * @return The newly instantiated {@link DeviceChannel}.
     * @since 1.0.0
     */
    DeviceChannel newChannel();

    /**
     * Instantiates a new {@link DeviceRequestChannel}
     *
     * @return The newly instantiated {@link DeviceRequestChannel}.
     * @since 1.0.0
     */
    DeviceRequestChannel newRequestChannel();

    /**
     * Instantiates a new {@link DevicePayload}
     *
     * @return The newly instantiated {@link DevicePayload}.
     * @since 1.0.0
     */
    DevicePayload newPayload();

    /**
     * Instantiates a new {@link DeviceRequestPayload}
     *
     * @return The newly instantiated {@link DeviceRequestPayload}.
     * @since 1.0.0
     */
    DeviceRequestPayload newRequestPayload();

}
