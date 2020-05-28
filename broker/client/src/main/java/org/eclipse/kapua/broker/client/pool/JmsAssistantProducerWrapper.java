/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.client.pool;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.message.JmsUtil;
import org.eclipse.kapua.broker.client.message.MessageConstants;

/**
 * Broker ({@link JmsProducerWrapper}) implementation.<BR>
 * This class provide methods to send messages for the device life cycle (to be send outside to a device specific topic)
 *
 * @since 1.0
 */
public class JmsAssistantProducerWrapper extends JmsProducerWrapper {

    public JmsAssistantProducerWrapper(ActiveMQConnectionFactory vmconnFactory, String destination, boolean transacted, boolean start) throws JMSException, KapuaException {
        super(vmconnFactory, destination, transacted, start);
    }

    /**
     * Send a text message to the specified topic
     *
     * @param topic
     * @param message
     * @throws JMSException
     */
    public void send(String topic, String message, Map<String, String> parameters) throws JMSException {
        TextMessage textMessage = session.createTextMessage();
        Topic jmsTopic = session.createTopic(topic);

        textMessage.setStringProperty(MessageConstants.PROPERTY_BROKER_ID, parameters.get(MessageConstants.PROPERTY_BROKER_ID));
        textMessage.setStringProperty(MessageConstants.PROPERTY_CLIENT_ID, parameters.get(MessageConstants.PROPERTY_CLIENT_ID));
        textMessage.setLongProperty(MessageConstants.PROPERTY_SCOPE_ID, Long.valueOf(parameters.get(MessageConstants.PROPERTY_SCOPE_ID)));
        textMessage.setStringProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC, JmsUtil.convertMqttWildCardToJms(topic));
        textMessage.setLongProperty(MessageConstants.PROPERTY_ENQUEUED_TIMESTAMP, System.currentTimeMillis());
        textMessage.setText(message);
        textMessage.setJMSDestination(jmsTopic);

        producer.send(jmsTopic, textMessage);
    }

}
