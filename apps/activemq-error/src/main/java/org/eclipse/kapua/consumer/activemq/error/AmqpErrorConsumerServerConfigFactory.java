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
package org.eclipse.kapua.consumer.activemq.error;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.apps.api.MessageConsumerServerConfig;
import org.eclipse.kapua.broker.client.amqp.AmqpConsumer;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.HealthCheckProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.connector.activemq.AmqpActiveMQSource;
import org.eclipse.kapua.connector.logger.LoggerTarget;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;


public class AmqpErrorConsumerServerConfigFactory implements ObjectFactory<MessageConsumerServerConfig<Message, Message>> {

    @Inject
    private Vertx vertx;

    @Inject
    @Named("event-bus-server.default-address")
    private String ebAddress;

    @Inject
    @Named("event-bus-server.health-address")
    private String healthCheckEBAddress;

    @Inject
    private JAXBContextProvider jaxbContextProvider;

    @Inject 
    private ConnectionConfiguration connectionConfig;

    @Inject 
    private SourceConfiguration sourceConfig;

    @Override
    public MessageConsumerServerConfig<Message, Message> create() {

        MessageConsumerServerConfig<Message, Message> config = new MessageConsumerServerConfig<Message, Message>();

        // Consumer
        AmqpActiveMQSource consumer = AmqpActiveMQSource.create(vertx, new AmqpConsumer(vertx, sourceConfig.createClientOptions(connectionConfig)));
        config.setMessageSource(consumer);
        config.getHealthCheckProviders().add(new HealthCheckProvider() {

            @Override
            public void registerHealthChecks(HealthCheckHandler handler) {
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
        LoggerTarget processor = LoggerTarget.getProcessorWithNoFilter();
        config.setMessageTarget(processor);
        config.getHealthCheckProviders().add(new HealthCheckProvider() {

            @Override
            public void registerHealthChecks(HealthCheckHandler handler) {
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
        config.setJAXBContextProvider(jaxbContextProvider);

        return config;
    }
}
