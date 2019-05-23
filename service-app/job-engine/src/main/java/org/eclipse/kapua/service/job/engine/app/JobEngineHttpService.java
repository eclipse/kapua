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

import org.eclipse.kapua.service.commons.http.HttpService;
import org.eclipse.kapua.service.commons.http.HttpServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class JobEngineHttpService extends AbstractVerticle {

// TODO Add endpoionts for the specific service
//
//    private AccountsRestEndpoint accountsEndpoint;

    private HttpServiceConfig serverConfig;

    private HttpService service;

    @Component
    public static class Builder {
// TODO Add endpoionts for the specific service
//
//        private AccountsRestEndpoint accountsEndpoint;

        private HttpServiceConfig serverConfig;
// TODO Add endpoionts for the specific service
//
//        public AccountsRestEndpoint getAccountsRestEndpoint() {
//            return this.accountsEndpoint;
//        }
//
//        @Autowired
//        public Builder setAccountsRestEndpoint(AccountsRestEndpoint endpoint) {
//            this.accountsEndpoint = endpoint;
//            return this;
//        }

        public HttpServiceConfig getHttpServiceConfig() {
            return this.serverConfig;
        }

        @Autowired
        public Builder setHttpServiceConfig(@Qualifier("job-engine") HttpServiceConfig config) {
            this.serverConfig = config;
            return this;
        }

        public JobEngineHttpService build() {
            return new JobEngineHttpService(this);
        }
    }

    private JobEngineHttpService(Builder builder) {
//        this.accountsEndpoint = builder.getAccountsRestEndpoint();
        this.serverConfig = builder.getHttpServiceConfig();
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        HttpService.Builder builder = HttpService.builder(vertx, serverConfig);
// TODO Add endpoionts for the specific service
//        builder.addEndpoint(accountsEndpoint);
        service = builder.build();
        service.start(startFuture);
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {

        if (service != null) {
            service.stop(stopFuture);
            return;
        }
        stopFuture.complete();
    }
}