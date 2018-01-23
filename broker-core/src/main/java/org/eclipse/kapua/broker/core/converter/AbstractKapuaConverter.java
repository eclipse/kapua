/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.broker.core.converter;

import java.util.Base64;
import java.util.Date;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.listener.CamelConstants;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.broker.core.message.CamelUtil;
import org.eclipse.kapua.broker.core.message.JmsUtil;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.model.id.KapuaId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

/**
 * Kapua message converter reference implementation used to convert from Camel incoming messages ({@link JmsMessage}) to a platform specific message type.
 *
 * @since 1.0
 */
public abstract class AbstractKapuaConverter {

    public static final Logger logger = LoggerFactory.getLogger(AbstractKapuaConverter.class);

    // metrics
    protected final static String METRIC_COMPONENT_NAME = "converter";
    protected final static MetricsService METRICS_SERVICE = MetricServiceFactory.getInstance();

    private final Counter metricConverterJmsMessage;
    private final Counter metricConverterJmsErrorMessage;
    private final Counter metricConverterErrorMessage;

    /**
     * Constructor
     */
    protected AbstractKapuaConverter() {
        metricConverterJmsMessage = METRICS_SERVICE.getCounter(METRIC_COMPONENT_NAME, "kapua", "jms", "messages", "count");
        metricConverterJmsErrorMessage = METRICS_SERVICE.getCounter(METRIC_COMPONENT_NAME, "kapua", "jms", "messages", "error", "count");
        metricConverterErrorMessage = METRICS_SERVICE.getCounter(METRIC_COMPONENT_NAME, "kapua", "kapua_message", "messages", "error", "count");
    }

    /**
     * Convert incoming message to a Kapua message (depending on messageType parameter)
     *
     * @param exchange
     * @param value
     * @param messageType
     *            expected incoming message type
     * @return Message container that contains message of asked type
     * @throws KapuaException
     *             if incoming message does not contain a javax.jms.BytesMessage or an error during conversion occurred
     */
    protected CamelKapuaMessage<?> convertTo(Exchange exchange, Object value, String messageType) throws KapuaException {
        // assume that the message is a Camel Jms message
        JmsMessage message = exchange.getIn(JmsMessage.class);
        if (message.getJmsMessage() instanceof BytesMessage) {
            try {
                // FIX #164
                Date queuedOn = new Date(message.getHeader(CamelConstants.JMS_HEADER_TIMESTAMP, Long.class));
                KapuaId connectionId = (KapuaId) SerializationUtils.deserialize(Base64.getDecoder().decode(message.getHeader(MessageConstants.HEADER_KAPUA_CONNECTION_ID, String.class)));
                String clientId = message.getHeader(MessageConstants.HEADER_KAPUA_CLIENT_ID, String.class);
                ConnectorDescriptor connectorDescriptor = (ConnectorDescriptor) SerializationUtils
                        .deserialize(Base64.getDecoder().decode(message.getHeader(MessageConstants.HEADER_KAPUA_CONNECTOR_DEVICE_PROTOCOL, String.class)));
                return JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, (byte[]) value, CamelUtil.getTopic(message), queuedOn, connectionId, clientId);
            } catch (JMSException e) {
                metricConverterErrorMessage.inc();
                logger.error("Exception converting message {}", e.getMessage(), e);
                throw KapuaException.internalError(e, "Cannot convert the message type " + exchange.getIn().getClass());
            }
        }
        metricConverterErrorMessage.inc();
        throw KapuaException.internalError("Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass());
    }

    /**
     * Convert incoming message to a javax.jms.Message
     *
     * @param exchange
     * @param value
     * @return jms Message
     * @throws KapuaException
     *             if incoming message does not contain a javax.jms.BytesMessage
     */
    @Converter
    public Message convertToJmsMessage(Exchange exchange, Object value) throws KapuaException {
        metricConverterJmsMessage.inc();
        // assume that the message is a Camel Jms message
        JmsMessage message = exchange.getIn(JmsMessage.class);
        if (message.getJmsMessage() instanceof BytesMessage) {
            return message.getJmsMessage();
        }
        metricConverterJmsErrorMessage.inc();
        throw KapuaException.internalError("Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass());
    }

}
