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

import io.vertx.core.Vertx;

import java.util.Objects;
import java.util.ServiceLoader;

import org.eclipse.kapua.commons.core.BeanContext;
import org.eclipse.kapua.commons.core.BeanContextConfig;
import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.spi.BeanContextConfigFactory;

import com.codahale.metrics.MetricRegistry;

public class EnvironmentImpl implements EnvironmentSetup, Environment {

    private HttpRestService httpRestService;
    private MetricRegistry metricRegistry;
    private BeanContextConfig beanContextConfig;
    private BeanContext beanContext;

    public EnvironmentImpl(Vertx vertx, Configuration config) {
    }

    @Override
    public HttpRestService getHttpRestService() {
        return httpRestService;
    }

    public void setHttpRestService(HttpRestService service) {
        this.httpRestService = service;
    }

    @Override
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    public void setMetricRegistry(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public void configure(BeanContextConfig context) {
        this.beanContextConfig = context;
    }

    @Override
    public void configure(String beanContextName) {

        Objects.requireNonNull(beanContextName, "Context name is null");
        ServiceLoader<BeanContextConfigFactory> loader = ServiceLoader.load(BeanContextConfigFactory.class);
        if (loader == null || !loader.iterator().hasNext()) {
            throw new RuntimeException("No factories found");
        }

        BeanContextConfigFactory selectedFactory = null;
        for(BeanContextConfigFactory factory:loader) {
            if (beanContextName.equals(factory.getName())) {
                selectedFactory = factory;
            }
        }

        if (selectedFactory == null) {
            throw new RuntimeException(String.format("No context bean factory found with name %s", beanContextName));
        }

        this.configure(selectedFactory.create());
    }

    @Override
    public BeanContext getBeanContext() {
        return beanContext;
    }

    public void setBeanContext(BeanContext context) {
        beanContext = context;
    }

    public BeanContextConfig getBeanContextConfig() {
        return beanContextConfig;
    }
}
