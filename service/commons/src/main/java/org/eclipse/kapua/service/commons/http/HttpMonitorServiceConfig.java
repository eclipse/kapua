/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons.http;

public class HttpMonitorServiceConfig {

    private boolean enableHealthCheck;
    private boolean enableMetrics;
    private String registryName;
    private HttpServiceConfig http;

    public boolean isHealthCheckEnable() {
        return enableHealthCheck;
    }

    public void setHealthCheckEnable(boolean enableHealthCheck) {
        this.enableHealthCheck = enableHealthCheck;
    }

    public boolean isMetricsEnable() {
        return enableMetrics;
    }

    public void setMetricsEnable(boolean enableMetrics) {
        this.enableMetrics = enableMetrics;
    }

    public String getMetricsRegistryName() {
        return registryName;
    }

    public void setMetricsRegistryName(String registryName) {
        this.registryName = registryName;
    }

    public HttpServiceConfig getHttp() {
        return http;
    }

    public void setHttp(HttpServiceConfig http) {
        this.http = http;
    }
}
