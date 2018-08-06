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
package org.eclipse.kapua.consumer.activemq.datastore;

import java.util.HashMap;
import java.util.Map;

//import java.util.Optional;
import javax.inject.Inject;

import org.eclipse.kapua.broker.client.amqp.ClientOptions;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.eclipse.kapua.commons.core.vertx.AbstractMainVerticle;
import org.eclipse.kapua.commons.core.vertx.HttpRestServer;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.connector.activemq.AmqpTransportActiveMQConnector;
import org.eclipse.kapua.consumer.activemq.datastore.settings.ActiveMQDatastoreSettings;
import org.eclipse.kapua.consumer.activemq.datastore.settings.ActiveMQDatastoreSettingsKey;
import org.eclipse.kapua.converter.kura.KuraPayloadProtoConverter;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.message.transport.TransportMessageType;
import org.eclipse.kapua.processor.Processor;
import org.eclipse.kapua.processor.datastore.DatastoreProcessor;
import org.eclipse.kapua.processor.error.amqp.activemq.ErrorProcessor;
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
    private final static String HEALTH_NAME_DATASTORE = "Datastore-processor";
    private final static String HEALTH_NAME_ERROR = "Error-processor";

    private final static String PROCESSOR_NAME_DATASTORE = "Datastore";

    protected Map<String, Processor<TransportMessage>> processorMap;
    protected Map<String, Processor> errorProcessorMap;

    private ClientOptions connectorOptions;
    private AmqpTransportActiveMQConnector connector;
    private KuraPayloadProtoConverter converter;
    private DatastoreProcessor processor;
    private ClientOptions errorOptions;
    protected ErrorProcessor errorProcessor;

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
                ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.CONNECTION_HOST),
                ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.CONNECTION_PORT),
                ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.CONNECTION_USERNAME),
                ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.CONNECTION_PASSWORD));
        connectorOptions.put(AmqpClientOptions.AUTO_ACCEPT, false);
        connectorOptions.put(AmqpClientOptions.QOS, ProtonQoS.AT_LEAST_ONCE);
        connectorOptions.put(AmqpClientOptions.CLIENT_ID, ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.TELEMETRY_CLIENT_ID));
        connectorOptions.put(AmqpClientOptions.DESTINATION, ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.TELEMETRY_DESTINATION));
        connectorOptions.put(AmqpClientOptions.CONNECT_TIMEOUT, ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.CONNECT_TIMEOUT));
        connectorOptions.put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.MAX_RECONNECTION_ATTEMPTS));
        connectorOptions.put(AmqpClientOptions.IDLE_TIMEOUT, ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.IDLE_TIMEOUT));
        connectorOptions.put(AmqpClientOptions.PREFETCH_MESSAGES, ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.TELEMETRY_PREFETCH_MESSAGES));
        errorOptions = new ClientOptions();
        errorOptions = new ClientOptions(
                ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.CONNECTION_HOST),
                ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.CONNECTION_PORT),
                ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.CONNECTION_USERNAME),
                ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.CONNECTION_PASSWORD));
        errorOptions.put(AmqpClientOptions.AUTO_ACCEPT, false);
        errorOptions.put(AmqpClientOptions.QOS, ProtonQoS.AT_LEAST_ONCE);
        errorOptions.put(AmqpClientOptions.CLIENT_ID, ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.ERROR_CLIENT_ID));
        errorOptions.put(AmqpClientOptions.DESTINATION, ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.ERROR_DESTINATION));
        errorOptions.put(AmqpClientOptions.CONNECT_TIMEOUT, ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.CONNECT_TIMEOUT));
        errorOptions.put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.MAX_RECONNECTION_ATTEMPTS));
        errorOptions.put(AmqpClientOptions.IDLE_TIMEOUT, ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.IDLE_TIMEOUT));
        errorOptions.put(AmqpClientOptions.PREFETCH_MESSAGES, ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.ERROR_PREFETCH_MESSAGES));
        processorMap = new HashMap<>();
        errorProcessorMap = new HashMap<>();
    }

    protected void initializeProcessors() {
        XmlUtil.setContextProvider(new JAXBContextProvider());
        logger.info("Starting Datastore Consumer...");
        logger.info("Starting Datastore Consumer... Starting KuraPayloadProtoConverter");
        converter = new KuraPayloadProtoConverter();
        logger.info("Starting Datastore Consumer... Starting DataStoreProcessor");
        processor = DatastoreProcessor.getProcessorWithNoFilter(vertx);//telemetry are already filtered by connector
        logger.info("Starting Datastore Consumer... Starting ErrorProcessor");
        errorProcessor = ErrorProcessor.getProcessorWithNoFilter(vertx, errorOptions);
        logger.info("Starting Datastore Consumer... Starting AmqpActiveMQConnector");
        processorMap.put(PROCESSOR_NAME_DATASTORE, processor);
        errorProcessorMap.put(PROCESSOR_NAME_DATASTORE, errorProcessor);
    }

    @Override
    protected void internalStart(Future<Void> future) throws Exception {
        initializeProcessors();
        connector = new AmqpTransportActiveMQConnector(vertx, connectorOptions, converter, processorMap, errorProcessorMap) {

            @Override
            protected boolean isProcessDestination(MessageContext<byte[]> message) {
                return TransportMessageType.TELEMETRY.equals(message.getProperties().get(Properties.MESSAGE_TYPE));
            }

        };
        logger.info("Starting Datastore Consumer... DONE");
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
            handler.register(HEALTH_NAME_DATASTORE, hcm -> {
                if (processor.getStatus().isOk()) {
                    hcm.complete(Status.OK());
                }
                else {
                    hcm.complete(Status.KO());
                }
            });
        });
        httpRestServer.registerHealthCheckProvider(handler -> {
            handler.register(HEALTH_NAME_ERROR, hcm -> {
                if (errorProcessor.getStatus().isOk()) {
                    hcm.complete(Status.OK());
                }
                else {
                    hcm.complete(Status.KO());
                }
            });
        });
    }

    @Override
    protected void internalStop(Future<Void> future) throws Exception {
        //do nothing
        logger.info("Stopping Datastore Consumer...");
        future.complete();
        logger.info("Stopping Datastore Consumer... DONE");
        //this stop call is no more needed since the connector is a verticle then is already stopped during the vertx.stop call
        //connector.stop(future);
    }

}
