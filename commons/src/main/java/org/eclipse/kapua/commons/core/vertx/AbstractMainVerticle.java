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
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public abstract class AbstractMainVerticle extends AbstractVerticle {

    private static Logger logger = LoggerFactory.getLogger(AbstractMainVerticle.class);

    @Override
    public final void init(Vertx vertx, Context context) {
        super.init(vertx, context);
    }

    @Override
    public final void start() throws Exception {
        logger.trace("Starting verticle...");
        super.start();

        Future<Void> startFuture = Future.future();
        internalStart(startFuture);    
        startFuture.setHandler(ar -> {
            if (ar.succeeded()) {
                logger.trace("Starting verticle...DONE");
            } else {
                logger.error("Starting verticle...FAILED", ar.cause());
            }
        });
    }

    @Override
    public final void stop() throws Exception {
        logger.trace("Stopping verticle...");
        Future<Void> stopFuture = Future.future();
        internalStop(stopFuture);
        super.stop();
        logger.trace("Stopping verticle...DONE");
    }

    protected void internalStart(Future<Void> startFuture) throws Exception {
    }

    protected void internalStop(Future<Void> stopFuture) throws Exception {
    }
}