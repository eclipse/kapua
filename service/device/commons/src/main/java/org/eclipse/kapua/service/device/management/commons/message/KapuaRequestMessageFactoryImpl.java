/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
