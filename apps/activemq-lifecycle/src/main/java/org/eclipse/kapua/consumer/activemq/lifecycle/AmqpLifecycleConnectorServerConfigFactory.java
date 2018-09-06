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
package org.eclipse.kapua.consumer.activemq.lifecycle;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.kapua.apps.api.AmqpConnectorServerConfig;
import org.eclipse.kapua.broker.client.amqp.AmqpConsumer;
import org.eclipse.kapua.broker.client.amqp.AmqpSender;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.HealthCheckProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.connector.activemq.AmqpTransportActiveMQConsumer;
import org.eclipse.kapua.connector.kura.KuraPayloadProtoConverter;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.connector.error.amqp.activemq.ErrorProcessor;
import org.eclipse.kapua.connector.lifecycle.LifecycleProcessor;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;


public class AmqpLifecycleConnectorServerConfigFactory implements ObjectFactory<AmqpConnectorServerConfig<byte[], TransportMessage>> {

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

    @Inject 
    private SenderConfiguration senderConfig;

    @Override
    public AmqpConnectorServerConfig<byte[], TransportMessage> create() {

        AmqpConnectorServerConfig<byte[], TransportMessage> config = new AmqpConnectorServerConfig<byte[], TransportMessage>();

        // Consumer
        AmqpTransportActiveMQConsumer consumer = AmqpTransportActiveMQConsumer.create(vertx, new AmqpConsumer(vertx, consumerConfig.createClientOptions(connectionConfig)));
        config.setConsumer(consumer);
        config.getHealthCheckProviders().add(new HealthCheckProvider() {

            @Override
            public void registerHealthChecks(HealthCheckHandler handler) {
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
        LifecycleProcessor processor = LifecycleProcessor.getProcessorWithNoFilter();
        config.setProcessor(processor);
        config.getHealthCheckProviders().add(new HealthCheckProvider() {

            @Override
            public void registerHealthChecks(HealthCheckHandler handler) {
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
        ErrorProcessor errorProcessor = ErrorProcessor.getProcessorWithNoFilter(vertx, new AmqpSender(vertx, senderConfig.createClientOptions(connectionConfig)));
        config.setErrorProcessor(errorProcessor);
        config.getHealthCheckProviders().add(new HealthCheckProvider() {

            @Override
            public void registerHealthChecks(HealthCheckHandler handler) {
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

        config.setEBAddress(ebAddress);
        config.setHealthCheckEBAddress(healthCheckEBAddress);
        config.setJAXBContextProvider(jaxbContextProvider);

        return config;
    }
}
