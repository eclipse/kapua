/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core.vertx;

/**
 * Holds the configuration parameters of an {@link EventBusService}
 *
 */
public class EventBusServiceConfig {

    private String healthCheckAddress;

    private String address;

    public String getHealthCheckAddress() {
        return healthCheckAddress;
    }

    public void setHealthCheckAddress(String address) {
        this.healthCheckAddress = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
