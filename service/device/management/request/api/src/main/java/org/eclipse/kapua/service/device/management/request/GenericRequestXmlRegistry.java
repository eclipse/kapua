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
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.request;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestChannel;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestPayload;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseChannel;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponsePayload;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class GenericRequestXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final GenericRequestFactory FACTORY = LOCATOR.getFactory(GenericRequestFactory.class);

    public GenericRequestChannel newRequestChannel() {
        return FACTORY.newRequestChannel();
    }

    public GenericRequestPayload newRequestPayload() {
        return FACTORY.newRequestPayload();
    }

    public GenericRequestMessage newRequestMessage() {
        return FACTORY.newRequestMessage();
    }

    public GenericResponseChannel newResponseChannel() {
        return FACTORY.newResponseChannel();
    }

    public GenericResponsePayload newResponsePayload() {
        return FACTORY.newResponsePayload();
    }

    public GenericResponseMessage newResponseMessage() {
        return FACTORY.newResponseMessage();
    }
}
