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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.service.commons.ServiceBuilder;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceConfig;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class BaseConfiguration implements Configuration {

    private String applicationName;
    private long startupTimeout;
    private VertxConfig vertxConfig;
    private HttpMonitorServiceConfig monitorServiceConfig;

    private Set<ObjectFactory<? extends ServiceBuilder<?, ?>>> serviceBuilderFactories = new HashSet<>();

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
    public HttpMonitorServiceConfig getHttpMonitorServiceConfig() {
        return monitorServiceConfig;
    }

    @Autowired
    public void setHttpMonitorServiceConfig(HttpMonitorServiceConfig aConfig) {
        monitorServiceConfig = aConfig;
    }

    @Qualifier("services")
    @Autowired
    public void setServiceBuilderFactories(Set<ObjectFactory<? extends ServiceBuilder<?, ?>>> someServiceBuilderFactories) {
        this.serviceBuilderFactories.addAll(someServiceBuilderFactories);
    }
}
