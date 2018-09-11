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
package org.eclipse.kapua.consumer.activemq.lifecycle;

//import java.util.Optional;
import javax.inject.Inject;

import org.eclipse.kapua.apps.api.MessageConsumerServer;
import org.eclipse.kapua.apps.api.MessageConsumerServerConfig;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.AbstractMainVerticle;
import org.eclipse.kapua.commons.core.vertx.EBHealthCheckProvider;
import org.eclipse.kapua.commons.core.vertx.HealthCheckProvider;
//import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
//import org.eclipse.kapua.commons.setting.system.SystemSetting;
//import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.core.vertx.HttpRestServer;
import org.eclipse.kapua.message.transport.TransportMessage;
//import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.google.common.base.MoreObjects;

import io.vertx.core.Future;

public class MainVerticle extends AbstractMainVerticle {

    protected final static Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    @Inject
    private HttpRestServer httpRestServer;

    @Inject
    private ObjectFactory<MessageConsumerServerConfig<byte[], TransportMessage>> amqpConnectorServerConfigFactory;

    @Override
    protected void internalStart(Future<Void> startFuture) throws Exception {

        logger.info("Starting Datastore Consumer... DONE");

        Future.succeededFuture().compose(map -> {
            Future<Void> future = Future.future();
            MessageConsumerServerConfig<byte[], TransportMessage> amqpConnectorServerConfig = amqpConnectorServerConfigFactory.create();
            MessageConsumerServer<byte[], TransportMessage> amqpConnectorServer = amqpConnectorServerConfig.build();

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
                startFuture.complete();
            } else {
                startFuture.fail(result.cause());
            }
        });
    }

    @Override
    protected void internalStop(Future<Void> future) throws Exception {
        //do nothing
        logger.info("Closing Lifecycle Consumer...");
        future.complete();
        logger.info("Closing Lifecycle Consumer... DONE");
        //this stop call is no more needed since the connector is a verticle then is already stopped during the vertx.stop call
        //connector.stop(future);
    }

}
