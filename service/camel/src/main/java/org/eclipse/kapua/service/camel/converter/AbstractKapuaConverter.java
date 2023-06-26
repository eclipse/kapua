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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.camel.converter;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.support.DefaultMessage;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.camel.application.MetricsCamel;
import org.eclipse.kapua.service.camel.message.CamelKapuaMessage;
import org.eclipse.kapua.service.camel.message.JmsUtil;
import org.eclipse.kapua.service.client.message.MessageConstants;
import org.eclipse.kapua.service.client.message.MessageType;
import org.eclipse.kapua.service.client.protocol.ProtocolDescriptor;
import org.eclipse.kapua.service.client.protocol.ProtocolDescriptorProviders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

import java.util.Base64;
import java.util.Date;

/**
 * Kapua message converter reference implementation used to convert from Camel incoming messages ({@link JmsMessage}) to a platform specific message type.
 *
 * @since 1.0
 */
public abstract class AbstractKapuaConverter {

    public static final Logger logger = LoggerFactory.getLogger(AbstractKapuaConverter.class);

    /**
     * Convert incoming message to a Kapua message (depending on messageType parameter)
     *
     * @param exchange
     * @param value
     * @param messageType expected incoming message type
     * @return Message container that contains message of asked type
     * @throws KapuaException if incoming message does not contain a javax.jms.BytesMessage or an error during conversion occurred
     */
    protected CamelKapuaMessage<?> convertTo(Exchange exchange, Object value, MessageType messageType) throws KapuaException {
        if (value instanceof byte[]) {
            byte[] messageContent = (byte[]) value;
            if (exchange.getIn() instanceof DefaultMessage) {
                DefaultMessage message = (DefaultMessage) exchange.getIn();
                try {
                    // FIX #164
                    Date queuedOn = new Date(message.getHeader(MessageConstants.HEADER_KAPUA_RECEIVED_TIMESTAMP, Long.class));
                    KapuaId connectionId = SerializationUtils.deserialize(Base64.getDecoder().decode(message.getHeader(MessageConstants.HEADER_KAPUA_CONNECTION_ID, String.class)));
                    String clientId = message.getHeader(MessageConstants.HEADER_KAPUA_CLIENT_ID, String.class);
                    String connectorName = message.getHeader(MessageConstants.HEADER_KAPUA_CONNECTOR_NAME, String.class);
                    ProtocolDescriptor connectorDescriptor = ProtocolDescriptorProviders.getDescriptor(connectorName);
                    if (connectorDescriptor == null) {
                        throw new IllegalStateException(String.format("Unable to find connector descriptor for connector '%s'", connectorName));
                    }
                    return JmsUtil.convertToCamelKapuaMessage(connectorDescriptor, messageType, messageContent, JmsUtil.getTopic(message), queuedOn, connectionId, clientId);
                } catch (JMSException e) {
                    MetricsCamel.getInstance().getConverterErrorMessage().inc();
                    logger.error("Exception converting message {}", e.getMessage(), e);
                    throw KapuaException.internalError(e, "Cannot convert the message type " + exchange.getIn().getClass());
                }
            }
        }
        MetricsCamel.getInstance().getConverterErrorMessage().inc();
        throw KapuaException.internalError("Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass());
    }

    /**
     * Convert incoming message to a javax.jms.Message
     *
     * @param exchange
     * @param value
     * @return jms Message
     * @throws KapuaException if incoming message does not contain a javax.jms.BytesMessage
     */
    @Converter
    public Message convertToJmsMessage(Exchange exchange, Object value) throws KapuaException {
        MetricsCamel.getInstance().getConverterJmsAttemptMessage().inc();
        // assume that the message is a Camel Jms message
        JmsMessage message = exchange.getIn(JmsMessage.class);
        if (message.getJmsMessage() instanceof BytesMessage) {
            return message.getJmsMessage();
        }
        MetricsCamel.getInstance().getConverterJmsErrorMessage().inc();
        throw KapuaException.internalError("Cannot convert the message - Wrong instance type: " + exchange.getIn().getClass());
    }

}
