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
package org.eclipse.kapua.consumer.hono;

import java.util.Optional;
import java.util.logging.Level;

import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.connector.hono.AmqpHonoConnector;
import org.eclipse.kapua.converter.kura.KuraPayloadProtoConverter;
import org.eclipse.kapua.processor.datastore.DatastoreProcessor;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Hono AMQP consumer with Kura payload converter and Kapua data store ingestion
 *
 */
public class HonoConsumer extends AbstractVerticle {

    protected final static Logger logger = LoggerFactory.getLogger(HonoConsumer.class);

    static {
        SystemSetting configSys = SystemSetting.getInstance();
        logger.info("Checking database... '{}'", configSys.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE));
        if(configSys.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
            logger.debug("Starting Liquibase embedded client.");
            String dbUsername = configSys.getString(SystemSettingKey.DB_USERNAME);
            String dbPassword = configSys.getString(SystemSettingKey.DB_PASSWORD);
            String schema = MoreObjects.firstNonNull(configSys.getString(SystemSettingKey.DB_SCHEMA_ENV), configSys.getString(SystemSettingKey.DB_SCHEMA));
            new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();
        }
    }

    private AmqpHonoConnector connectorVerticle;
    private KuraPayloadProtoConverter converter;
    private DatastoreProcessor processor;

    @Override
    public void start() throws Exception {
        Future<Void> future = Future.future();
        //disable Vertx BlockedThreadChecker log
        java.util.logging.Logger.getLogger("io.vertx.core.impl.BlockedThreadChecker").setLevel(Level.OFF);
        XmlUtil.setContextProvider(new HonoConsumerJAXBContextProvider());
        logger.info("Instantiating HonoConsumer...");
        logger.info("Instantiating HonoConsumer... initializing KuraPayloadProtoConverter");
        converter = new KuraPayloadProtoConverter();
        logger.info("Instantiating HonoConsumer... initializing DataStoreProcessor");
        processor = new DatastoreProcessor();
        logger.info("Instantiating HonoConsumer... instantiating AmqpHonoConnector");
        connectorVerticle = new AmqpHonoConnector(vertx, converter, processor);
        logger.info("Instantiating HonoConsumer... DONE");
        connectorVerticle.start(future);
    }

    @Override
    public void stop() throws Exception {
        Future<Void> future = Future.future();
        if (connectorVerticle!=null) {
            connectorVerticle.stop(future);
        }
    }

}
