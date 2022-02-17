/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.event;

/**
 * Service entry used buy the {@link ServiceEventModule} and the {@link ServiceEventHousekeeper} to bind/unbind the services to the correct addresses
 *
 */
public class ServiceEntry {

    private String serviceName;
    private String address;

    public ServiceEntry(String serviceName, String address) {
        this.serviceName = serviceName;
        this.address = address;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getAddress() {
        return address;
    }
}
