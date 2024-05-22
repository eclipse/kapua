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
package org.eclipse.kapua.consumer.telemetry.converter;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.consumer.telemetry.MetricsTelemetry;
import org.eclipse.kapua.service.camel.application.MetricsCamel;
import org.eclipse.kapua.service.camel.converter.AbstractKapuaConverter;
import org.eclipse.kapua.service.camel.message.CamelKapuaMessage;
import org.eclipse.kapua.service.client.message.MessageType;
import org.eclipse.kapua.service.client.protocol.ProtocolDescriptorProvider;
import org.eclipse.kapua.translator.TranslatorHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua message converter used to convert data messages.
 *
 * @since 1.0
 */
public class KapuaDataConverter extends AbstractKapuaConverter {

    public static final Logger logger = LoggerFactory.getLogger(KapuaDataConverter.class);

    private final MetricsTelemetry metrics;

    @Inject
    public KapuaDataConverter(TranslatorHub translatorHub, MetricsCamel metricsCamel, MetricsTelemetry metricsTelemetry, ProtocolDescriptorProvider protocolDescriptorProvider) {
        super(translatorHub, metricsCamel, protocolDescriptorProvider);
        this.metrics = metricsTelemetry;
    }

    /**
     * Convert incoming message to a Kapua data message
     *
     * @param exchange
     * @param value
     * @return Message container that contains data message
     * @throws KapuaException
     *         if incoming message does not contain a javax.jms.BytesMessage or an error during conversion occurred
     */
    @Converter
    public CamelKapuaMessage<?> convertToData(Exchange exchange, Object value) throws KapuaException {
        metrics.getConverterDataMessage().inc();
        CamelKapuaMessage<?> message = convertTo(exchange, value, MessageType.DATA);
        if (StringUtils.isEmpty(message.getDatastoreId())) {
            message.setDatastoreId(UUID.randomUUID().toString());
        }
        return message;
    }

    @Converter
    public CamelKapuaMessage<?> convertToDataOnException(Exchange exchange, Object value) throws KapuaException {
        metrics.getConverterDataMessage().inc();
        // this converter may be used in different camel route step so may we already have a CamelKapuaMessage (depending on which step in the Camel route failed)
        CamelKapuaMessage<?> message;
        if (value instanceof CamelKapuaMessage<?>) {
            message = (CamelKapuaMessage<?>) value;
        } else {
            message = convertTo(exchange, value, MessageType.DATA);
        }
        if (StringUtils.isEmpty(message.getDatastoreId())) {
            logger.warn("Reprocessing message without datastore message id.");
            message.setDatastoreId(UUID.randomUUID().toString());
        }
        return message;
    }

}
