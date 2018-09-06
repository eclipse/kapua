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
import org.eclipse.kapua.apps.api.AmqpConnectorServerConfig;
import org.eclipse.kapua.broker.client.amqp.AmqpConsumer;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.HealthCheckProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.connector.activemq.AmqpActiveMQConsumer;
import org.eclipse.kapua.connector.logger.LoggerProcessor;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;


public class AmqpErrorConnectorServerConfigFactory implements ObjectFactory<AmqpConnectorServerConfig<Message, Message>> {

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
    private ConsumerConfiguration consumerConfig;

    @Override
    public AmqpConnectorServerConfig<Message, Message> create() {

        AmqpConnectorServerConfig<Message, Message> config = new AmqpConnectorServerConfig<Message, Message>();

        // Consumer
        AmqpActiveMQConsumer consumer = AmqpActiveMQConsumer.create(vertx, new AmqpConsumer(vertx, consumerConfig.createClientOptions(connectionConfig)));
        config.setConsumer(consumer);
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
        LoggerProcessor processor = LoggerProcessor.getProcessorWithNoFilter();
        config.setProcessor(processor);
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
        config.setErrorProcessor(null);

        // Other
        config.setEBAddress(ebAddress);
        config.setHealthCheckEBAddress(healthCheckEBAddress);
        config.setJAXBContextProvider(jaxbContextProvider);

        return config;
    }
}
