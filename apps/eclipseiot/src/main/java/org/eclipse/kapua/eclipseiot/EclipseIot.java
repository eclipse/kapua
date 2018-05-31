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

import java.util.logging.Level;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ClassUtil;
import org.eclipse.kapua.converter.Converter;
import org.eclipse.kapua.eclipseiot.setting.EclipseiotSetting;
import org.eclipse.kapua.eclipseiot.setting.EclipseiotSettingKey;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class EclipseIot extends AbstractVerticle {

    protected final static Logger logger = LoggerFactory.getLogger(EclipseIot.class);

    private static final String CONVERTER_CLASS_NAME;
    private static final String PROCESSOR_CLASS_NAME;

    static {
        EclipseiotSetting config = EclipseiotSetting.getInstance();
        CONVERTER_CLASS_NAME = config.getString(EclipseiotSettingKey.CONVERTER_CLASS_NAME);
        PROCESSOR_CLASS_NAME = config.getString(EclipseiotSettingKey.PROCESSOR_CLASS_NAME);
    }

    private EclipseIotVerticle iotVerticle;
    private Converter<byte[], TransportMessage> converter;
    private Processor<TransportMessage> processor;

    public static void main(String argv[]) throws KapuaException {
        VertxOptions options = new VertxOptions();
        options.setBlockedThreadCheckInterval(60 * 1000);
        // TODO more options?
        Vertx vertx = Vertx.vertx(options);
        EclipseIot eclipseIot = new EclipseIot();
        vertx.deployVerticle(eclipseIot.initialize());

//        logger.info("Waiting 30 seconds before stopping consumer");
//        try {
//            Thread.sleep(30000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        logger.info("Stopping consumer");

//        vertx.close();
    }

    @SuppressWarnings("unchecked")
    public EclipseIot() throws KapuaException {
        logger.info("Instantiating EclipseIot...");
        logger.info("Instantiating EclipseIot... initializing converter");
        converter = ClassUtil.newInstance(CONVERTER_CLASS_NAME, Converter.class);
        logger.info("Instantiating EclipseIot... initializing processor");
        processor = ClassUtil.newInstance(PROCESSOR_CLASS_NAME, Processor.class);
        logger.info("Instantiating EclipseIot...");
    }

    private EclipseIotVerticle initialize() {
        //disable Vertx BlockedThreadChecker log
        java.util.logging.Logger.getLogger("io.vertx.core.impl.BlockedThreadChecker").setLevel(Level.OFF);
        iotVerticle = new EclipseIotVerticle(vertx, converter, processor);
        return iotVerticle;
    }

    @Override
    public void start() throws Exception {
        Future<Void> future = Future.future();
        logger.info("Instantiating EclipseIot... initializing verticle");
        initialize();
        iotVerticle.startInternal(future);
    }

    @Override
    public void stop() throws Exception {
        Future<Void> future = Future.future();
        iotVerticle.stopInternal(future);
    }

}
