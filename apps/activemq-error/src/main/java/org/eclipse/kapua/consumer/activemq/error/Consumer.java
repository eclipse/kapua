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
package org.eclipse.kapua.consumer.activemq.error;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.apps.api.AbstractApplication;
import org.eclipse.kapua.apps.api.ApplicationContext;
import org.eclipse.kapua.broker.client.amqp.ClientOptions;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.activemq.AmqpActiveMQConnector;
import org.eclipse.kapua.consumer.activemq.error.settings.ActiveMQErrorSettings;
import org.eclipse.kapua.consumer.activemq.error.settings.ActiveMQErrorSettingsKey;
import org.eclipse.kapua.processor.logger.LoggerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.ext.healthchecks.Status;
import io.vertx.proton.ProtonQoS;

/**
 * ActiveMQ AMQP consumer handling error messages
 *
 */
public class Consumer extends AbstractApplication {

    protected final static Logger logger = LoggerFactory.getLogger(Consumer.class);
    private final static String HEALTH_NAME_CONNECTOR = "ActiveMQ-connector";
    private final static String HEALTH_NAME_ERROR = "Error-processor";
    private final static String HEALTH_PATH = "/health/consumer/activemq/error";

    public static void main(String args[]) throws Exception {
        Consumer consumer = new Consumer();
        consumer.start(args);
    }

    private ClientOptions connectorOptions;
    private AmqpActiveMQConnector connector;
    private LoggerProcessor processor;

    protected Consumer() {
        super(HEALTH_PATH);
        //init options
        connectorOptions = new ClientOptions(
                ActiveMQErrorSettings.getInstance().getString(ActiveMQErrorSettingsKey.CONNECTION_HOST),
                ActiveMQErrorSettings.getInstance().getInt(ActiveMQErrorSettingsKey.CONNECTION_PORT),
                ActiveMQErrorSettings.getInstance().getString(ActiveMQErrorSettingsKey.CONNECTION_USERNAME),
                ActiveMQErrorSettings.getInstance().getString(ActiveMQErrorSettingsKey.CONNECTION_PASSWORD));
        connectorOptions.put(AmqpClientOptions.AUTO_ACCEPT, false);
        connectorOptions.put(AmqpClientOptions.QOS, ProtonQoS.AT_LEAST_ONCE);
        connectorOptions.put(AmqpClientOptions.CLIENT_ID, ActiveMQErrorSettings.getInstance().getString(ActiveMQErrorSettingsKey.CLIENT_ID));
        connectorOptions.put(AmqpClientOptions.DESTINATION, ActiveMQErrorSettings.getInstance().getString(ActiveMQErrorSettingsKey.DESTINATION));
        connectorOptions.put(AmqpClientOptions.CONNECT_TIMEOUT, ActiveMQErrorSettings.getInstance().getInt(ActiveMQErrorSettingsKey.CONNECT_TIMEOUT));
        connectorOptions.put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, ActiveMQErrorSettings.getInstance().getInt(ActiveMQErrorSettingsKey.MAX_RECONNECTION_ATTEMPTS));
        connectorOptions.put(AmqpClientOptions.IDLE_TIMEOUT, ActiveMQErrorSettings.getInstance().getInt(ActiveMQErrorSettingsKey.IDLE_TIMEOUT));
    }

    @Override
    protected CompletableFuture<String> internalStart(ApplicationContext applicationContext) throws Exception {
        CompletableFuture<String> startFuture = new CompletableFuture<>();
        //disable Vertx BlockedThreadChecker log
        java.util.logging.Logger.getLogger("io.vertx.core.impl.BlockedThreadChecker").setLevel(Level.OFF);
        XmlUtil.setContextProvider(new JAXBContextProvider());
        logger.info("Instantiating ErrorLogger Consumer...");
        logger.info("Instantiating ErrorLogger Consumer... initializing ErrorLogger");
        processor = new LoggerProcessor();
        logger.info("Instantiating LoggerProcessor Consumer... instantiating AmqpActiveMQConnector");
        connector = new AmqpActiveMQConnector(applicationContext.getVertx(), connectorOptions, processor) {

            @Override
            protected boolean isProcessDestination(MessageContext<Message> message) {
                return true;
            }

        };
        logger.info("Instantiating LoggerProcessor Consumer... DONE");
        applicationContext.getVertx().deployVerticle(connector, ar -> {
            if (ar.succeeded()) {
                startFuture.complete(ar.result());
            }
            else {
                startFuture.completeExceptionally(ar.cause());
            }
        });
        applicationContext.registerHealthCheck(HEALTH_NAME_CONNECTOR, hcm -> {
            if (connector.getStatus().isOk()) {
                hcm.complete(Status.OK());
            }
            else {
                hcm.complete(Status.KO());
            }
        });
        applicationContext.registerHealthCheck(HEALTH_NAME_ERROR, hcm -> {
            if (processor.getStatus().isOk()) {
                hcm.complete(Status.OK());
            }
            else {
                hcm.complete(Status.KO());
            }
        });
        return startFuture;
    }

    @Override
    protected CompletableFuture<String> internalStop(ApplicationContext applicationContext) throws Exception {
        CompletableFuture<String> stopFuture = new CompletableFuture<>();
        stopFuture.complete("Application stopped!");
        return stopFuture;
    }

}
