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
 *******************************************************************************/
package org.eclipse.kapua.service.device.management;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.request.KapuaRequestPayload;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class RequestMessageXmlRegistry {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final KapuaRequestMessageFactory factory = locator.getFactory(KapuaRequestMessageFactory.class);

    public KapuaRequestChannel newRequestChannel() {
        return factory.newRequestChannel();
    }

    public KapuaRequestMessage newRequestMessage() {
        return factory.newRequestMessage();
    }

    public KapuaRequestPayload newRequestPayload() {
        return factory.newRequestPayload();
    }
}
