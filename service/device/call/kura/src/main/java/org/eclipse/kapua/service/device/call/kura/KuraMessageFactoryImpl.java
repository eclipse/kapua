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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.call.DeviceMessageFactory;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.service.device.call.message.DevicePayload;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestPayload;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestPayload;

/**
 * {@link DeviceMessageFactory} {@link Kura} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class KuraMessageFactoryImpl implements DeviceMessageFactory {

    @Override
    public DeviceRequestChannel newChannel() {
        return new KuraRequestChannel();
    }

    @Override
    public DeviceRequestChannel newRequestChannel() {
        return new KuraRequestChannel();
    }

    @Override
    public DevicePayload newPayload() {
        return new KuraPayload();
    }

    @Override
    public DeviceRequestPayload newRequestPayload() {
        return new KuraRequestPayload();
    }

    @Override
    public DeviceMessage newMessage() {
        return new KuraMessage();
    }

    @Override
    public DeviceRequestMessage newRequestMessage() {
        return new KuraRequestMessage();
    }

}
