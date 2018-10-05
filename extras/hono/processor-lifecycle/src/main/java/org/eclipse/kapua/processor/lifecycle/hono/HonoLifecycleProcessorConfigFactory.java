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
package org.eclipse.kapua.processor.lifecycle.hono;

import javax.inject.Inject;

import org.eclipse.kapua.broker.client.hono.HonoClient;
import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.HealthCheckAdapter;
import org.eclipse.kapua.connector.MessageContent;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.connector.hono.AmqpHonoSource;
import org.eclipse.kapua.connector.kura.KuraPayloadProtoConverter;
import org.eclipse.kapua.connector.logger.LoggerTarget;
import org.eclipse.kapua.device.registry.connector.hono.LifecycleProcessor;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.message.transport.TransportMessageType;
import org.eclipse.kapua.processor.commons.MessageProcessorConfig;
import org.eclipse.kapua.processor.commons.hono.HonoConsumerConfig;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;

public class HonoLifecycleProcessorConfigFactory implements ObjectFactory<MessageProcessorConfig<byte[], TransportMessage>> {

    private static final String CONFIG_PROP_PROCESSOR = "kapua.lifecycleProcessor";
    private static final String CONFIG_PROP_PROCESSOR_MSG_SOURCE_HONO = "kapua.lifecycleProcessor.messageSource.hono";

    @Inject
    private Vertx vertx;

    @Inject
    private Configuration configuration;

    @Override
    public MessageProcessorConfig<byte[], TransportMessage> create() {

        MessageProcessorConfig<byte[], TransportMessage> config = MessageProcessorConfig.<byte[], TransportMessage>create(CONFIG_PROP_PROCESSOR, configuration);

        // Hono Source
        HonoConsumerConfig honoSourceConfig = HonoConsumerConfig.create(CONFIG_PROP_PROCESSOR_MSG_SOURCE_HONO, configuration);
        AmqpHonoSource source = AmqpHonoSource.create(vertx, new HonoClient(vertx, honoSourceConfig.createClientOptions()));
        source.messageFilter(message -> {
            TransportMessageType messageType = (TransportMessageType) message.getProperties().get(Properties.MESSAGE_TYPE);
            MessageContent messageContent = (MessageContent)message.getProperties().get(Properties.MESSAGE_CONTENT);
            if (TransportMessageType.EVENTS.equals(messageType) && !MessageContent.SYSTEM.equals(messageContent)) {
                return true;
            }
            else {
                return false;
            }

        });
        config.setMessageSource(source);
        config.getHealthCheckAdapters().add(new HealthCheckAdapter() {

            @Override
            public void register(HealthCheckHandler handler) {
                handler.register("AmqpTransportActiveMQConsumer", statusEvent -> {
                    if (source != null && source.isConnected()) {
                        statusEvent.complete(Status.OK());
                    } else {
                        statusEvent.complete(Status.KO());
                    }
                });
            }
        });

        config.setConverter(new KuraPayloadProtoConverter());

        // Processor
        LifecycleProcessor processor = LifecycleProcessor.create();
        config.setMessageTarget(processor);
        config.getHealthCheckAdapters().add(new HealthCheckAdapter() {

            @Override
            public void register(HealthCheckHandler handler) {
                handler.register("LifecycleProcessor", statusEvent -> {
                    // TODO define a more meaningful health check
                    if (processor != null) {
                        statusEvent.complete(Status.OK());
                    } else {
                        statusEvent.complete(Status.KO());
                    }
                });
            }
        });

        // Error processor
        @SuppressWarnings("rawtypes")
        LoggerTarget errorProcessor = LoggerTarget.create();
        config.setErrorTarget(errorProcessor);
        config.getHealthCheckAdapters().add(new HealthCheckAdapter() {

            @Override
            public void register(HealthCheckHandler handler) {
                handler.register("LoggerProcessor", statusEvent -> {
                    // TODO define a more meaningful health check
                    if (errorProcessor != null) {
                        statusEvent.complete(Status.OK());
                    } else {
                        statusEvent.complete(Status.KO());
                    }
                });
            }
        });

        return config;
    }
}
