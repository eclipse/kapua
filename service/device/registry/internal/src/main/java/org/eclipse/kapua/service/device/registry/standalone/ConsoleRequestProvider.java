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

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.internal.DeviceFactoryImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Collections;

public class ConsoleRequestProvider {

    public void start() {
        try {
            StaticRequestBinding staticRequestBinding = new StaticRequestBinding(new StandaloneDeviceRegistryFactory().create());

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String buffer = null;
            System.out.println("Enter message request:");
            while ((buffer = br.readLine()) != null) {
                String[] message = buffer.split(" ");
                KapuaId tenant = new KapuaEid(BigInteger.valueOf(Long.parseLong(message[0])));
                String operation = message[1];
                String clientId = message[2];

                Request request = null;
                if (operation.equals("create")) {
                    request = new Request(tenant, operation, Collections.singletonList(new DeviceFactoryImpl().newCreator(tenant, clientId)));
                    staticRequestBinding.onMessage(request);
                } else if (operation.equals("findByClientId")) {
                    request = new Request(tenant, operation, Collections.singletonList(clientId));
                    staticRequestBinding.onMessage(request);
                }
                System.out.println(request.getResponse());
                System.out.println("Enter message request:");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
