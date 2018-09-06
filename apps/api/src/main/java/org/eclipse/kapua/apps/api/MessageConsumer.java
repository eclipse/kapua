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
package org.eclipse.kapua.apps.api;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.commons.core.vertx.AbstractEBServer;
import org.eclipse.kapua.commons.core.vertx.EBServerConfig;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.connector.AbstractMessageProcessor;
import org.eclipse.kapua.connector.MessageSource;
import org.eclipse.kapua.connector.Converter;
import org.eclipse.kapua.connector.MessageTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;

// TODO move to common project
public class MessageConsumer<M,P> extends AbstractEBServer {

    protected final static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    private final static String PROCESSOR_NAME_DATASTORE = "Datastore";

    protected Map<String, MessageTarget<P>> processorMap;
    protected Map<String, MessageTarget> errorProcessorMap;

    private MessageSource<M> consumer;
    private Converter<M,P> converter;
    private MessageTarget<P> processor;
    private MessageTarget errorProcessor;
    private AbstractMessageProcessor<M,P> connectorVerticle;
    private JAXBContextProvider jaxbContextProvider;

    public static interface Builder<M,P> {

        MessageSource<M> getConsumer();

        Converter<M,P> getConverter();

        MessageTarget<P> getProcessor();

        MessageTarget getErrorProcessor();

        String getHealthCheckEBAddress();

        String getEBAddress();

        JAXBContextProvider getJAXBContextProvider();

        default MessageConsumer<M,P> build() {
            MessageConsumer<M,P> server = new MessageConsumer<M,P>();
            server.consumer = this.getConsumer();
            server.converter = this.getConverter();
            server.processor = this.getProcessor();
            server.errorProcessor = this.getErrorProcessor();
            server.healthCheckEBAddress = this.getHealthCheckEBAddress();
            server.ebAddress = this.getEBAddress();
            return server;
        }
    }

    private String healthCheckEBAddress;
    private String ebAddress;
    private EBServerConfig ebServerConfig;

    public MessageConsumer() {
//      SystemSetting configSys = SystemSetting.getInstance();
//      logger.info("Checking database... '{}'", configSys.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false));
//      if(configSys.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
//          logger.debug("Starting Liquibase embedded client.");
//          String dbUsername = configSys.getString(SystemSettingKey.DB_USERNAME);
//          String dbPassword = configSys.getString(SystemSettingKey.DB_PASSWORD);
//          String schema = MoreObjects.firstNonNull(configSys.getString(SystemSettingKey.DB_SCHEMA_ENV), configSys.getString(SystemSettingKey.DB_SCHEMA));
//          new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();
//      }
      //init options
    }

    public void start(Future<Void> startFuture) throws Exception {
        super.start();
        logger.info("Starting Datastore Consumer...");

        initializeProcessors();
        connectorVerticle.start(startFuture);
    }

    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop();
        stopFuture.complete();
    }

    @Override
    public EBServerConfig getConfigs() {

        if (ebServerConfig == null) {
            ebServerConfig = new EBServerConfig();
            ebServerConfig.setAddress(ebAddress);
            ebServerConfig.setHealthCheckAddress(healthCheckEBAddress);
        }
        return this.ebServerConfig;
    }

    private void initializeProcessors() {
        XmlUtil.setContextProvider(jaxbContextProvider);
        processorMap = new HashMap<>();
        processorMap.put(PROCESSOR_NAME_DATASTORE, processor);
        errorProcessorMap = new HashMap<>();
        errorProcessorMap.put(PROCESSOR_NAME_DATASTORE, errorProcessor);
        connectorVerticle = new AbstractMessageProcessor<>(consumer, converter, processorMap, errorProcessorMap);
    }
}
