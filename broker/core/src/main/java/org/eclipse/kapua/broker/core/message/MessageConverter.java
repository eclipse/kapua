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
package org.eclipse.kapua.broker.core.message;

import java.util.Date;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.converter.AbstractKapuaConverter;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor.MessageType;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jms message utility class
 *
 * @since 1.0
 */
public class MessageConverter {

    public static final Logger logger = LoggerFactory.getLogger(MessageConverter.class);

    private MessageConverter() {
    }

    /**
     * Convert a {@link BytesMessage} to {@link CamelKapuaMessage}
     *
     * @param jmsMessage
     * @throws JMSException
     * @throws KapuaException
     */
    public static CamelKapuaMessage<?> convertToKapuaMessage(ConnectorDescriptor connectorDescriptor, MessageType messageType, BytesMessage jmsMessage, KapuaId connectionId, String clientId)
            throws JMSException, KapuaException {
        String jmsTopic = jmsMessage.getStringProperty(Properties.PROPERTY_ORIGINAL_TOPIC);
        Date queuedOn = new Date(jmsMessage.getLongProperty(Properties.PROPERTY_ENQUEUED_TIMESTAMP));
        return convertToKapuaMessage(connectorDescriptor, connectorDescriptor.getDeviceClass(messageType), connectorDescriptor.getKapuaClass(messageType), jmsMessage, jmsTopic, queuedOn, connectionId,
                clientId);
    }

    /**
     * Convert a {@link BytesMessage} to {@link KapuaMessage}
     * <p>
     * this code
     * <p>
     * <pre>
     * <code>
     * if (jmsMessage.getBodyLength() > 0) {
     *    payload = new byte[(int) jmsMessage.getBodyLength()];
     *    jmsMessage.readBytes(payload);
     * }
     * </code>
     * </pre>
     * <p>
     * with camel doesn't work.<br>
     * The call getBodyLength returns the correct message size but the read call reads an empty array (-1 is returned).<br>
     * The following code return the payload evaluated.<br>
     * ((ActiveMQMessage)jmsMessage).getContent().data<br>
     * so we modify the method assuming that camel converter called this utility method with a byte[] representing the jms body message.
     *
     * @param jmsMessage
     * @throws JMSException
     * @throws KapuaException
     * @see AbstractKapuaConverter
     */

    // TODO check the code with huge messages
    private static CamelKapuaMessage<?> convertToKapuaMessage(ConnectorDescriptor connectorDescriptor, Class<? extends DeviceMessage<?, ?>> deviceMessageType,
            Class<? extends KapuaMessage<?, ?>> kapuaMessageType, BytesMessage jmsMessage, String jmsTopic,
            Date queuedOn, KapuaId connectionId, String clientId)
            throws JMSException, KapuaException {
        byte[] payload = null;
        // TODO JMS message have no size limits!
        if (jmsMessage.getBodyLength() > 0) {
            payload = new byte[(int) jmsMessage.getBodyLength()];
            int readBytes = jmsMessage.readBytes(payload);
            logger.debug("Message conversion... {} bytes read!", readBytes);
        }
        KapuaMessage<?, ?> kapuaMessage = convertToKapuaMessage(deviceMessageType, kapuaMessageType, payload, jmsTopic, queuedOn, connectionId, clientId);
        return new CamelKapuaMessage<>(kapuaMessage, connectionId, connectorDescriptor);
    }

    /**
     * Convert raw byte[] message to {@link CamelKapuaMessage}
     *
     * @param connectorDescriptor
     * @param messageType
     * @param messageBody
     * @param jmsTopic
     * @param queuedOn
     * @param connectionId
     * @return
     * @throws KapuaException
     */
    public static CamelKapuaMessage<?> convertToCamelKapuaMessage(ConnectorDescriptor connectorDescriptor, MessageType messageType, byte[] messageBody, String jmsTopic, Date queuedOn,
            KapuaId connectionId, String clientId)
            throws KapuaException {
        KapuaMessage<?, ?> kapuaMessage = convertToKapuaMessage(connectorDescriptor.getDeviceClass(messageType), connectorDescriptor.getKapuaClass(messageType), messageBody, jmsTopic, queuedOn,
                connectionId, clientId);
        return new CamelKapuaMessage<KapuaMessage<?, ?>>(kapuaMessage, connectionId, connectorDescriptor);
    }

    /**
     * Convert raw byte[] message to {@link KapuaMessage}
     *
     * @param deviceMessageType
     * @param kapuaMessageType
     * @param messageBody
     * @param jmsTopic
     * @param queuedOn
     * @param connectionId
     * @return
     * @throws KapuaException
     */
    private static KapuaMessage<?, ?> convertToKapuaMessage(Class<? extends DeviceMessage<?, ?>> deviceMessageType, Class<? extends KapuaMessage<?, ?>> kapuaMessageType, byte[] messageBody,
            String jmsTopic, Date queuedOn, KapuaId connectionId, String clientId)
            throws KapuaException {
        // first step... from jms to device dependent protocol level (unknown)
        Translator<JmsMessage, DeviceMessage<?, ?>> translatorFromJms = Translator.getTranslatorFor(JmsMessage.class, deviceMessageType);// birth ...
        DeviceMessage<?, ?> deviceMessage = translatorFromJms.translate(new JmsMessage(new JmsTopic(jmsTopic), queuedOn, new JmsPayload(messageBody)));

        // second step.... from device dependent protocol (unknown) to Kapua
        Translator<DeviceMessage<?, ?>, KapuaMessage<?, ?>> translatorToKapua = Translator.getTranslatorFor(deviceMessageType, kapuaMessageType);
        KapuaMessage<?, ?> message = translatorToKapua.translate(deviceMessage);
        message.setClientId(clientId);
        return message;
    }

    /**
     * Convert a {@link KapuaMessage} message to a {@link JmsMessage}
     *
     * @param connectorDescriptor
     * @param messageType
     * @param kapuaMessage
     * @return
     * @throws KapuaException
     * @throws ClassNotFoundException
     */
    public static JmsMessage convertToJmsMessage(ConnectorDescriptor connectorDescriptor, MessageType messageType, KapuaMessage<?, ?> kapuaMessage) throws KapuaException, ClassNotFoundException {
        // first step... from Kapua to device level dependent protocol (unknown)
        Translator<KapuaMessage<?, ?>, DeviceMessage<?, ?>> translatorFromKapua = Translator
                .getTranslatorFor(connectorDescriptor.getKapuaClass(messageType), connectorDescriptor.getDeviceClass(messageType));
        DeviceMessage<?, ?> deviceMessage = translatorFromKapua.translate(kapuaMessage);

        // second step.... from device level dependent protocol (unknown) to jms
        Translator<DeviceMessage<?, ?>, JmsMessage> translatorToJms = Translator.getTranslatorFor(connectorDescriptor.getDeviceClass(messageType), JmsMessage.class);
        return translatorToJms.translate(deviceMessage);
    }

}
