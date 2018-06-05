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

import org.eclipse.kapua.converter.Converter;
import org.eclipse.kapua.converter.KapuaConverterException;
import org.eclipse.kapua.processor.KapuaProcessorException;
import org.eclipse.kapua.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * Abstract connector to be customized with specific server connection code.<br>
 * The incoming message will flow through the converter (if provided) and the processor
 * 
 * @param <M> Converter message type (optional)
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
     * @param converter message instance
     * @param processor processor instance
     */
    protected AbstractConnector(Vertx vertx, Converter<M, P> converter, Processor<P> processor) {
        this.converter = converter;
        this.processor = processor;
        this.vertx = vertx;
    }

    /**
     * Constructor with no message converter
     * @param Vertx instance
     * @param processor processor instance
     */
    protected AbstractConnector(Vertx vertx, Processor<P> processor) {
        this(vertx, null, processor);
    }

    /**
     * Internal components start hook
     * @param startFuture
     * @throws KapuaConnectorException
     */
    protected abstract void startInternal(Future<Void> startFuture);

    /**
     * Internal components stop hook
     * @param stopFuture
     * @throws KapuaConnectorException
     */
    protected abstract void stopInternal(Future<Void> stopFuture);

    /**
     * 
     * @param message
     * @return
     * @throws KapuaConverterException
     */
    protected abstract MessageContext<M> convert(MessageContext<?> message) throws KapuaConverterException;

    public void start(Future<Void> startFuture) throws KapuaConnectorException {
        logger.debug("Starting connector...");
        Future<Void> composerFuture = Future.future();
        composerFuture.compose(mapper -> {
            Future<Void> internalFuture = Future.future();
            startInternal(internalFuture);
            return internalFuture;
        })
        .setHandler(result -> {
            if (result.succeeded()) {
                logger.debug("Starting connector...DONE");
                startFuture.complete();
            } else {
                logger.warn("Starting connector...FAIL [message:{}]", result.cause().getMessage());
                logger.debug("\tException:", result.cause());
                startFuture.fail(result.cause());
            }
        });
        processor.start(composerFuture);
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
        logger.debug("Stopping connector...");
        Future<Void> composerFuture = Future.future();
        composerFuture.compose(mapper -> {
            Future<Void> internalFuture = Future.future();
            processor.stop(internalFuture);
            return internalFuture;
        })
        .setHandler(result -> {
            if (result.succeeded()) {
                logger.debug("Stopping connector...DONE");
                stopFuture.complete();
            } else {
                logger.warn("Stopping connector...FAIL [message:{}]", result.cause().getMessage());
                stopFuture.fail(result.cause());
            }
        });
        stopInternal(composerFuture);
    }
}
