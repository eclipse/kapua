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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.converter.Converter;
import org.eclipse.kapua.converter.KapuaConverterException;
import org.eclipse.kapua.processor.Processor;

import io.vertx.core.CompositeFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * Abstract connector to be customized with specific server connection code.<br>
 * The incoming message will flow through the converter (if provided) and the processor
 *
 * @param <M> Converter message type (optional)
 * @param <P> Processor message type
 */
public abstract class AbstractConnector<M, P> extends AbstractVerticle {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractConnector.class);

    protected Vertx vertx;
    protected Converter<M, P> converter;
    protected Map<String, Processor<P>> processorMap;
    @SuppressWarnings("rawtypes")
    protected Map<String, Processor> errorProcessorMap;
    private boolean connected;

    /**
     * Default protected constructor
     * @param vertx instance
     * @param converter message instance
     * @param processorMap processor map instances
     * @param errorProcessorMap error processor map instances (handles messages errors)
     */
    protected AbstractConnector(Vertx vertx, Converter<M, P> converter, Map<String, Processor<P>> processorMap, @SuppressWarnings("rawtypes") Map<String, Processor> errorProcessorMap) {
        this.converter = converter;
        this.processorMap = processorMap;
        this.vertx = vertx;
        if (errorProcessorMap != null) {
            this.errorProcessorMap = errorProcessorMap;
        }
        else {
            this.errorProcessorMap = new HashMap<>();
        }
    }

    /**
     * Default protected constructor
     * @param vertx instance
     * @param converter message instance
     * @param processorMap processor map instances
     */
    protected AbstractConnector(Vertx vertx, Converter<M, P> converter, Map<String, Processor<P>> processorMap) {
        this.converter = converter;
        this.processorMap = processorMap;
        this.vertx = vertx;
    }

    /**
     * Constructor with no message converter
     * @param vertx instance
     * @param processorMap processor map instances
     */
    protected AbstractConnector(Vertx vertx, Map<String, Processor<P>> processorMap) {
        this(vertx, null, processorMap, null);
    }

    /**
     * Internal components start hook
     * @param startFuture
     */
    protected abstract void startInternal(Future<Void> startFuture);

    /**
     * Internal components stop hook
     * @param stopFuture
     */
    protected abstract void stopInternal(Future<Void> stopFuture);

    /**
     *
     * @param message
     * @return
     * @throws KapuaConverterException
     */
    protected abstract MessageContext<M> convert(MessageContext<?> message) throws KapuaException;

    /**
     * Tells if the destination should or should not be processed by the chain
     * @param message
     * @return
     */
    protected abstract boolean isProcessDestination(MessageContext<M> message);

    public boolean isConnected() {
        return connected;
    }

    protected void setConnected(boolean connected) {
        this.connected = connected;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        logger.debug("Starting connector...");
        Future.succeededFuture()
        .compose(mapper -> {
            List<Future> futureList = new ArrayList<>();
            for (Entry<String, Processor> entry: errorProcessorMap.entrySet()) {
                Future<Void> internalFuture = Future.future();
                entry.getValue().start(internalFuture);
                futureList.add(internalFuture);
            }
            return CompositeFuture.join(futureList);
        })
        .compose(mapper -> {
            List<Future> futureList = new ArrayList<>();
            for (Entry<String, Processor<P>> entry: processorMap.entrySet()) {
                Future<Void> internalFuture = Future.future();
                entry.getValue().start(internalFuture);
                futureList.add(internalFuture);
            }
            return CompositeFuture.join(futureList);
        })
        .compose(mapper -> {
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
    }

    protected void handleMessage(MessageContext<?> message, Handler<AsyncResult<Void>> result) throws KapuaException {
        MessageContext<M> msg = convert(message);
        if (!isProcessDestination(msg)) {
            result.handle(Future.succeededFuture());
        }
        else {
            @SuppressWarnings("rawtypes")
            List<Future> futureList = new ArrayList<>();
            CompositeFuture compositeFuture;
            try {
                final MessageContext<P> convertedMessage = getConvertedMessage(msg);
                for (Entry<String, Processor<P>> entry : processorMap.entrySet()) {
                    futureList.add(callProcessor(convertedMessage, entry.getValue(), entry.getKey()));
                }
                compositeFuture = CompositeFuture.join(futureList);
            } catch (Exception e) {
                compositeFuture = CompositeFuture.join(Collections.singletonList(Future.failedFuture(e)));
            }

            compositeFuture.setHandler(ar -> {
                if (ar.succeeded()) {
                    result.handle(Future.succeededFuture());
                } else {
                    result.handle(Future.failedFuture(ar.cause()));
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    private MessageContext<P> getConvertedMessage(MessageContext<M> msg) throws KapuaConverterException {
        if (converter != null) {
            return converter.convert(msg);
        } else {
            return (MessageContext<P>) msg;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Future<Void> callProcessor(MessageContext<P> message, Processor<P> processor, String processorName) {
        Future<Void> internalFuture = Future.future();
        try {
            processor.process(message, internalFuture);
        } catch (KapuaException e) {
            Processor<?> errorProcessor = errorProcessorMap.get(processorName);
                try {
                    if (errorProcessor != null) {
                        errorProcessor.process(new MessageContext(message), ar -> {
                                if (ar.succeeded()) {
                                    internalFuture.succeeded();
                                }
                                else {
                                    internalFuture.fail(ar.cause());
                                }
                            }
                        );
                    }
                    else {
                        internalFuture.fail(e);
                    }
                } catch (Exception e1) {
                    internalFuture.fail(e1);
                }
        }
        return internalFuture;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        logger.debug("Stopping connector...");
        Future.succeededFuture()
        .compose(mapper -> {
            Future<Void> internalFuture = Future.future();
            stopInternal(internalFuture);
            return internalFuture;
        })
        .compose(mapper -> {
            List<Future> futureList = new ArrayList<>();
            for (Entry<String, Processor<P>> entry: processorMap.entrySet()) {
                Future<Void> internalFuture = Future.future();
                entry.getValue().stop(internalFuture);
                futureList.add(internalFuture);
            }
            return CompositeFuture.join(futureList);
        })
        .compose(mapper -> {
            List<Future> futureList = new ArrayList<>();
            for (Entry<String, Processor> entry: errorProcessorMap.entrySet()) {
                Future<Void> internalFuture = Future.future();
                entry.getValue().stop(internalFuture);
                futureList.add(internalFuture);
            }
            return CompositeFuture.join(futureList);
        })
        .setHandler(result -> {
            if (result.succeeded()) {
                logger.debug("Starting connector...DONE");
                stopFuture.complete();
            } else {
                logger.warn("Starting connector...FAIL [message:{}]", result.cause().getMessage());
                logger.debug("\tException:", result.cause());
                stopFuture.fail(result.cause());
            }
        });
    }

}
