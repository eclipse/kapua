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
package org.eclipse.kapua.processor.error.broker;

import javax.inject.Inject;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.broker.client.amqp.AmqpReceiver;
import org.eclipse.kapua.broker.connector.amqp.AmqpActiveMQSource;
import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.HealthCheckAdapter;
import org.eclipse.kapua.connector.logger.LoggerTarget;
import org.eclipse.kapua.processor.commons.AmqpConsumerConfig;
import org.eclipse.kapua.processor.commons.MessageProcessorConfig;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;

public class AmqpErrorProcessorConfigFactory implements ObjectFactory<MessageProcessorConfig<Message, Message>> {

    private static final String CONFIG_PROP_PROCESSOR = "kapua.errorProcessor";
    private static final String CONFIG_PROP_PROCESSOR_MSG_SOURCE_AMQP = "kapua.errorProcessor.messageSource.amqp";

    @Inject
    private Vertx vertx;

    @Inject
    private Configuration configuration;

    @Override
    public MessageProcessorConfig<Message, Message> create() {

        MessageProcessorConfig<Message, Message> config = MessageProcessorConfig.<Message, Message>create(CONFIG_PROP_PROCESSOR, configuration);

        // Consumer
        AmqpConsumerConfig amqpSourceConfig = AmqpConsumerConfig.create(CONFIG_PROP_PROCESSOR_MSG_SOURCE_AMQP, configuration);
        AmqpActiveMQSource consumer = AmqpActiveMQSource.create(vertx, new AmqpReceiver(vertx, amqpSourceConfig.createClientOptions()));
        consumer.messageFilter(message -> {
            return true;
        });
        config.setMessageSource(consumer);
        config.getHealthCheckAdapters().add(new HealthCheckAdapter() {

            @Override
            public void register(HealthCheckHandler handler) {
                handler.register("AmqpActiveMQConsumer", statusEvent -> {
                    if (consumer != null && consumer.isConnected()) {
                        statusEvent.complete(Status.OK());
                    } else {
                        statusEvent.complete(Status.KO());
                    }
                });
            }
        });

        // Processor
        LoggerTarget processor = LoggerTarget.create();
        config.setMessageTarget(processor);
        config.getHealthCheckAdapters().add(new HealthCheckAdapter() {

            @Override
            public void register(HealthCheckHandler handler) {
                handler.register("LoggerProcessor", statusEvent -> {
                    // TODO define a more meaningful health check
                    if (processor != null) {
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
