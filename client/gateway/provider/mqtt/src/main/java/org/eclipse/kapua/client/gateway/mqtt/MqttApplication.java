/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.mqtt;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;

import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.MessageHandler;
import org.eclipse.kapua.client.gateway.Payload;
import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.client.gateway.spi.AbstractApplication;
import org.eclipse.kapua.client.gateway.spi.AbstractData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttApplication extends AbstractApplication {

    private static final Logger logger = LoggerFactory.getLogger(MqttApplication.class);

    private final MqttClient client;

    public MqttApplication(final MqttClient client, final String applicationId, final ExecutorService executor) {
        super(client, applicationId, executor);
        this.client = client;
    }

    @Override
    public AbstractData data(final Topic topic) {
        return new AbstractData(this, topic);
    }

    @Override
    protected CompletionStage<?> publish(final Topic topic, final Payload payload) {
        logger.debug("Publishing values - {} -> {}", topic, payload.getValues());

        try {
            final ByteBuffer buffer = client.getCodec().encode(payload, null);
            buffer.flip();

            return client.publish(applicationId, topic, buffer);
        } catch (final Exception e) {
            final CompletableFuture<?> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    protected CompletionStage<?> internalSubscribe(final Topic topic, final MessageHandler handler, final ErrorHandler<? extends Throwable> errorHandler) throws Exception {
        return client.subscribe(applicationId, topic, (messageTopic, payload) -> {
            logger.debug("Received message for: {}", topic);
            try {
                MqttApplication.this.handleMessage(handler, payload);
            } catch (final Exception e) {
                try {
                    errorHandler.handleError(e, null);
                } catch (final Exception e1) {
                    throw e1;
                } catch (final Throwable e1) {
                    throw new Exception(e1);
                }
            }
        });
    }

    protected void handleMessage(final MessageHandler handler, final ByteBuffer buffer) throws Exception {
        final Payload payload = client.getCodec().decode(buffer);
        logger.debug("Received: {}", payload);
        handler.handleMessage(payload);
    }
}