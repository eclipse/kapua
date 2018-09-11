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
package org.eclipse.kapua.connector.lifecycle;

import java.util.List;

import org.eclipse.kapua.connector.KapuaProcessorException;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.MessageTarget;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public abstract class LifecycleProcessor implements MessageTarget<TransportMessage> {

    private static final Logger logger = LoggerFactory.getLogger(LifecycleProcessor.class);

    private static final String INVALID_TOPIC = "Cannot detect destination!";

    private LifecycleListener lifecycleListener;

    enum LifecycleTypes {
        APPS,
        BIRTH,
        DC,
        MISSING
    }

    public static LifecycleProcessor getProcessorWithNoFilter() {
        return new LifecycleProcessor() {
            @Override
            public boolean isProcessDestination(MessageContext<TransportMessage> message) {
                String topic = (String) message.getProperties().get(Properties.MESSAGE_DESTINATION);
                if (topic!=null && (topic.endsWith("/MQTT/BIRTH") ||
                        topic.endsWith("/MQTT/DC") ||
                        topic.endsWith("/MQTT/LWT") ||
                        topic.endsWith("/MQTT/MISSING") ||
                        topic.endsWith("MQTT/PROV"))
                        ) {
                    return true;
                }
                else {
                    return false;
                }

            }
        };
    }

    protected LifecycleProcessor() {
        lifecycleListener = new LifecycleListener();
    }

    @Override
    public void start(Future<Void> startFuture) {
        // nothing to do
        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        // nothing to do
        stopFuture.complete();
    }

    @Override
    public void process(MessageContext<TransportMessage> message, Handler<AsyncResult<Void>> result) throws KapuaProcessorException {
        List<String> destination = message.getMessage().getChannel().getSemanticParts();
        if (destination!=null && destination.size()>1) {
            String messageType = destination.get(1);
            LifecycleTypes token = null;
            try {
                token = LifecycleTypes.valueOf(messageType);
            }
            catch (IllegalArgumentException | NullPointerException e) {
                logger.debug("Invalid message type ({})", messageType);
                result.handle(Future.failedFuture(INVALID_TOPIC));
                return;
            }
            switch (token) {
            case APPS:
                lifecycleListener.processAppsMessage(message);
                break;
            case BIRTH:
                lifecycleListener.processBirthMessage(message);
                break;
            case DC:
                lifecycleListener.processDisconnectMessage(message);
                break;
            case MISSING:
                lifecycleListener.processMissingMessage(message);
                break;
            default:
                result.handle(Future.succeededFuture());
                break;
            }
        }
        else {
            result.handle(Future.failedFuture(INVALID_TOPIC));
        }
    }
}
