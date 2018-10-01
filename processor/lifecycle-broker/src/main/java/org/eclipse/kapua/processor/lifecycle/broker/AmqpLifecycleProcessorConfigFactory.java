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
import org.eclipse.kapua.broker.connector.amqp.AmqpTransportActiveMQSource;
import org.eclipse.kapua.broker.connector.amqp.ErrorTarget;
import org.eclipse.kapua.commons.core.ObjectFactory;
import org.eclipse.kapua.commons.core.vertx.HealthCheckAdapter;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.connector.kura.KuraPayloadProtoConverter;
import org.eclipse.kapua.device.registry.connector.LifecycleProcessor;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.processor.commons.MessageProcessorConfig;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;


public class AmqpLifecycleProcessorConfigFactory implements ObjectFactory<MessageProcessorConfig<byte[], TransportMessage>> {

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

    @Inject 
    private TargetConfiguration targetConfig;

    @Override
    public MessageProcessorConfig<byte[], TransportMessage> create() {

        MessageProcessorConfig<byte[], TransportMessage> config = new MessageProcessorConfig<byte[], TransportMessage>();

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
        ErrorTarget errorProcessor = ErrorTarget.getProcessor(vertx, new AmqpSender(vertx, targetConfig.createClientOptions(connectionConfig)));
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

        config.setEBAddress(ebAddress);
        config.setHealthCheckEBAddress(healthCheckEBAddress);
        return config;
    }
}
