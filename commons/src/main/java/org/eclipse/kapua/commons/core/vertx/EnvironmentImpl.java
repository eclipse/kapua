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

import java.util.Objects;

import org.eclipse.kapua.commons.core.BeanContext;
import org.eclipse.kapua.commons.core.BeanContextConfig;
import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.spi.BeanContextConfigFactory;

import com.codahale.metrics.MetricRegistry;

import io.vertx.core.Vertx;

public class EnvironmentImpl implements EnvironmentSetup, Environment {

    private MetricRegistry metricRegistry;
    private BeanContextConfig beanContextConfig;
    private BeanContext beanContext;

    public EnvironmentImpl(Vertx vertx, Configuration config) {
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
    public void configure(BeanContextConfigFactory factory) {        
        Objects.requireNonNull(factory, "Invalid factory (null)");
        this.configure(factory.create());
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
