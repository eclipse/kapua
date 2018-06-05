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
package org.eclipse.kapua.eclipseiot;

import java.util.Optional;
import java.util.logging.Level;

import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ClassUtil;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.connector.AbstractConnector;
import org.eclipse.kapua.converter.Converter;
import org.eclipse.kapua.eclipseiot.setting.MessageConsumerSetting;
import org.eclipse.kapua.eclipseiot.setting.MessageConsumerSettingKey;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.processor.Processor;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * 
 *
 */
public class MessageConsumer extends AbstractVerticle {

    protected final static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    private static final String VERTICLE_CLASS_NAME;
    private static final String CONVERTER_CLASS_NAME;
    private static final String PROCESSOR_CLASS_NAME;

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
        MessageConsumerSetting config = MessageConsumerSetting.getInstance();
        VERTICLE_CLASS_NAME = config.getString(MessageConsumerSettingKey.VERTICLE_CLASS_NAME);
        CONVERTER_CLASS_NAME = config.getString(MessageConsumerSettingKey.CONVERTER_CLASS_NAME);
        PROCESSOR_CLASS_NAME = config.getString(MessageConsumerSettingKey.PROCESSOR_CLASS_NAME);
    }

    private AbstractConnector<?, ?> connectorVerticle;
    private Converter<?, ?> converter;
    private Processor<TransportMessage> processor;

    @SuppressWarnings("unchecked")
    @Override
    public void start() throws Exception {
        Future<Void> future = Future.future();
        //disable Vertx BlockedThreadChecker log
        java.util.logging.Logger.getLogger("io.vertx.core.impl.BlockedThreadChecker").setLevel(Level.OFF);
        XmlUtil.setContextProvider(new MessageConsumerJAXBContextProvider());
        logger.info("Instantiating MessageConsumer...");
        logger.info("Instantiating MessageConsumer... initializing converter");
        converter = ClassUtil.newInstance(CONVERTER_CLASS_NAME, Converter.class);
        logger.info("Instantiating MessageConsumer... initializing processor");
        processor = ClassUtil.newInstance(PROCESSOR_CLASS_NAME, Processor.class);
        logger.info("Instantiating MessageConsumer... instantiating verticle");
        connectorVerticle = ClassUtil.newInstance(VERTICLE_CLASS_NAME, AbstractConnector.class,
                new Class[] {Vertx.class, Converter.class, Processor.class},
                new Object[]{vertx, converter, processor});
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
