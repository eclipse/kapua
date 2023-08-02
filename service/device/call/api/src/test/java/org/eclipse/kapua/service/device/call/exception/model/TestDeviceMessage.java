/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.exception.model;

import org.eclipse.kapua.service.device.call.message.DeviceChannel;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.service.device.call.message.DevicePayload;

import java.util.Date;

/**
 * {@link DeviceMessage} for testing.
 *
 * @since 2.0.0
 */
public class TestDeviceMessage implements DeviceMessage<DeviceChannel, DevicePayload> {
    @Override
    public DeviceChannel getChannel() {
        return null;
    }

    @Override
    public void setChannel(DeviceChannel channel) {

    }

    @Override
    public DevicePayload getPayload() {
        return null;
    }

    @Override
    public void setPayload(DevicePayload payload) {

    }

    @Override
    public Date getTimestamp() {
        return null;
    }

    @Override
    public void setTimestamp(Date timestamp) {

    }
}
