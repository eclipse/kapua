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

import com.codahale.metrics.Counter;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua message converter used to convert device management notification messages.
 *
 * @since 1.0
 */
public class DeviceManagementNotificationConverter extends AbstractKapuaConverter {

    public static final Logger logger = LoggerFactory.getLogger(DeviceManagementNotificationConverter.class);

    private Counter metricConverterDeviceManagementNotificationMessage;

    public DeviceManagementNotificationConverter() {
        super();
        metricConverterDeviceManagementNotificationMessage = METRICS_SERVICE.getCounter(ConverterMetrics.METRIC_MODULE_NAME, ConverterMetrics.METRIC_COMPONENT_NAME, ConverterMetrics.METRIC_KAPUA_MESSAGE, ConverterMetrics.METRIC_MESSAGES, ConverterMetrics.METRIC_NOTIFY, ConverterMetrics.METRIC_COUNT);
    }

    /**
     * Convert incoming message to a Kapua management notification message
     *
     * @param exchange
     * @param value
     * @return Message container that contains management notification message
     * @throws KapuaException if incoming message does not contain a javax.jms.BytesMessage or an error during conversion occurred
     */
    @Converter
    public CamelKapuaMessage<?> convertToManagementNotification(Exchange exchange, Object value) throws KapuaException {
        metricConverterDeviceManagementNotificationMessage.inc();
        return convertTo(exchange, value, MessageType.NOTIFY);
    }

    @Converter
    public CamelKapuaMessage<?> convertToManagementNotificationOnException(Exchange exchange, Object value) throws KapuaException {
        metricConverterDeviceManagementNotificationMessage.inc();

        // this converter may be used in different camel route step so may we already have a CamelKapuaMessage (depending on which step in the Camel route failed)
        CamelKapuaMessage<?> message;
        if (value instanceof CamelKapuaMessage<?>) {
            message = (CamelKapuaMessage<?>) value;
        } else {
            message = convertTo(exchange, value, MessageType.NOTIFY);
        }

        return message;
    }

}
