/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.commons.message;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.commons.KapuaAppPropertiesImpl;
import org.eclipse.kapua.service.device.management.commons.message.request.KapuaRequestChannelImpl;
import org.eclipse.kapua.service.device.management.commons.message.request.KapuaRequestMessageImpl;
import org.eclipse.kapua.service.device.management.commons.message.request.KapuaRequestPayloadImpl;
import org.eclipse.kapua.service.device.management.message.KapuaAppProperties;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessageFactory;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestPayload;

/**
 * {@link KapuaRequestMessageFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class KapuaRequestMessageFactoryImpl implements KapuaRequestMessageFactory {

    @Override
    public KapuaRequestChannel newRequestChannel() {
        return new KapuaRequestChannelImpl();
    }

    @Override
    public KapuaRequestMessage<?, ?> newRequestMessage() {
        return new KapuaRequestMessageImpl<>();
    }

    @Override
    public KapuaRequestPayload newRequestPayload() {
        return new KapuaRequestPayloadImpl();
    }

    @Override
    public KapuaAppProperties newAppProperties(String value) {
        return new KapuaAppPropertiesImpl(value);
    }
}
