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
public class MessageConsumerServer<M,P> extends AbstractEBServer {

    protected final static Logger logger = LoggerFactory.getLogger(MessageConsumerServer.class);

    private final static String PROCESSOR_NAME_DATASTORE = "Datastore";

    protected Map<String, MessageTarget<P>> targetMap;
    protected Map<String, MessageTarget> errorTargetMap;

    private MessageSource<M> messageSource;
    private Converter<M,P> converter;
    private MessageTarget<P> messageTarget;
    private MessageTarget errorTarget;
    private AbstractMessageProcessor<M,P> messageProcessor;
    private JAXBContextProvider jaxbContextProvider;

    public static interface Builder<M,P> {

        MessageSource<M> getMessageSource();

        Converter<M,P> getConverter();

        MessageTarget<P> getMessageTarget();

        MessageTarget getErrorTarget();

        String getHealthCheckEBAddress();

        String getEBAddress();

        JAXBContextProvider getJAXBContextProvider();

        default MessageConsumerServer<M,P> build() {
            MessageConsumerServer<M,P> server = new MessageConsumerServer<M,P>();
            server.messageSource = this.getMessageSource();
            server.converter = this.getConverter();
            server.messageTarget = this.getMessageTarget();
            server.errorTarget = this.getErrorTarget();
            server.healthCheckEBAddress = this.getHealthCheckEBAddress();
            server.ebAddress = this.getEBAddress();
            return server;
        }
    }

    private String healthCheckEBAddress;
    private String ebAddress;
    private EBServerConfig ebServerConfig;

    public MessageConsumerServer() {
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
        messageProcessor.start(startFuture);
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
        targetMap = new HashMap<>();
        targetMap.put(PROCESSOR_NAME_DATASTORE, messageTarget);
        errorTargetMap = new HashMap<>();
        errorTargetMap.put(PROCESSOR_NAME_DATASTORE, errorTarget);
        messageProcessor = new AbstractMessageProcessor<>(messageSource, converter, targetMap, errorTargetMap);
    }
}
