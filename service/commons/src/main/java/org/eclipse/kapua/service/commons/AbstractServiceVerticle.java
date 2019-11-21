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
package org.eclipse.kapua.service.commons;

import java.util.Objects;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public abstract class AbstractServiceVerticle extends AbstractVerticle {

    @Override
    public final void init(Vertx vertx, Context context) {
        super.init(vertx, context);
    }

    @Override
    public final void start(Future<Void> startFuture) throws Exception {
        Objects.requireNonNull(startFuture, "Param: startFuture");
        Future.<Void>succeededFuture()
        .compose(mapper -> {
            Future<Void> serviceStart = Future.future();
            try {
                this.internalStart(serviceStart);
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
    public final void stop(Future<Void> stopFuture) throws Exception {
        Objects.requireNonNull(stopFuture, "Param: stopFuture");
        Future.<Void>succeededFuture()
        .compose(mapper -> {
            Future<Void> serviceStop = Future.future();
            try {
                this.internalStop(stopFuture);
            } catch (Exception e) {
                serviceStop.fail(e);
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

    protected abstract void internalStart(Future<Void> startFuture) throws Exception;

    protected abstract void internalStop(Future<Void> stopFuture) throws Exception;
}
