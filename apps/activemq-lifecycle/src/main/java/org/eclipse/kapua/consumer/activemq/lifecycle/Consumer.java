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

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.eclipse.kapua.apps.api.AbstractApplication;
import org.eclipse.kapua.apps.api.ApplicationContext;
import org.eclipse.kapua.broker.client.amqp.ClientOptions;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.activemq.AmqpTransportActiveMQConnector;
import org.eclipse.kapua.consumer.activemq.lifecycle.settings.ActiveMQLifecycleSettings;
import org.eclipse.kapua.consumer.activemq.lifecycle.settings.ActiveMQLifecycleSettingsKey;
import org.eclipse.kapua.converter.Converter;
import org.eclipse.kapua.converter.kura.KuraPayloadProtoConverter;
import org.eclipse.kapua.processor.error.amqp.activemq.ErrorProcessor;
import org.eclipse.kapua.processor.lifecycle.LifecycleProcessor;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

import io.vertx.ext.healthchecks.Status;
import io.vertx.proton.ProtonQoS;

/**
 * ActiveMQ AMQP consumer with Kura payload converter and Kapua lifecycle manager
 *
 */
public class Consumer extends AbstractApplication {

    protected final static Logger logger = LoggerFactory.getLogger(Consumer.class);
    private final static String HEALTH_NAME_CONNECTOR = "ActiveMQ-connector";
    private final static String HEALTH_NAME_LIFECYCLE = "Lifecycle-processor";
    private final static String HEALTH_NAME_ERROR = "Error-processor";
    private final static String HEALTH_PATH = "/health/consumer/activemq/lifecycle";

    public static void main(String args[]) throws Exception {
        Consumer consumer = new Consumer();
        consumer.start(args);
    }

    private ClientOptions connectorOptions;
    private AmqpTransportActiveMQConnector connector;
    private KuraPayloadProtoConverter converter;
    private LifecycleProcessor processor;
    private ClientOptions errorOptions;
    private ErrorProcessor errorProcessor;

    protected Consumer() {
        super(HEALTH_PATH);
        SystemSetting configSys = SystemSetting.getInstance();
        logger.info("Checking database... '{}'", configSys.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE));
        if(configSys.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
            logger.debug("Starting Liquibase embedded client.");
            String dbUsername = configSys.getString(SystemSettingKey.DB_USERNAME);
            String dbPassword = configSys.getString(SystemSettingKey.DB_PASSWORD);
            String schema = MoreObjects.firstNonNull(configSys.getString(SystemSettingKey.DB_SCHEMA_ENV), configSys.getString(SystemSettingKey.DB_SCHEMA));
            new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();
        }
        //init options
        connectorOptions = new ClientOptions(
                ActiveMQLifecycleSettings.getInstance().getString(ActiveMQLifecycleSettingsKey.CONNECTION_HOST),
                ActiveMQLifecycleSettings.getInstance().getInt(ActiveMQLifecycleSettingsKey.CONNECTION_PORT),
                ActiveMQLifecycleSettings.getInstance().getString(ActiveMQLifecycleSettingsKey.CONNECTION_USERNAME),
                ActiveMQLifecycleSettings.getInstance().getString(ActiveMQLifecycleSettingsKey.CONNECTION_PASSWORD));
        connectorOptions.put(AmqpClientOptions.CLIENT_ID, ActiveMQLifecycleSettings.getInstance().getString(ActiveMQLifecycleSettingsKey.TELEMETRY_CLIENT_ID));
        connectorOptions.put(AmqpClientOptions.DESTINATION, ActiveMQLifecycleSettings.getInstance().getString(ActiveMQLifecycleSettingsKey.TELEMETRY_DESTINATION));
        connectorOptions.put(AmqpClientOptions.CONNECT_TIMEOUT, ActiveMQLifecycleSettings.getInstance().getInt(ActiveMQLifecycleSettingsKey.CONNECT_TIMEOUT));
        connectorOptions.put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, ActiveMQLifecycleSettings.getInstance().getInt(ActiveMQLifecycleSettingsKey.MAX_RECONNECTION_ATTEMPTS));
        connectorOptions.put(AmqpClientOptions.IDLE_TIMEOUT, ActiveMQLifecycleSettings.getInstance().getInt(ActiveMQLifecycleSettingsKey.IDLE_TIMEOUT));
        connectorOptions.put(AmqpClientOptions.AUTO_ACCEPT, false);
        connectorOptions.put(AmqpClientOptions.QOS, ProtonQoS.AT_LEAST_ONCE);
        errorOptions = new ClientOptions();
        errorOptions = new ClientOptions(
                ActiveMQLifecycleSettings.getInstance().getString(ActiveMQLifecycleSettingsKey.CONNECTION_HOST),
                ActiveMQLifecycleSettings.getInstance().getInt(ActiveMQLifecycleSettingsKey.CONNECTION_PORT),
                ActiveMQLifecycleSettings.getInstance().getString(ActiveMQLifecycleSettingsKey.CONNECTION_USERNAME),
                ActiveMQLifecycleSettings.getInstance().getString(ActiveMQLifecycleSettingsKey.CONNECTION_PASSWORD));
        errorOptions.put(AmqpClientOptions.CLIENT_ID, ActiveMQLifecycleSettings.getInstance().getString(ActiveMQLifecycleSettingsKey.ERROR_CLIENT_ID));
        errorOptions.put(AmqpClientOptions.DESTINATION, ActiveMQLifecycleSettings.getInstance().getString(ActiveMQLifecycleSettingsKey.ERROR_DESTINATION));
        errorOptions.put(AmqpClientOptions.CONNECT_TIMEOUT, ActiveMQLifecycleSettings.getInstance().getInt(ActiveMQLifecycleSettingsKey.CONNECT_TIMEOUT));
        errorOptions.put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, ActiveMQLifecycleSettings.getInstance().getInt(ActiveMQLifecycleSettingsKey.MAX_RECONNECTION_ATTEMPTS));
        errorOptions.put(AmqpClientOptions.IDLE_TIMEOUT, ActiveMQLifecycleSettings.getInstance().getInt(ActiveMQLifecycleSettingsKey.IDLE_TIMEOUT));
        errorOptions.put(AmqpClientOptions.AUTO_ACCEPT, false);
        errorOptions.put(AmqpClientOptions.QOS, ProtonQoS.AT_LEAST_ONCE);
    }

    @Override
    protected CompletableFuture<String> internalStart(ApplicationContext applicationContext) throws Exception {
        CompletableFuture<String> startFuture = new CompletableFuture<>();
        //disable Vertx BlockedThreadChecker log
        java.util.logging.Logger.getLogger("io.vertx.core.impl.BlockedThreadChecker").setLevel(Level.OFF);
        XmlUtil.setContextProvider(new JAXBContextProvider());
        logger.info("Instantiating Lifecycle Consumer...");
        logger.info("Instantiating Lifecycle Consumer... initializing KuraPayloadProtoConverter");
        converter = new KuraPayloadProtoConverter();
        logger.info("Instantiating Lifecycle Consumer... initializing LifecycleProcessor");
        processor = new LifecycleProcessor();
        logger.info("Instantiating Lifecycle Consumer... initializing ErrorProcessor");
        errorProcessor = new ErrorProcessor(applicationContext.getVertx(), errorOptions);
        logger.info("Instantiating Lifecycle Consumer... instantiating AmqpActiveMQConnector");
        connector = new AmqpTransportActiveMQConnector(applicationContext.getVertx(), connectorOptions, converter, processor, errorProcessor) {

            @Override
            protected boolean isProcessDestination(MessageContext<byte[]> message) {
                String topic = (String) message.getProperties().get(Converter.MESSAGE_DESTINATION);
                if (topic!=null && (topic.endsWith("/MQTT/BIRTH") ||
                        topic.endsWith("/MQTT/DC") ||
                        topic.endsWith("/MQTT/LWT") ||
                        topic.endsWith("/MQTT/MISSING"))
                        ) {
                    return true;
                }
                else {
                    return false;
                }
            }

        };
        logger.info("Instantiating Lifecycle Consumer... DONE");
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
        applicationContext.registerHealthCheck(HEALTH_NAME_LIFECYCLE, hcm -> {
            if (processor.getStatus().isOk()) {
                hcm.complete(Status.OK());
            }
            else {
                hcm.complete(Status.KO());
            }
        });
        applicationContext.registerHealthCheck(HEALTH_NAME_ERROR, hcm -> {
            if (errorProcessor.getStatus().isOk()) {
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
