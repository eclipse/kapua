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

import org.eclipse.kapua.service.commons.HealthCheckProvider;
import org.eclipse.kapua.service.commons.HealthChecker;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

public interface HttpMonitorService extends HealthCheckProvider {

    public static interface Builder {

        public void addLivenessChecker(HealthChecker checker);

        public void addReadinessChecker(HealthChecker checker);

        public void setServerConfig(HttpMonitorServiceConfig config);

        public HttpMonitorService build();
    }

    public void start(Future<Void> startFuture) throws Exception;

    public void stop(Future<Void> stopFuture) throws Exception;

    public static Builder builder(Vertx vertx) {
        return new HttpMonitorServiceImpl.Builder(vertx);
    }

    public static Builder builder(Vertx vertx, HttpMonitorServiceConfig config) {
        return new HttpMonitorServiceImpl.Builder(vertx, config);
    }
}
