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

/**
 * Base verticle class that is automatically deployed by the {@link VertxApplication} 
 * <p>
 * During execution of the {@link #start} method, {@link #internalStart} is
 * invoked. During execution of the {@link #stop} method, {@link #internalStop} is
 * invoked. Implementation classes may execute application specific logic 
 * by inheriting and implementing these methods (e.g. deploy other verticles).
 *
 */
public abstract class AbstractMainVerticle extends AbstractVerticle {

    private static Logger logger = LoggerFactory.getLogger(AbstractMainVerticle.class);

    @Override
    public final void init(Vertx vertx, Context context) {
        super.init(vertx, context);
    }

    @Override
    public final void start(Future<Void> startEvent) throws Exception {
        logger.trace("Starting verticle...");
        Future.succeededFuture()
        .compose(map -> {
            Future<Void> event = Future.future();
            try {
                super.start(event);
            } catch (Exception e) {
                event.fail(e);
            }
            return event;
        })
        .compose(mapper -> {
            Future<Void> event = Future.future();
            try {
                internalStart(event);
            } catch (Exception e) {
                event.fail(e);
            } 
            return event;
        }).setHandler(event -> {
            if (event.succeeded()) {
                logger.trace("Starting verticle...DONE");
                startEvent.complete();
            } else {
                logger.error("Starting verticle...FAIL", event.cause());
                startEvent.fail(event.cause());
            }
        });
    }

    @Override
    public final void start() throws Exception {
        logger.trace("Stopping verticle...");
        super.start();
        this.internalStart();
    }

    @Override
    public final void stop(Future<Void> stopEvent) throws Exception {
        logger.trace("Stopping verticle...");
        Future.succeededFuture()
        .compose(map -> {
            Future<Void> event = Future.future();
            try {
                internalStop(event);
            } catch (Exception e) {
                event.fail(e);
            }
            return event;
        })
        .compose(mapper -> {
            Future<Void> event = Future.future();
            try {
                super.stop(event);
            } catch (Exception e) {
                event.fail(e);
            }
            return event;
        })
        .setHandler(event -> {
            if (event.succeeded()) {
                logger.trace("Stopping verticle...DONE");
                stopEvent.complete();
            } else {
                logger.error("Stopping verticle...FAIL", event.cause());
                stopEvent.fail(event.cause());
            }
        });
    }

    @Override
    public final void stop() throws Exception {
        logger.trace("Stopping verticle...");
        this.internalStop();
        super.stop();
    }

    protected void internalStart(Future<Void> startFuture) throws Exception {
        internalStart();
        startFuture.complete();
    }

    protected void internalStart() throws Exception {
    }

    protected void internalStop(Future<Void> stopFuture) throws Exception {
        internalStart();
        stopFuture.complete();
    }

    protected void internalStop() throws Exception {
    }
}
