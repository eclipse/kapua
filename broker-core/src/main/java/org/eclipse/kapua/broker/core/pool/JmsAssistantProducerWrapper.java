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
package org.eclipse.kapua.broker.core.pool;

import java.util.ArrayList;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.kapua.broker.core.message.JmsUtil;
import org.eclipse.kapua.broker.core.message.MessageConstants;

/**
 * Broker assistant ({@link JmsProducerWrapper}) implementation.<BR>
 * This class provide methods to send messages for the device life cycle (to be send outside to a device specific topic) and alert/login info update messages (to be send to a specific queue)
 * 
 * @since 1.0
 */
public class JmsAssistantProducerWrapper extends JmsProducerWrapper {

    private final static List<String> semanticTopicConnect = new ArrayList<String>();
    private final static List<String> semanticTopicDisconnect = new ArrayList<String>();
    private final static List<String> semanticTopicMissing = new ArrayList<String>();

    static {
        semanticTopicConnect.add("MQTT");
        semanticTopicConnect.add("CONNECT");

        semanticTopicDisconnect.add("MQTT");
        semanticTopicDisconnect.add("DISCONNECT");

        semanticTopicMissing.add("MQTT");
        semanticTopicMissing.add("MISSING");
    }

    public JmsAssistantProducerWrapper(ActiveMQConnectionFactory vmconnFactory, String destination, boolean transacted, boolean start) throws JMSException {
        super(vmconnFactory, destination, transacted, start);
    }

    // ==========================================================
    // Messages to be send into the internal KAPUA_SERVICE queue
    // ==========================================================
    /**
     * Send the data messages gone in error to a specific queue to keep them
     * 
     * @param messageNotStored
     * @param topic
     * @throws JMSException
     */
    public void sendDataMessageNotStored(String topic, byte[] messageNotStored) throws JMSException {
        BytesMessage message = session.createBytesMessage();

        message.setStringProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC, JmsUtil.convertMqttWildCardToJms(topic));
        message.setLongProperty(MessageConstants.PROPERTY_ENQUEUED_TIMESTAMP, System.currentTimeMillis());
        message.writeBytes(messageNotStored);

        producer.send(message);
    }

}
