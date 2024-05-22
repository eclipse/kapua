/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.consumer.lifecycle.converter;

import javax.inject.Inject;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.consumer.lifecycle.MetricsLifecycle;
import org.eclipse.kapua.service.camel.application.MetricsCamel;
import org.eclipse.kapua.service.camel.converter.AbstractKapuaConverter;
import org.eclipse.kapua.service.camel.message.CamelKapuaMessage;
import org.eclipse.kapua.service.client.message.MessageType;
import org.eclipse.kapua.service.client.protocol.ProtocolDescriptorProvider;
import org.eclipse.kapua.translator.TranslatorHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua message converter used to convert life cycle messages.
 *
 * @since 1.0
 */
public class KapuaLifeCycleConverter extends AbstractKapuaConverter {

    public static final Logger logger = LoggerFactory.getLogger(KapuaLifeCycleConverter.class);

    private final MetricsLifecycle metrics;

    @Inject
    public KapuaLifeCycleConverter(TranslatorHub translatorHub, MetricsCamel metricsCamel, MetricsLifecycle metricsLifecycle, ProtocolDescriptorProvider protocolDescriptorProvider) {
        super(translatorHub, metricsCamel, protocolDescriptorProvider);
        this.metrics = metricsLifecycle;
    }

    /**
     * Convert incoming message to a Kapua application (life cycle) message
     *
     * @param exchange
     * @param value
     * @return Message container that contains application message
     * @throws KapuaException
     *         if incoming message does not contain a javax.jms.BytesMessage or an error during conversion occurred
     */
    @Converter
    public CamelKapuaMessage<?> convertToApps(Exchange exchange, Object value) throws KapuaException {
        metrics.getConverterAppMessage().inc();
        return convertTo(exchange, value, MessageType.APP);
    }

    /**
     * Convert incoming message to a Kapua birth (life cycle) message
     *
     * @param exchange
     * @param value
     * @return Message container that contains birth message
     * @throws KapuaException
     *         if incoming message does not contain a javax.jms.BytesMessage or an error during conversion occurred
     */
    @Converter
    public CamelKapuaMessage<?> convertToBirth(Exchange exchange, Object value) throws KapuaException {
        metrics.getConverterBirthMessage().inc();
        return convertTo(exchange, value, MessageType.BIRTH);
    }

    /**
     * Convert incoming message to a Kapua disconnect (life cycle) message
     *
     * @param exchange
     * @param value
     * @return Message container that contains disconnect message
     * @throws KapuaException
     *         if incoming message does not contain a javax.jms.BytesMessage or an error during conversion occurred
     */
    @Converter
    public CamelKapuaMessage<?> convertToDisconnect(Exchange exchange, Object value) throws KapuaException {
        metrics.getConverterDcMessage().inc();
        return convertTo(exchange, value, MessageType.DISCONNECT);
    }

    /**
     * Convert incoming message to a Kapua missing (life cycle) message
     *
     * @param exchange
     * @param value
     * @return Message container that contains missing message
     * @throws KapuaException
     *         if incoming message does not contain a javax.jms.BytesMessage or an error during conversion occurred
     */
    @Converter
    public CamelKapuaMessage<?> convertToMissing(Exchange exchange, Object value) throws KapuaException {
        metrics.getConverterMissingMessage().inc();
        return convertTo(exchange, value, MessageType.MISSING);
    }

    /**
     * Convert incoming message to a Kapua notify (life cycle) message
     *
     * @param exchange
     * @param value
     * @return
     * @throws KapuaException
     */
    @Converter
    public CamelKapuaMessage<?> convertToNotify(Exchange exchange, Object value) throws KapuaException {
        metrics.getConverterNotifyMessage().inc();
        return convertTo(exchange, value, MessageType.NOTIFY);
    }

}
