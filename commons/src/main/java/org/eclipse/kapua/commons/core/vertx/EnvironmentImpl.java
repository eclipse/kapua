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

import org.eclipse.kapua.commons.core.ObjectContext;
import org.eclipse.kapua.commons.core.ObjectContextConfig;
import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.spi.ObjectContextConfigFactory;

import com.codahale.metrics.MetricRegistry;

import io.vertx.core.Vertx;

public class EnvironmentImpl implements EnvironmentSetup, Environment {

    private MetricRegistry metricRegistry;
    private ObjectContextConfig beanContextConfig;
    private ObjectContext beanContext;

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
    public void configure(ObjectContextConfig context) {
        this.beanContextConfig = context;
    }

    @Override
    public void configure(ObjectContextConfigFactory factory) {
        Objects.requireNonNull(factory, "Invalid factory (null)");
        this.configure(factory.create());
    }

    @Override
    public ObjectContext getBeanContext() {
        return beanContext;
    }

    public void setBeanContext(ObjectContext context) {
        beanContext = context;
    }

    public ObjectContextConfig getBeanContextConfig() {
        return beanContextConfig;
    }
}
