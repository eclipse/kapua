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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

//import java.util.Optional;

import javax.inject.Inject;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.broker.client.amqp.ClientOptions;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.eclipse.kapua.commons.core.vertx.AbstractMainVerticle;
//import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
//import org.eclipse.kapua.commons.setting.system.SystemSetting;
//import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.core.vertx.HttpRestServer;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.activemq.AmqpActiveMQConnector;
import org.eclipse.kapua.consumer.activemq.error.settings.ActiveMQErrorSettings;
import org.eclipse.kapua.consumer.activemq.error.settings.ActiveMQErrorSettingsKey;
import org.eclipse.kapua.processor.Processor;
import org.eclipse.kapua.processor.logger.LoggerProcessor;
//import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.google.common.base.MoreObjects;

import io.vertx.core.Future;
import io.vertx.ext.healthchecks.Status;
import io.vertx.proton.ProtonQoS;

public class MainVerticle extends AbstractMainVerticle {

    protected final static Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    private final static String HEALTH_NAME_CONNECTOR = "ActiveMQ-connector";
    private final static String HEALTH_NAME_ERROR = "Error-processor";

    private final static String PROCESSOR_NAME_ERROR = "Error";

    protected Map<String, Processor<Message>> processorMap;

    private ClientOptions connectorOptions;
    private AmqpActiveMQConnector connector;
    private LoggerProcessor processor;

    @Inject
    private HttpRestServer httpRestServer;

    public MainVerticle() {
//        SystemSetting configSys = SystemSetting.getInstance();
//        logger.info("Checking database... '{}'", configSys.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false));
//        if(configSys.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
//            logger.debug("Starting Liquibase embedded client.");
//            String dbUsername = configSys.getString(SystemSettingKey.DB_USERNAME);
//            String dbPassword = configSys.getString(SystemSettingKey.DB_PASSWORD);
//            String schema = MoreObjects.firstNonNull(configSys.getString(SystemSettingKey.DB_SCHEMA_ENV), configSys.getString(SystemSettingKey.DB_SCHEMA));
//            new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();
//        }
        //init options
        connectorOptions = new ClientOptions(
                ActiveMQErrorSettings.getInstance().getString(ActiveMQErrorSettingsKey.CONNECTION_HOST),
                ActiveMQErrorSettings.getInstance().getInt(ActiveMQErrorSettingsKey.CONNECTION_PORT),
                ActiveMQErrorSettings.getInstance().getString(ActiveMQErrorSettingsKey.CONNECTION_USERNAME),
                ActiveMQErrorSettings.getInstance().getString(ActiveMQErrorSettingsKey.CONNECTION_PASSWORD));
        connectorOptions.put(AmqpClientOptions.AUTO_ACCEPT, false);
        connectorOptions.put(AmqpClientOptions.PREFETCH_MESSAGES, ActiveMQErrorSettings.getInstance().getInt(ActiveMQErrorSettingsKey.PREFETCH_MESSAGES));
        connectorOptions.put(AmqpClientOptions.QOS, ProtonQoS.AT_LEAST_ONCE);
        connectorOptions.put(AmqpClientOptions.CLIENT_ID, ActiveMQErrorSettings.getInstance().getString(ActiveMQErrorSettingsKey.CLIENT_ID));
        connectorOptions.put(AmqpClientOptions.DESTINATION, ActiveMQErrorSettings.getInstance().getString(ActiveMQErrorSettingsKey.DESTINATION));
        connectorOptions.put(AmqpClientOptions.CONNECT_TIMEOUT, ActiveMQErrorSettings.getInstance().getInt(ActiveMQErrorSettingsKey.CONNECT_TIMEOUT));
        connectorOptions.put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, ActiveMQErrorSettings.getInstance().getInt(ActiveMQErrorSettingsKey.MAX_RECONNECTION_ATTEMPTS));
        connectorOptions.put(AmqpClientOptions.IDLE_TIMEOUT, ActiveMQErrorSettings.getInstance().getInt(ActiveMQErrorSettingsKey.IDLE_TIMEOUT));
        //disable Vertx BlockedThreadChecker log
        java.util.logging.Logger.getLogger("io.vertx.core.impl.BlockedThreadChecker").setLevel(Level.OFF);
        XmlUtil.setContextProvider(new JAXBContextProvider());
        logger.info("Starting ErrorLogger Consumer...");
        logger.info("Starting ErrorLogger Consumer... Starting ErrorLogger");
        processor = LoggerProcessor.getProcessorWithNoFilter();
        logger.info("Starting LoggerProcessor Consumer... Starting AmqpActiveMQConnector");
        processorMap = new HashMap<>();
        processorMap.put(PROCESSOR_NAME_ERROR, processor);
    }

    @Override
    protected void internalStart(Future<Void> future) throws Exception {
        connector = new AmqpActiveMQConnector(vertx, connectorOptions, processorMap, null) {

            @Override
            protected boolean isProcessDestination(MessageContext<Message> message) {
                return true;
            }

        };
        vertx.deployVerticle(connector, ar -> {
            if (ar.succeeded()) {
                future.complete();
            }
            else {
                future.fail(ar.cause());
            }
        });
        httpRestServer.registerHealthCheckProvider(handler -> {
            handler.register(HEALTH_NAME_CONNECTOR, hcm -> {
                if (connector.getStatus().isOk()) {
                    hcm.complete(Status.OK());
                }
                else {
                    hcm.complete(Status.KO());
                }
            });
        });
        httpRestServer.registerHealthCheckProvider(handler -> {
            handler.register(HEALTH_NAME_ERROR, hcm -> {
                if (processor.getStatus().isOk()) {
                    hcm.complete(Status.OK());
                }
                else {
                    hcm.complete(Status.KO());
                }
            });
        });
        logger.info("Starting LoggerProcessor Consumer... DONE");
    }

    @Override
    protected void internalStop(Future<Void> future) throws Exception {
        //do nothing
        logger.info("Stopping ErrorLogger Consumer...");
        future.complete();
        logger.info("Stopping ErrorLogger Consumer... DONE");
        //this stop call is no more needed since the connector is a verticle then is already stopped during the vertx.stop call
        //connector.stop(future);
    }

}
