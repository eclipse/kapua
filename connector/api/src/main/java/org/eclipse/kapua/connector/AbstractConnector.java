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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * Abstract connector to be customized with specific server connection code.<br>
 * The incoming message will flow through the converter (if provided) and the processor
 *
 * @param <M> Converter message type (optional)
 * @param <P> Processor message type
 */
public class AbstractConnector<M, P> {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractConnector.class);

    protected Consumer<M> consumer;
    protected Converter<M, P> converter;
    protected Map<String, Processor<P>> processorMap;

    // TODO perche' non Processor<P> ????

    @SuppressWarnings("rawtypes")
    protected Map<String, Processor> errorProcessorMap;

    /**
     * Default protected constructor
     * @param vertx instance
     * @param converter message instance
     * @param processorMap processor map instances
     * @param errorProcessorMap error processor map instances (handles messages errors)
     */
    public AbstractConnector(Consumer<M> consumer, Converter<M, P> converter, Map<String, Processor<P>> processorMap, @SuppressWarnings("rawtypes") Map<String, Processor> errorProcessorMap) {
        this.consumer = consumer;
        this.consumer.messageHandler(this::handleMessage);

        this.converter = converter;
        this.processorMap = processorMap;
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
    public AbstractConnector(Consumer<M> consumer, Converter<M, P> converter, Map<String, Processor<P>> processorMap) {
        this.consumer = consumer;
        this.consumer.messageHandler(this::handleMessage);
        this.converter = converter;
        this.processorMap = processorMap;
    }

    /**
     * Constructor with no message converter
     * @param vertx instance
     * @param processorMap processor map instances
     */
    public AbstractConnector(Consumer<M> connector, Map<String, Processor<P>> processorMap) {
        this(connector, null, processorMap, null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
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
            consumer.start(internalFuture);
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

    protected void handleMessage(MessageContext<M> message, Handler<AsyncResult<Void>> result) throws KapuaException {
        @SuppressWarnings("rawtypes")
        List<Future> futureList = new ArrayList<>();
        CompositeFuture compositeFuture;
        try {
            final MessageContext<P> convertedMessage = getConvertedMessage(message);
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
    public void stop(Future<Void> stopFuture) throws Exception {
        logger.debug("Stopping connector...");
        Future.succeededFuture()
        .compose(mapper -> {
            Future<Void> internalFuture = Future.future();
            consumer.stop(internalFuture);;
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
