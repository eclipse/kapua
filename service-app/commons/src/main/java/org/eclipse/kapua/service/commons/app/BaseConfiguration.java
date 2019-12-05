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
package org.eclipse.kapua.service.commons.app;

import org.eclipse.kapua.service.commons.ServiceConfigs;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceConfig;
import org.eclipse.kapua.service.commons.http.HttpServiceBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseConfiguration implements Configuration {

    private String applicationName;
    private long startupTimeout;
    private VertxConfig vertxConfig;
    private ServiceConfigs serviceConfigs;
    private HttpMonitorServiceConfig monitorServiceConfig;

    private ServiceBuilderManager serviceBuilderManager;
    private HttpServiceBuilderFactory httpServiceBuilderFactory;

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Autowired
    public void setApplicationName(String anApplicationName) {
        applicationName = anApplicationName;
    }

    @Override
    public long getStartupTimeout() {
        return startupTimeout;
    }

    @Autowired
    public void setStartupTimeout(long aStartupTimeout) {
        startupTimeout = aStartupTimeout;
    }

    @Override
    public VertxConfig getVertxConfig() {
        return vertxConfig;
    }

    @Autowired
    public void setVertxConfig(VertxConfig aConfig) {
        vertxConfig = aConfig;
    }

    @Override
    public ServiceConfigs getServiceConfigs() {
        return serviceConfigs;
    }

    @Autowired
    public void setServiceConfigs(ServiceConfigs someConfigs) {
        serviceConfigs = someConfigs;
    }

    @Override
    public HttpMonitorServiceConfig getHttpMonitorServiceConfig() {
        return monitorServiceConfig;
    }

    @Autowired
    public void setHttpMonitorServiceConfig(HttpMonitorServiceConfig aConfig) {
        monitorServiceConfig = aConfig;
    }

    @Autowired
    public void setServiceBuilderManager(ServiceBuilderManager aServiceBuilderManager) {
        serviceBuilderManager = aServiceBuilderManager;
        if (httpServiceBuilderFactory == null) {
            HttpServiceBuilderFactory.create(serviceBuilderManager);
        }
    }
}
