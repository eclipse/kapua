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
package org.eclipse.kapua.processor.datastore.broker;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.AbstractMainVerticle;
import org.eclipse.kapua.commons.core.vertx.EBHealthCheckProvider;
import org.eclipse.kapua.commons.core.vertx.HealthCheckProvider;
import org.eclipse.kapua.commons.core.vertx.HttpRestServer;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.processor.commons.KapuaServiceContext;
import org.eclipse.kapua.processor.commons.MessageProcessorServer;
import org.eclipse.kapua.processor.commons.MessageProcessorServerConfig;
//import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.google.common.base.MoreObjects;

import io.vertx.core.Future;

public class MainVerticle extends AbstractMainVerticle {

    protected final static Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    private KapuaServiceContext kapuaServiceCtx;

    @Inject
    private JAXBContextProvider jaxbContextProvider;

    @Inject
    private HttpRestServer httpRestServer;

    @Inject
    private ObjectFactory<MessageProcessorServerConfig<byte[], TransportMessage>> amqpConnectorServerConfigFactory;

    @Override
    protected void internalStart(Future<Void> startFuture) throws Exception {

        logger.info("Starting Datastore Processor...");

        Future<Void> superFuture = Future.future();
        vertx.executeBlocking(fut -> {
            try {
               kapuaServiceCtx = KapuaServiceContext.create(SystemSetting.getInstance(), jaxbContextProvider);
               fut.complete();
           } catch (KapuaException e) {
               fut.fail(e);
           }
        }, ar -> {
            if (ar.succeeded()) {
                superFuture.complete();;
            }
            else {
                superFuture.fail(ar.cause());;
            }
        });
        superFuture.compose(map -> {
            Future<Void> future = Future.future();
            MessageProcessorServerConfig<byte[], TransportMessage> amqpConnectorServerConfig = amqpConnectorServerConfigFactory.create();
            MessageProcessorServer<byte[], TransportMessage> amqpConnectorServer = amqpConnectorServerConfig.build();

            for(HealthCheckProvider provider:amqpConnectorServerConfig.getHealthCheckProviders()) {
                amqpConnectorServer.registerHealthCheckProvider(provider);
            }
            vertx.deployVerticle(amqpConnectorServer, ar -> {
                if (ar.succeeded()) {
                    future.complete();
                }
                else {
                    future.fail(ar.cause());
                }
            });
            return future;
        }).compose(map -> {
            Future<Void> future = Future.future();
            httpRestServer.registerHealthCheckProvider(EBHealthCheckProvider.create(vertx.eventBus(), "health"));
            vertx.deployVerticle(httpRestServer, ar -> {
                if (ar.succeeded()) {
                    future.complete();
                }
                else {
                    future.fail(ar.cause());
                }
            });
            return future;
        }).setHandler(result -> {
            if (result.succeeded()) {
                logger.info("Starting Datastore Processor...DONE");
                startFuture.complete();
            } else {
                logger.error("Starting Datastore Processor...FAILED", result.cause());
                startFuture.fail(result.cause());
            }
        });
    }

    @Override
    protected void internalStop(Future<Void> stopFuture) throws Exception {
        logger.info("Stopping Datastore Processor...");
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
                stopFuture.handle(Future.succeededFuture());
            }
            else {
                logger.info("Stopping Datastore Processor...FAILED", ar.cause());
                stopFuture.handle(Future.failedFuture(ar.cause()));
            }
        });
    }

}
