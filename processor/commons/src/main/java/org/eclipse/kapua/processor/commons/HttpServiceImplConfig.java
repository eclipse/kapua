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
package org.eclipse.kapua.processor.commons;

import java.util.Objects;

import org.eclipse.kapua.commons.core.Configuration;

public class HttpServiceImplConfig {

    private String metricsRoot;
    private String host;
    private int port;
    private String eventbusHealthCheckName;
    private String eventbusHealthCheckAddress;

    private HttpServiceImplConfig(String aPrefix, Configuration aConfiguration) {
        metricsRoot = aConfiguration.getString("kapua.vertxApp.metricsRoot");
        host = aConfiguration.getString(aPrefix + ".httpServer.host");
        port = aConfiguration.getInteger(aPrefix + ".httpServer.port");
        eventbusHealthCheckName = aConfiguration.getString(aPrefix + ".eventBusHealthcheckAdapter.name");
        eventbusHealthCheckAddress = aConfiguration.getString(aPrefix + ".eventBusHealthcheckAdapter.address");
    }

    public static HttpServiceImplConfig create(String aPrefix, Configuration aConfiguration) {
        Objects.requireNonNull(aPrefix, "Invalid null prefix");
        Objects.requireNonNull(aConfiguration, "Invalid null configuration");
        return new HttpServiceImplConfig(aPrefix, aConfiguration);
    }

    public String getMetricsRoot() {
        return metricsRoot;
    }

    public void setMetricsRoot(String metricsRoot) {
        this.metricsRoot = metricsRoot;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEventbusHealthCheckName() {
        return eventbusHealthCheckName;
    }

    public void setEventbusHealthCheckName(String eventbusHealthCheckName) {
        this.eventbusHealthCheckName = eventbusHealthCheckName;
    }

    public String getEventbusHealthCheckAddress() {
        return eventbusHealthCheckAddress;
    }

    public void setEventbusHealthCheckAddress(String eventbusHealthCheckAddress) {
        this.eventbusHealthCheckAddress = eventbusHealthCheckAddress;
    }
}
