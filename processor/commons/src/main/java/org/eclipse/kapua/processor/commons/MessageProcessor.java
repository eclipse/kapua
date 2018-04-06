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
package org.eclipse.kapua.processor.commons;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.AbstractEventBusService;
import org.eclipse.kapua.connector.AbstractMessageProcessor;
import org.eclipse.kapua.connector.Converter;
import org.eclipse.kapua.connector.MessageSource;
import org.eclipse.kapua.connector.MessageTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

// TODO move to common project
public class MessageProcessor<M,P> extends AbstractEventBusService {

    protected final static Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

    private final static String PROCESSOR_NAME_DATASTORE = "Datastore";

    protected Map<String, MessageTarget<P>> targetMap;
    protected Map<String, MessageTarget> errorTargetMap;

    private MessageProcessorConfig<M,P> config;

    private MessageSource<M> messageSource;
    private Converter<M,P> converter;
    private MessageTarget<P> messageTarget;
    private MessageTarget errorTarget;
    private AbstractMessageProcessor<M,P> messageProcessor;

    protected MessageProcessor(Vertx aVertx, MessageProcessorConfig<M,P> aConfig) {
        super(aVertx, aConfig.getEventBusServiceConfig());
        config = aConfig;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Future.succeededFuture()
        .compose(map -> {
            Future<Void> future = Future.future();
            try {
                super.start(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            try {
                logger.info("Starting Datastore Consumer...");
                initializeProcessors();
                messageProcessor.start(future);
            } catch (KapuaException e) {
                future.fail(e);            
            }
            return future;
        })
        .setHandler(ar -> {
            if (ar.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        Future.succeededFuture()
        .compose(map -> {
            Future<Void> future = Future.future();
            if (messageProcessor != null) {
                try {
                    messageProcessor.stop(future);
                    messageProcessor = null;
                    future.complete();
                 } catch (KapuaException e) {
                       future.fail(e);
                 }
                 return future;
            } else {
                future.complete();
            }
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            try {
                super.stop(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
        })
        .setHandler(ar -> {
            if (ar.succeeded()) {
                stopFuture.complete();
            } else {
                stopFuture.fail(ar.cause());
            }
        });
    }

    private void initializeProcessors() {
        messageSource = config.getMessageSource();
        converter = config.getConverter();
        messageTarget = config.getMessageTarget();
        errorTarget = config.getErrorTarget();
        targetMap = new HashMap<>();
        if (messageTarget != null) {
            targetMap.put(PROCESSOR_NAME_DATASTORE, messageTarget);
        }
        errorTargetMap = new HashMap<>();
        if (errorTarget != null) {
            errorTargetMap.put(PROCESSOR_NAME_DATASTORE, errorTarget);
        }
        messageProcessor = new AbstractMessageProcessor<>(messageSource, converter, targetMap, errorTargetMap);
    }
}
