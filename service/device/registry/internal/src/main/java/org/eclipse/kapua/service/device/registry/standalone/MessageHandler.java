/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.standalone;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;

public class MessageHandler {

    private final DeviceRegistryService deviceRegistry;

    public MessageHandler(DeviceRegistryService deviceRegistry) {
        this.deviceRegistry = deviceRegistry;
    }

    Object onMessage(Message message) {
        try {
            if (message.getOperation().equals("create")) {
                return deviceRegistry.create((DeviceCreator) message.getArguments().get(0));
            } else if (message.getOperation().equals("findByClientId")) {
                return deviceRegistry.findByClientId(message.getTenant(), (String) message.getArguments().get(0));
            }
            return null;
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }

}
