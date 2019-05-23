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
package org.eclipse.kapua.service.job.engine.app;

import org.eclipse.kapua.service.commons.http.HttpMonitorService;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class JobEngineHttpServiceMonitor extends AbstractVerticle {

// TODO Add checkers for the specific service
//    private HealthChecker someChecker;

    private HttpMonitorServiceConfig serverConfig;

    private HttpMonitorService service;

    @Component
    public static class Builder {

// TODO Add checkers for the specific service
//        private HealthChecker someChecker;

        private HttpMonitorServiceConfig serverConfig;

// TODO Add checkers for the specific service
//        public HealthChecker getSomeChecker() {
//            return someChecker;
//        }
//
//        @Autowired
//        public Builder setSomeChecker(HealthChecker someChecker) {
//            this.someChecker = someChecker;
//            return this;
//        }

        public HttpMonitorServiceConfig getServerConfig() {
            return serverConfig;
        }

        @Autowired
        public Builder setServerConfig(@Qualifier("monitoring") HttpMonitorServiceConfig serverConfig) {
            this.serverConfig = serverConfig;
            return this;
        }

        public JobEngineHttpServiceMonitor build() {
            return new JobEngineHttpServiceMonitor(this);
        }
    }

    private JobEngineHttpServiceMonitor(Builder builder) {
        this.serverConfig = builder.serverConfig;
// TODO Add checkers for the specific service
//        this.someChecker = builder.someChecker;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        Future.<Void>succeededFuture()
                .compose(mapper -> {
                    Future<Void> serviceStart = Future.future();

                    try {
                        // Add health checks endpoint
                        HttpMonitorService.Builder builder = HttpMonitorService.builder(vertx, serverConfig);
// TODO Add checkers for the specific service
//                        builder.addLivenessChecker(someChecker);
                        service = builder.build();
                        service.start(serviceStart);
                    } catch (Exception e) {
                        serviceStart.fail(e);
                    }
                    return serviceStart;
                })
                .setHandler(ar -> {
                    if (ar.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(ar.cause());
                    }
                });
    }

    @Override
    public void stop(Future<Void> stopFuture) {

        Future.<Void>succeededFuture()
                .compose(mapper -> {
                    Future<Void> serviceStop = Future.future();
                    if (service != null) {
                        try {
                            service.stop(serviceStop);
                        } catch (Exception e) {
                            serviceStop.fail(e);
                        }
                    }
                    return serviceStop;
                })
                .setHandler(ar -> {
                    if (ar.succeeded()) {
                        stopFuture.complete();
                    } else {
                        stopFuture.fail(ar.cause());
                    }
                });
    }
}