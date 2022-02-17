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
package org.eclipse.kapua.broker.core.converter;

import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

/**
 * Kapua message converter used to convert data messages.
 *
 * @since 1.0
 */
public class KapuaDataConverter extends AbstractKapuaConverter {

    public static final Logger logger = LoggerFactory.getLogger(KapuaDataConverter.class);

    private Counter metricConverterDataMessage;

    public KapuaDataConverter() {
        super();
        metricConverterDataMessage = METRICS_SERVICE.getCounter(ConverterMetrics.METRIC_MODULE_NAME, ConverterMetrics.METRIC_COMPONENT_NAME, ConverterMetrics.METRIC_KAPUA_MESSAGE, ConverterMetrics.METRIC_MESSAGES, ConverterMetrics.METRIC_DATA, ConverterMetrics.METRIC_COUNT);
    }

    /**
     * Convert incoming message to a Kapua data message
     *
     * @param exchange
     * @param value
     * @return Message container that contains data message
     * @throws KapuaException
     *             if incoming message does not contain a javax.jms.BytesMessage or an error during conversion occurred
     */
    @Converter
    public CamelKapuaMessage<?> convertToData(Exchange exchange, Object value) throws KapuaException {
        metricConverterDataMessage.inc();
        CamelKapuaMessage<?> message = convertTo(exchange, value, MessageType.DATA);
        if (StringUtils.isEmpty(message.getDatastoreId())) {
            message.setDatastoreId(UUID.randomUUID().toString());
        }
        return message;
    }

    @Converter
    public CamelKapuaMessage<?> convertToDataOnException(Exchange exchange, Object value) throws KapuaException {
        metricConverterDataMessage.inc();
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
