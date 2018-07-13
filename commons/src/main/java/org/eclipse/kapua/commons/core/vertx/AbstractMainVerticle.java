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

import javax.inject.Inject;

import org.eclipse.kapua.commons.core.Configuration;

public abstract class AbstractMainVerticle extends AbstractVerticle {

    private static Logger logger = LoggerFactory.getLogger(AbstractMainVerticle.class);

    @Inject
    private Configuration configuration;

    @Inject
    private Environment environment;

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public final void init(Vertx vertx, Context context) {
        super.init(vertx, context);
    }

    @Override
    public final void start() throws Exception {
        logger.trace("Starting verticle...");
        super.start();

        HttpRestServer httpRestServer = environment.getBeanContext().getInstance(HttpRestServer.class);
        ((EnvironmentImpl)environment).setHttpRestService(httpRestServer);

        Future<Void> startFuture = Future.future();
        internalStart(startFuture);    
        startFuture.compose( mapper -> {
            Future<Void> fut = Future.future();
            vertx.deployVerticle(httpRestServer, ar -> {
                if (ar.succeeded()) {
                    fut.complete();
                } else {
                    fut.fail(ar.cause());
                }});
            return fut;
        }).setHandler(ar -> {
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
