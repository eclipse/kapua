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

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * Kapua abstract application
 *
 */
public abstract class AbstractApplication {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractApplication.class);

    private final static long TIMEOUT = 30000;//TODO get from configuration
    private ApplicationContext applicationContext;
    private Vertx vertx;
    private HttpServer httpServer;
    private Router router;
    String path;

    protected AbstractApplication(String path) {
        this.path = path;
    }

    final public void start(String[] args) throws Exception {
        //get from configuration
        VertxOptions options = new VertxOptions();
        options.setBlockedThreadCheckInterval(60 * 1000);
        // TODO more options?
        vertx = Vertx.vertx(options);

        //router
        router = Router.router(vertx);

        //http server
        HttpServerOptions httpOptions = new HttpServerOptions();
        httpOptions.setHost("0.0.0.0");
        httpOptions.setPort(8090);
        //TODO get options

        httpServer = vertx.createHttpServer(httpOptions);
        httpServer.requestHandler(router::accept).listen();

        //init application context
        applicationContext = new ContextImpl(this);

        CompletableFuture<String> startFuture = internalStart(applicationContext);
        try {
            startFuture.get(TIMEOUT, TimeUnit.SECONDS);
        }
        catch (TimeoutException e) {
            logger.error("Timeout starting application: {}", e.getMessage(), e);
            stop();
        }
        catch (CancellationException | InterruptedException e) {
            logger.error("Error starting application: {}", e.getMessage(), e);
            stop();
        }
        catch (ExecutionException e) {
            logger.error("Error starting application: {}", e.getMessage(), e);
        }
    }

    final public void stop() throws Exception {
        CompletableFuture<String> stopFuture = internalStop(applicationContext);
        if (applicationContext.getVertx() != null) {
            applicationContext.getVertx().close(ar -> {
                logger.info("Application stopped!");
            });
        }
        else {
            stopFuture.complete("Nothing to do... application stopped!");
        }
    }

    public String getPath() {
        return path;
    }

    protected abstract CompletableFuture<String> internalStart(ApplicationContext applicationContext) throws Exception;

    protected abstract CompletableFuture<String> internalStop(ApplicationContext applicationContext) throws Exception;

    private class ContextImpl implements ApplicationContext {

        private AbstractApplication abstractApplication;
        private Route route;
        private HealthCheckHandler handler;

        public ContextImpl(AbstractApplication abstractApplication) {
            this.abstractApplication = abstractApplication;
            route = abstractApplication.router.get(getPath());
            handler = HealthCheckHandler.create(vertx);
        }

        @Override
        public Vertx getVertx() {
            return abstractApplication.vertx;
        }

        @Override
        public void registerHealthCheck(String name, Handler<Future<Status>> procedure) {
            route.handler(handler.register(name, procedure));
        }

        @Override
        public String getPath() {
            return abstractApplication.getPath();
        }
    }

}
