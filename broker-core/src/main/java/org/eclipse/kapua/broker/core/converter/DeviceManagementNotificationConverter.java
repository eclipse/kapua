/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
        metricConverterDeviceManagementNotificationMessage = METRICS_SERVICE.getCounter(METRIC_COMPONENT_NAME, "kapua", "kapua_message", "messages", "notify", "count");
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
