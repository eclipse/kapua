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

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.eclipse.kapua.service.commons.HealthCheckProvider;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

public interface HttpMonitorService {

    public HttpMonitorService addHealthCheckProviders(Set<HealthCheckProvider> someProviders);

    public HttpMonitorService addHealthCheckProvider(HealthCheckProvider aProvider);

    public void start(Future<Void> startFuture) throws Exception;

    public void stop(@NotNull Future<Void> stopFuture) throws Exception;

    public static HttpMonitorService create(Vertx aVertx) {
        return new HttpMonitorServiceImpl(aVertx);
    }

    public static HttpMonitorService create(Vertx aVertx, HttpMonitorServiceConfig aConfig) {
        return new HttpMonitorServiceImpl(aVertx, aConfig);
    }
}
