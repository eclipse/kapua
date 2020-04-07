/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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
import java.util.Objects;
import java.util.Set;

import org.eclipse.kapua.service.commons.ServiceVerticleBuilder;
import org.eclipse.kapua.service.commons.ServiceVerticleBuilders;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceVerticleBuilder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.vertx.core.Vertx;

/**
 * This class implements the initialization context for {@link BaseApplication}
 *
 * @param <C> the configuration class associated with your own Vertx based application
 */
final class InitContextImpl<C extends Configuration> implements InitContext<C> {

    private Vertx vertx;

    private C configuration;

    private HttpMonitorServiceVerticleBuilder monitorServiceBuilder;

    private Set<ObjectFactory<ServiceVerticleBuilder<?, ?>>> serviceBuilderFactories = new HashSet<>();

    private ServiceVerticleBuilders serviceBuilders = new ServiceVerticleBuilders();

    @Override
    public Vertx getVertx() {
        return vertx;
    }

    /**
     * Sets the Vertx instance. If the class instance is created using Spring Boot, the Vertx instance is injected 
     * using the configuration defined in {@link AbstractBeanProvider}. 
     * 
     * @param aVertx the Vertx instance.
     */
    @Autowired
    public void setVertx(Vertx aVertx) {
        Objects.requireNonNull(aVertx, "param: aVertx");
        vertx = aVertx;
    }

    @Override
    public C getConfig() {
        return configuration;
    }

    /**
     * Sets the configuration instance. If the class instance is created using Spring Boot, the configuration is injected 
     * using the configuration defined in {@link AbstractBeanProvider}.
     * 
     * @param aConfig the configuration instance
     */
    @Autowired
    public void setConfig(C aConfig) {
        Objects.requireNonNull(aConfig, "param: aConfig");
        configuration = aConfig;
    }

    @Override
    public HttpMonitorServiceVerticleBuilder getMonitorServiceVerticleBuilder() {
        return monitorServiceBuilder;
    }

    /**
     * Sets the monitor builder instance. If the class instance is created using Spring Boot, the builder is injected 
     * using the configuration defined in {@link AbstractBeanProvider}.
     * 
     * @param builder
     */
    @Autowired
    public void setMonitorServiceVerticleBuilder(HttpMonitorServiceVerticleBuilder builder) {
        monitorServiceBuilder = builder;
    }

    @Override
    public Set<ObjectFactory<ServiceVerticleBuilder<?, ?>>> getServiceBuilderFactories() {
        return serviceBuilderFactories;
    }

    /**
     * Sets the service builder factories. If the class instance is created using Spring Boot, the factories are injected 
     * using the configuration defined in {@link AbstractBeanProvider}.
     * 
     * @param aServiceBuilderFactories
     */
    @Autowired(required = false)
    public void setServiceBuilderFactories(Set<ObjectFactory<ServiceVerticleBuilder<?, ?>>> aServiceBuilderFactories) {
        Objects.requireNonNull(serviceBuilderFactories, "param: serviceBuilderFactories");
        serviceBuilderFactories.addAll(aServiceBuilderFactories);
    }

    @Override
    public ServiceVerticleBuilders getServiceVerticleBuilders() {
        return serviceBuilders;
    }
}