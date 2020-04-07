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

import org.eclipse.kapua.service.commons.http.HttpMonitorServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class extends the {@link Configuration} interface for {@link BaseApplication}
 *
 */
public class BaseConfiguration implements Configuration {

    private String applicationName;
    private long startupTimeout;
    private VertxConfig vertxConfig;
    private HttpMonitorServiceConfig monitorServiceConfig;

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Sets the application name. If the class instance is created using Spring Boot, the name is injected 
     * using the configuration defined in {@link AbstractBeanProvider}. 
     * 
     * @param anApplicationName
     */
    @Autowired
    public void setApplicationName(String anApplicationName) {
        applicationName = anApplicationName;
    }

    @Override
    public long getStartupTimeout() {
        return startupTimeout;
    }

    /**
     * Sets the startup timeout. If the class instance is created using Spring Boot, the timeout is injected 
     * using the configuration defined in {@link AbstractBeanProvider}. 
     * 
     * @param aStartupTimeout
     */
    @Autowired
    public void setStartupTimeout(long aStartupTimeout) {
        startupTimeout = aStartupTimeout;
    }

    @Override
    public VertxConfig getVertxConfig() {
        return vertxConfig;
    }

    /**
     * Sets the Vertx configuration. If the class instance is created using Spring Boot, the configuration is 
     * injected using the bean defined in {@link AbstractBeanProvider}. 
     * 
     * @param aConfig
     */
    @Autowired
    public void setVertxConfig(VertxConfig aConfig) {
        vertxConfig = aConfig;
    }

    @Override
    public HttpMonitorServiceConfig getHttpMonitorServiceConfig() {
        return monitorServiceConfig;
    }

    /**
     * Sets the monitor service configuration. If the class instance is created using Spring Boot, the configuration 
     * is injected using the bean defined in {@link AbstractBeanProvider}. 
     * 
     * @param aConfig
     */
    @Autowired
    public void setHttpMonitorServiceConfig(HttpMonitorServiceConfig aConfig) {
        monitorServiceConfig = aConfig;
    }
}
