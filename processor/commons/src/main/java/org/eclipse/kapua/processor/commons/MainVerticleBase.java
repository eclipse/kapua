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
            vertx.deployVerticle(messageProcessorVerticle, ar -> {
                if (ar.succeeded()) {
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
    public void internalStop(Future<Void> closeFuture) {
        logger.info("Closing Processor...");
        Future.succeededFuture()
        .compose(map -> {
            Future<Void> future = Future.future();
            vertx.executeBlocking(fut -> {
                try {
                    if (kapuaServiceCtx != null) {
                        kapuaServiceCtx.close();
                        kapuaServiceCtx = null;
                    }
                    fut.complete();
                } catch (KapuaException e) {
                    fut.fail(e);
                }
            }, ar -> {
                if (ar.succeeded()) {
                    future.complete();;
                }
                else {
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
                logger.info("Closing Processor...DONE");
                closeFuture.handle(Future.succeededFuture());
            }
            else {
                logger.info("Closing Processor...FAILED", ar.cause());
                closeFuture.handle(Future.failedFuture(ar.cause()));
            }
        });
    }

}
