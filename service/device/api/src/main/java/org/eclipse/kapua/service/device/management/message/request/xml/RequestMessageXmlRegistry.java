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
package org.eclipse.kapua.service.device.management.message.request.xml;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessageFactory;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestPayload;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class RequestMessageXmlRegistry {

    private final KapuaRequestMessageFactory kapuaRequestMessageFactory = KapuaLocator.getInstance().getFactory(KapuaRequestMessageFactory.class);

    public KapuaRequestMessage<?, ?> newRequestMessage() {
        return kapuaRequestMessageFactory.newRequestMessage();
    }

    public KapuaRequestChannel newRequestChannel() {
        return kapuaRequestMessageFactory.newRequestChannel();
    }

    public KapuaRequestPayload newRequestPayload() {
        return kapuaRequestMessageFactory.newRequestPayload();
    }
}
