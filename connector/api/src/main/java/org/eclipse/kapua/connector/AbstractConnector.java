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

import io.vertx.core.Future;
import io.vertx.core.Vertx;

import org.eclipse.kapua.converter.Converter;
import org.eclipse.kapua.converter.KapuaConverterException;
import org.eclipse.kapua.processor.KapuaProcessorException;
import org.eclipse.kapua.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract connector to be customized with specific server connection code.<br>
 * The incoming message will flow through the converter (if provided) and the processor
 * 
 * @param <M> Message type (optional)
 * @param <P> Processor message type
 */
public abstract class AbstractConnector<M, P> {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractConnector.class);

    protected Vertx vertx;
    protected Converter<M, P> converter;
    protected Processor<P> processor;

    /**
     * Default protected constructor
     * @param Vertx instance
     * @param converter message converter instance
     * @param processor message processor instance
     */
    protected AbstractConnector(Vertx vertx, Converter<M, P> converter, Processor<P> processor) {
        this.converter = converter;
        this.processor = processor;
        this.vertx = vertx;
    }

    /**
     * Constructor with no message converter
     * @param Vertx instance
     * @param processor
     */
    protected AbstractConnector(Vertx vertx, Processor<P> processor) {
        this(vertx, null, processor);
    }

    /**
     * Internal components start hook
     * @param startFuture
     * @throws KapuaConnectorException
     */
    protected abstract void startInternal(Future<Void> startFuture) throws KapuaConnectorException;

    /**
     * Internal components stop hook
     * @param stopFuture
     * @throws KapuaConnectorException
     */
    protected abstract void stopInternal(Future<Void> stopFuture) throws KapuaConnectorException;

    /**
     * 
     * @param message
     * @return
     * @throws KapuaConverterException
     */
    protected abstract MessageContext<M> convert(MessageContext<?> message) throws KapuaConverterException;

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
    protected void handleMessage(MessageContext<?> message) throws KapuaConnectorException, KapuaConverterException, KapuaProcessorException {
        MessageContext<M> msg = convert(message);
        MessageContext<P> convertedMessage = null;
        if (converter != null) {
            convertedMessage = converter.convert(msg);
        } else {
            convertedMessage = (MessageContext<P>) msg;
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
