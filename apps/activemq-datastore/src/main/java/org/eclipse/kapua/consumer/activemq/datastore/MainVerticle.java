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

import java.util.Optional;

import org.eclipse.kapua.broker.client.amqp.ClientOptions;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.eclipse.kapua.commons.core.vertx.AbstractMainVerticle;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.connector.activemq.AmqpTransportActiveMQConnector;
import org.eclipse.kapua.consumer.activemq.datastore.settings.ActiveMQDatastoreSettings;
import org.eclipse.kapua.consumer.activemq.datastore.settings.ActiveMQDatastoreSettingsKey;
import org.eclipse.kapua.converter.kura.KuraPayloadProtoConverter;
import org.eclipse.kapua.processor.datastore.DatastoreProcessor;
import org.eclipse.kapua.processor.error.amqp.activemq.ErrorProcessor;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

import io.vertx.core.Future;
import io.vertx.ext.healthchecks.Status;
import io.vertx.proton.ProtonQoS;

public class MainVerticle extends AbstractMainVerticle {

    protected final static Logger logger = LoggerFactory.getLogger(MainVerticle.class);
    private final static String HEALTH_NAME_CONNECTOR = "ActiveMQ-connector";
    private final static String HEALTH_NAME_DATASTORE = "Datastore-processor";
    private final static String HEALTH_NAME_ERROR = "Error-processor";

    private ClientOptions connectorOptions;
    private AmqpTransportActiveMQConnector connector;
    private KuraPayloadProtoConverter converter;
    private DatastoreProcessor processor;
    private ClientOptions errorOptions;
    private ErrorProcessor errorProcessor;

    public MainVerticle() {
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
    }

    @Override
    protected void internalStart(Future<Void> future) throws Exception {
        XmlUtil.setContextProvider(new JAXBContextProvider());
        logger.info("Instantiating Datastore Consumer...");
        logger.info("Instantiating Datastore Consumer... initializing KuraPayloadProtoConverter");
        converter = new KuraPayloadProtoConverter();
        logger.info("Instantiating Datastore Consumer... initializing DataStoreProcessor");
        processor = new DatastoreProcessor(vertx);
        logger.info("Instantiating Datastore Consumer... initializing ErrorProcessor");
        errorProcessor = new ErrorProcessor(vertx, errorOptions);
        logger.info("Instantiating Datastore Consumer... instantiating AmqpActiveMQConnector");
        connector = new AmqpTransportActiveMQConnector(vertx, connectorOptions, converter, processor, errorProcessor);
        logger.info("Instantiating Datastore Consumer... DONE");
        vertx.deployVerticle(connector, ar -> {
            if (ar.succeeded()) {
                future.complete();
            }
            else {
                future.fail(ar.cause());
            }
        });
        getEnvironment().getHttpRestService().registerHealthCheckProvider(handler -> {
            handler.register(HEALTH_NAME_CONNECTOR, hcm -> {
                if (connector.getStatus().isOk()) {
                    hcm.complete(Status.OK());
                }
                else {
                    hcm.complete(Status.KO());
                }
            });
        });
        getEnvironment().getHttpRestService().registerHealthCheckProvider(handler -> {
            handler.register(HEALTH_NAME_DATASTORE, hcm -> {
                if (processor.getStatus().isOk()) {
                    hcm.complete(Status.OK());
                }
                else {
                    hcm.complete(Status.KO());
                }
            });
        });
        getEnvironment().getHttpRestService().registerHealthCheckProvider(handler -> {
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
        //TODO stop connectors
        future.complete();
    }

}
