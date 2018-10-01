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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.AbstractMainVerticle;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
//import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.google.common.base.MoreObjects;

import io.vertx.core.Future;

public class MainVerticleBase<M,P> extends AbstractMainVerticle {

    protected final static Logger logger = LoggerFactory.getLogger(MainVerticleBase.class);

    @Inject
    private JAXBContextProvider jaxbContextProvider;

    private KapuaServiceContext kapuaServiceCtx;

    @Inject
    private HttpServiceVerticle httpServiceVerticle;

    @Inject
    private MessageProcessorVerticle messageProcessorVerticle;

    private String httpServiceVerticleId;
    private String messageProcessorverticleId;

    @Override
    protected void internalStart(Future<Void> startFuture) throws Exception {

        logger.info("Starting Processor...");

        Future.succeededFuture()
        .compose(map-> {
            Future<Void> future = Future.future();
            try {
                super.internalStart(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();            
            vertx.executeBlocking(fut -> {
                try {
                   kapuaServiceCtx = KapuaServiceContext.create(SystemSetting.getInstance(), jaxbContextProvider);
                   fut.complete();
               } catch (KapuaException e) {
                   fut.fail(e);
               }
            }, ar -> {
                if (ar.succeeded()) {
                    future.complete();;
                }
                else {
                    future.fail(ar.cause());;
                }
            });
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            vertx.deployVerticle(messageProcessorVerticle, ar -> {
                if (ar.succeeded()) {
                    messageProcessorverticleId = ar.result();
                    future.complete();
                }
                else {
                    future.fail(ar.cause());
                }
            });
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            vertx.deployVerticle(httpServiceVerticle, ar -> {
                if (ar.succeeded()) {
                    httpServiceVerticleId = ar.result();
                    future.complete();
                }
                else {
                    future.fail(ar.cause());
                }
            });
            return future;
        })
        .setHandler(result -> {
            if (result.succeeded()) {
                logger.info("Starting Processor...DONE");
                startFuture.complete();
            } else {
                logger.error("Starting Processor...FAILED", result.cause());
                startFuture.fail(result.cause());
            }
        });
    }

    @Override
    protected void internalStop(Future<Void> stopFuture) throws Exception {
        logger.info("Stopping Datastore Processor...");
        Future.succeededFuture()
        .compose(map -> {
            Future<Void> future = Future.future();
            if (httpServiceVerticleId != null) {
                vertx.undeploy(httpServiceVerticleId, ar -> {
                    if (ar.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(ar.cause());
                    }
                });
            } else {
                future.complete();
            }
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            if (messageProcessorverticleId != null) {
                vertx.undeploy(messageProcessorverticleId, ar -> {
                    if (ar.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(ar.cause());
                    }
                });
            } else {
                future.complete();
            }
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            vertx.executeBlocking(fut -> {
                try {
                    if (kapuaServiceCtx != null) {
                        kapuaServiceCtx.close();
                        kapuaServiceCtx = null;
                    }
                    stopFuture.complete();
                } catch (KapuaException e) {
                    stopFuture.fail(e);
                }
            }, ar -> {
                if (ar.succeeded()) {
                    logger.info("Stopping Datastore Processor...DONE");
                    future.complete();;
                }
                else {
                    logger.info("Stopping Datastore Processor...FAILED", ar.cause());
                    future.fail(ar.cause());
                }
            });
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            try {
                super.internalStop(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
        })
        .setHandler(ar -> {
            if (ar.succeeded()) {
                logger.info("Stopping Datastore Processor...DONE");
                stopFuture.handle(Future.succeededFuture());
            }
            else {
                logger.info("Stopping Datastore Processor...FAILED", ar.cause());
                stopFuture.handle(Future.failedFuture(ar.cause()));
            }
        });
    }

}
