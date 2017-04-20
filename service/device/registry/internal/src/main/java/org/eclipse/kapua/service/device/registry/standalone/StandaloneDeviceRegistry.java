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

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.internal.DeviceFactoryImpl;

import java.math.BigInteger;
import java.util.Collections;

import static java.lang.Long.parseLong;
import static java.util.Collections.singletonList;

public class StandaloneDeviceRegistry {

    public static void main(String... args) {
        if(args.length == 0) {
            main("1", "create", "foo");
            main("1", "findByClientId", "foo");
            return;
        }

        KapuaId tenant = new KapuaEid(BigInteger.valueOf(parseLong(args[0])));
        String operation = args[1];
        String clientId = args[2];

        MessageHandler messageHandler = new MessageHandler(new StandaloneDeviceRegistryFactory().create());

        Object result = null;
        if(operation.equals("create")) {
            result = messageHandler.onMessage(new Message(tenant, operation, singletonList(new DeviceFactoryImpl().newCreator(tenant, clientId))));
        } else if(operation.equals("findByClientId")) {
            result = messageHandler.onMessage(new Message(tenant, operation, Collections.singletonList(clientId)));
        }
        System.out.println(result);
    }

}
