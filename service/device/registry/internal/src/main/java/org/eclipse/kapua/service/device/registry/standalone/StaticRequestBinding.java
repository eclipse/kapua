/*******************************************************************************
 * Copyright (c) 2011, 2017 RedHat and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     RedHat
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.standalone;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;

public class StaticRequestBinding {

    private final DeviceRegistryService deviceRegistry;

    public StaticRequestBinding(DeviceRegistryService deviceRegistry) {
        this.deviceRegistry = deviceRegistry;
    }

    void onMessage(Request request) {
        try {
            if (request.getOperation().equals("create")) {
                request.response(deviceRegistry.create((DeviceCreator) request.getArguments().get(0)));
            } else if (request.getOperation().equals("findByClientId")) {
                request.response(deviceRegistry.findByClientId(request.getTenant(), (String) request.getArguments().get(0)));
            }
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }

}
