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
package org.eclipse.kapua.commons.event.jms;

import org.eclipse.kapua.event.ServiceEventBusListener;

public class Subscription {

    private String name;
    private String address;
    private ServiceEventBusListener kapuaEventListener;

    public Subscription(String address, String name, ServiceEventBusListener kapuaEventListener) {
        this.name = name;
        this.address = address;
        this.kapuaEventListener = kapuaEventListener;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public ServiceEventBusListener getKapuaEventListener() {
        return kapuaEventListener;
    }

}