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
package org.eclipse.kapua.processor.lifecycle.broker;

import javax.inject.Inject;

import org.eclipse.kapua.broker.client.amqp.AmqpReceiver;
import org.eclipse.kapua.broker.client.amqp.AmqpSender;
import org.eclipse.kapua.broker.connector.amqp.AmqpTransportActiveMQSource;
import org.eclipse.kapua.broker.connector.amqp.ErrorTarget;
import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.HealthCheckAdapter;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.connector.kura.KuraPayloadProtoConverter;
import org.eclipse.kapua.device.registry.connector.LifecycleProcessor;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.processor.commons.AmqpConsumerConfig;
import org.eclipse.kapua.processor.commons.MessageProcessorConfig;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;


public class AmqpLifecycleProcessorConfigFactory implements ObjectFactory<MessageProcessorConfig<byte[], TransportMessage>> {

    private static final String CONFIG_PROP_PROCESSOR = "kapua.lifecycleProcessor";
    private static final String CONFIG_PROP_PROCESSOR_MSG_SOURCE_AMQP = "kapua.lifecycleProcessor.messageSource.amqp";
    private static final String CONFIG_PROP_PROCESSOR_ERR_TARGET_AMQP = "kapua.lifecycleProcessor.errorTarget.amqp";

    @Inject
    private Vertx vertx;

    @Inject
    private Configuration configuration;

    @Override
    public MessageProcessorConfig<byte[], TransportMessage> create() {

        MessageProcessorConfig<byte[], TransportMessage> config = MessageProcessorConfig.<byte[], TransportMessage>create(CONFIG_PROP_PROCESSOR, configuration);

        // Consumer
        AmqpConsumerConfig amqpSourceConfig = AmqpConsumerConfig.create(CONFIG_PROP_PROCESSOR_MSG_SOURCE_AMQP, configuration);
        AmqpTransportActiveMQSource consumer = AmqpTransportActiveMQSource.create(vertx, new AmqpReceiver(vertx, amqpSourceConfig.createClientOptions()));
        consumer.messageFilter(message -> {
            String topic = (String) message.getProperties().get(Properties.MESSAGE_ORIGINAL_DESTINATION);
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

        });
        config.setMessageSource(consumer);
        config.getHealthCheckAdapters().add(new HealthCheckAdapter() {

            @Override
            public void register(HealthCheckHandler handler) {
                handler.register("AmqpTransportActiveMQConsumer", statusEvent -> {
                    if (consumer != null && consumer.isConnected()) {
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
        AmqpConsumerConfig amqpErrorTargetConfig = AmqpConsumerConfig.create(CONFIG_PROP_PROCESSOR_ERR_TARGET_AMQP, configuration);
        ErrorTarget errorProcessor = ErrorTarget.getProcessor(vertx, new AmqpSender(vertx, amqpErrorTargetConfig.createClientOptions()));
        config.setErrorTarget(errorProcessor);
        config.getHealthCheckAdapters().add(new HealthCheckAdapter() {

            @Override
            public void register(HealthCheckHandler handler) {
                handler.register("ErrorProcessor", statusEvent -> {
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
