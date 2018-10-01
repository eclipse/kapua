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
import javax.inject.Named;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.broker.client.amqp.AmqpConsumer;
import org.eclipse.kapua.broker.connector.amqp.AmqpActiveMQSource;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.HealthCheckAdapter;
import org.eclipse.kapua.connector.logger.LoggerTarget;
import org.eclipse.kapua.processor.commons.MessageProcessorConfig;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;

public class AmqpErrorProcessorConfigFactory implements ObjectFactory<MessageProcessorConfig<Message, Message>> {

    @Inject
    private Vertx vertx;

    @Inject
    @Named("kapua.vertx-app.event-bus-server.default-address")
    private String ebAddress;

    @Inject
    @Named("kapua.vertx-app.event-bus-server.health-address")
    private String healthCheckEBAddress;

    @Inject 
    private ConnectionConfiguration connectionConfig;

    @Inject 
    private SourceConfiguration sourceConfig;

    @Override
    public MessageProcessorConfig<Message, Message> create() {

        MessageProcessorConfig<Message, Message> config = new MessageProcessorConfig<Message, Message>();

        // Consumer
        AmqpActiveMQSource consumer = AmqpActiveMQSource.create(vertx, new AmqpConsumer(vertx, sourceConfig.createClientOptions(connectionConfig)));
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

        // Converter
        config.setConverter(null);

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

        // Error processor
        config.setErrorTarget(null);

        // Other
        config.setEBAddress(ebAddress);
        config.setHealthCheckEBAddress(healthCheckEBAddress);
        return config;
    }
}
