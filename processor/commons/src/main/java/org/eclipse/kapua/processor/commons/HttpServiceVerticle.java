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
package org.eclipse.kapua.processor.commons;

import javax.inject.Inject;

import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.vertx.EventBusHealthCheckAdapter;
import org.eclipse.kapua.commons.core.vertx.HttpServiceConfig;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class HttpServiceVerticle extends AbstractVerticle {

    @Inject
    Configuration configuration;

    private HttpServiceImpl restService;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Future.succeededFuture()
        .compose(map-> {
            Future<Void> future = Future.future();
            try {
                super.start(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            HttpServiceImplConfig restServiceConfig = HttpServiceImplConfig.create("kapua.restService", configuration);
            HttpServiceConfig config = new HttpServiceConfig();
            config.setHost(restServiceConfig.getHost());
            config.setMetricsRoot(restServiceConfig.getMetricsRoot());
            config.setPort(restServiceConfig.getPort());
            restService = new HttpServiceImpl(vertx, config);
            restService.register(EventBusHealthCheckAdapter.create(vertx.eventBus()
                    , restServiceConfig.getEventbusHealthCheckName()
                    , restServiceConfig.getEventbusHealthCheckAddress()));
            try {
                restService.start(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
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
    public void stop(Future<Void> stopFuture) throws Exception {
        Future.succeededFuture()
        .compose(map -> {
            Future<Void> future = Future.future();
            if (restService != null) {
                try {
                    restService.stop(future);
                } catch (Exception e) {
                    future.fail(e);
                }
            }
            else {
                future.complete();
            }
            return future;
        })
        .compose(map-> {
            Future<Void> future = Future.future();
            try {
                super.stop(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
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
