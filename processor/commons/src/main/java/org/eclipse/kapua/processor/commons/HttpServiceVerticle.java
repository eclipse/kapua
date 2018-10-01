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
import javax.inject.Named;

import org.eclipse.kapua.commons.core.vertx.EventBusHealthCheckAdapter;
import org.eclipse.kapua.commons.core.vertx.HttpServiceConfig;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class HttpServiceVerticle extends AbstractVerticle {

    @Inject
    @Named("kapua.vertx-app.metrics-root")
    private String metricsRoot;

    @Inject
    @Named("kapua.vertx-app.http-server.host")
    private String host;

    @Inject
    @Named("kapua.vertx-app.http-server.port")
    private int port;

    private HttpServiceImpl impl;

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
            HttpServiceConfig config = new HttpServiceConfig();
            config.setHost(this.host);
            config.setMetricsRoot(this.metricsRoot);
            config.setPort(port);
            impl = new HttpServiceImpl(vertx, config);
            impl.register(EventBusHealthCheckAdapter.create(vertx.eventBus(), "health"));
            try {
                impl.start(future);
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
            if (impl != null) {
                try {
                    impl.stop(future);
                } catch (Exception e) {
                    future.fail(e);
                }
                return future;
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
