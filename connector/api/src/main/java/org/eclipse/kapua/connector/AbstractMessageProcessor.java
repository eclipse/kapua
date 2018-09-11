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
public class AbstractMessageProcessor<M, P> implements Processor {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractMessageProcessor.class);

    protected MessageSource<M> messagingSource;
    protected Converter<M, P> converter;
    protected Map<String, MessageTarget<P>> serviceTargetMap;

    // TODO perche' non Processor<P> ????

    @SuppressWarnings("rawtypes")
    protected Map<String, MessageTarget> errorTargetMap;

    /**
     * Default protected constructor
     * @param vertx instance
     * @param converter message instance
     * @param processorMap processor map instances
     * @param errorProcessorMap error processor map instances (handles messages errors)
     */
    public AbstractMessageProcessor(MessageSource<M> consumer, Converter<M, P> converter, Map<String, MessageTarget<P>> processorMap, @SuppressWarnings("rawtypes") Map<String, MessageTarget> errorProcessorMap) {
        this.messagingSource = consumer;
        this.messagingSource.messageHandler(this::handleMessage);

        this.converter = converter;
        this.serviceTargetMap = processorMap;
        if (errorProcessorMap != null) {
            this.errorTargetMap = errorProcessorMap;
        }
        else {
            this.errorTargetMap = new HashMap<>();
        }
    }

    /**
     * Default protected constructor
     * @param vertx instance
     * @param converter message instance
     * @param processorMap processor map instances
     */
    public AbstractMessageProcessor(MessageSource<M> consumer, Converter<M, P> converter, Map<String, MessageTarget<P>> processorMap) {
        this.messagingSource = consumer;
        this.messagingSource.messageHandler(this::handleMessage);
        this.converter = converter;
        this.serviceTargetMap = processorMap;
    }

    /**
     * Constructor with no message converter
     * @param vertx instance
     * @param processorMap processor map instances
     */
    public AbstractMessageProcessor(MessageSource<M> connector, Map<String, MessageTarget<P>> processorMap) {
        this(connector, null, processorMap, null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void start(Future<Void> startFuture) throws KapuaException {
        logger.debug("Starting connector...");
        Future.succeededFuture()
        .compose(mapper -> {
            List<Future> futureList = new ArrayList<>();
            for (Entry<String, MessageTarget> entry: errorTargetMap.entrySet()) {
                Future<Void> internalFuture = Future.future();
                entry.getValue().start(internalFuture);
                futureList.add(internalFuture);
            }
            return CompositeFuture.join(futureList);
        })
        .compose(mapper -> {
            List<Future> futureList = new ArrayList<>();
            for (Entry<String, MessageTarget<P>> entry: serviceTargetMap.entrySet()) {
                Future<Void> internalFuture = Future.future();
                entry.getValue().start(internalFuture);
                futureList.add(internalFuture);
            }
            return CompositeFuture.join(futureList);
        })
        .compose(mapper -> {
            Future<Void> internalFuture = Future.future();
            messagingSource.start(internalFuture);
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
            for (Entry<String, MessageTarget<P>> entry : serviceTargetMap.entrySet()) {
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
    private Future<Void> callProcessor(MessageContext<P> message, MessageTarget<P> processor, String processorName) {
        Future<Void> internalFuture = Future.future();
        try {
            processor.process(message, internalFuture);
        } catch (KapuaException e) {
            MessageTarget<?> errorProcessor = errorTargetMap.get(processorName);
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
    public void stop(Future<Void> stopFuture) throws KapuaException {
        logger.debug("Stopping connector...");
        Future.succeededFuture()
        .compose(mapper -> {
            Future<Void> internalFuture = Future.future();
            messagingSource.stop(internalFuture);;
            return internalFuture;
        })
        .compose(mapper -> {
            List<Future> futureList = new ArrayList<>();
            for (Entry<String, MessageTarget<P>> entry: serviceTargetMap.entrySet()) {
                Future<Void> internalFuture = Future.future();
                entry.getValue().stop(internalFuture);
                futureList.add(internalFuture);
            }
            return CompositeFuture.join(futureList);
        })
        .compose(mapper -> {
            List<Future> futureList = new ArrayList<>();
            for (Entry<String, MessageTarget> entry: errorTargetMap.entrySet()) {
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
