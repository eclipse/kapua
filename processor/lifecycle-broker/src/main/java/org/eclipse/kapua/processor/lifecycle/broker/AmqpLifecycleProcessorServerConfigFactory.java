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
import javax.inject.Named;

import org.eclipse.kapua.broker.client.amqp.AmqpConsumer;
import org.eclipse.kapua.broker.client.amqp.AmqpSender;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.HealthCheckProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.connector.activemq.AmqpTransportActiveMQSource;
import org.eclipse.kapua.connector.error.amqp.activemq.ErrorTarget;
import org.eclipse.kapua.connector.kura.KuraPayloadProtoConverter;
import org.eclipse.kapua.connector.lifecycle.LifecycleProcessor;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.processor.commons.MessageProcessorServerConfig;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;


public class AmqpLifecycleProcessorServerConfigFactory implements ObjectFactory<MessageProcessorServerConfig<byte[], TransportMessage>> {

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

    @Inject 
    private TargetConfiguration targetConfig;

    @Override
    public MessageProcessorServerConfig<byte[], TransportMessage> create() {

        MessageProcessorServerConfig<byte[], TransportMessage> config = new MessageProcessorServerConfig<byte[], TransportMessage>();

        // Consumer
        AmqpTransportActiveMQSource consumer = AmqpTransportActiveMQSource.create(vertx, new AmqpConsumer(vertx, sourceConfig.createClientOptions(connectionConfig)));
        consumer.messageFilter(message -> {
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

        });
        config.setMessageSource(consumer);
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
        LifecycleProcessor processor = LifecycleProcessor.create();
        config.setMessageTarget(processor);
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
        ErrorTarget errorProcessor = ErrorTarget.getProcessor(vertx, new AmqpSender(vertx, targetConfig.createClientOptions(connectionConfig)));
        config.setErrorTarget(errorProcessor);
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
