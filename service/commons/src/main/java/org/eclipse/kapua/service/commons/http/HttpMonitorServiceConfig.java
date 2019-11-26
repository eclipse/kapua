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

import org.eclipse.kapua.service.commons.HttpEndpointConfig;

public class HttpMonitorServiceConfig {

    private String name = "monitoringService";
    private String basePath;
    private boolean enableHealthCheck;
    private boolean enableMetrics;
    private String registryName;
    private HttpEndpointConfig endpoint;

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String aBasePath) {
        basePath = aBasePath;
    }

    public boolean isHealthCheckEnable() {
        return enableHealthCheck;
    }

    public void setHealthCheckEnable(boolean enable) {
        enableHealthCheck = enable;
    }

    public boolean isMetricsEnable() {
        return enableMetrics;
    }

    public void setMetricsEnable(boolean enable) {
        enableMetrics = enable;
    }

    public String getMetricsRegistryName() {
        return registryName;
    }

    public void setMetricsRegistryName(String aRegistryName) {
        registryName = aRegistryName;
    }

    public HttpEndpointConfig getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(HttpEndpointConfig aConfig) {
        endpoint = aConfig;
    }

    @Override
    public String toString() {
        return String.format("\"name\":\"%s\""
                + ", \"basePath\":\"%s\""
                + ", \"enableHealthCheck\":\"%b\""
                + ", \"enableMetrics\":\"%b\""
                + ", \"regsitryName\":\"%s\""
                + ", \"endpoint\":{%s}", 
                getName(), getBasePath(), isHealthCheckEnable(), isMetricsEnable(), 
                getMetricsRegistryName(), endpoint == null ? "null" : endpoint.toString());
    }
}
