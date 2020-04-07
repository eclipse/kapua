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

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.eclipse.kapua.service.commons.http.HttpMonitorServiceContext;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceVerticleBuilder;

import io.vertx.core.Vertx;

/**
 * This class implements the running context for {@link BaseApplication}
 *
 */
final class ContextImpl implements Context {

    private Vertx vertx;

    private InitContext<? extends Configuration> initContext;

    private ContextImpl(@NotNull Vertx aVertx, @NotNull InitContext<? extends Configuration> aInitContext) {
        vertx = aVertx;
        initContext = aInitContext;
    }

    @Override
    public Vertx getVertx() {
        return vertx;
    }

    @Override
    public HttpMonitorServiceContext getMonitorServiceContext() {
        return initContext.getMonitorServiceVerticleBuilder().getContext();
    }

    @Override
    public <C> C getServiceContext(String name, Class<C> clazz) {
        return clazz.cast(initContext.getServiceVerticleBuilders().get(name).getContext());
    }

    public static ContextImpl create(@NotNull Vertx aVertx, @NotNull HttpMonitorServiceVerticleBuilder aMonitorServiceBuilder, @NotNull InitContext<? extends Configuration> aInitContext) {
        Objects.requireNonNull(aVertx, "param: aVertx");
        // Objects.requireNonNull(aMonitorServiceBuilder, "param: aMonitorServiceBuilder");
        Objects.requireNonNull(aInitContext, "param: aInitContext");
        return new ContextImpl(aVertx, aInitContext);
    }
}