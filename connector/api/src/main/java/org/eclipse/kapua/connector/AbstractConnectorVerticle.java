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
package org.eclipse.kapua.connector;

import java.util.Map;

import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;

public abstract class AbstractConnectorVerticle<S,T> extends AbstractVerticle {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractConnectorVerticle.class);

    protected Converter<S,T> converter;
    protected Processor<T> processor;

    protected AbstractConnectorVerticle(Converter<S,T> converter,
            Processor<T> processor) {
        this.converter = converter;
        this.processor = processor;
    }

    protected AbstractConnectorVerticle(Processor<T> processor) {
        this(null, processor);
    }

    protected abstract void startInternal(Future<Void> startFuture) throws KapuaConnectorException;

    protected abstract void stopInternal(Future<Void> stopFuture) throws KapuaConnectorException;

    public void start(Future<Void> startFuture) throws KapuaConnectorException {
        try {
            // Start subclass
            startInternal(startFuture);

            //Start processor
            logger.info("Invoking processor.start...");
            processor.start();

            startFuture.complete();
        } catch (Exception ex) {
            logger.warn("Verticle start failed", ex);
            startFuture.fail(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public void handleMessage(Map<String,Object> properties, S message) throws KapuaConnectorException {

        T convertedMessage = null;
        if (converter != null) {
            convertedMessage = converter.convert(properties, message);
        } else {
            convertedMessage = (T) message;
        }

        processor.process(convertedMessage);
    }

    public void stop(Future<Void> stopFuture) throws KapuaConnectorException {
        try {
            // Stop subclass
            stopInternal(stopFuture);

            // Stop processor
            logger.info("Invoking processor.stop...");
            processor.stop();

            stopFuture.complete();
        } catch (Exception ex) {
            logger.warn("Verticle stop failed", ex);
            stopFuture.fail(ex);
        }
    }
}
