/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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

import javax.jms.JMSException;
import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQMessage;
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityBrokerFilter;
import org.eclipse.kapua.connector.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jms message utility class
 *
 * @since 1.0
 */
public class JmsUtil {

    public static final Logger logger = LoggerFactory.getLogger(JmsUtil.class);

    private JmsUtil() {
    }

    /**
     * Return the topic for the message's destination
     *
     * @param jmsMessage
     * @return
     * @throws JMSException
     */
    public static String getJmsTopic(ActiveMQMessage jmsMessage) throws JMSException {
        String jmsTopic = null;
        if (jmsMessage.getDestination().isTopic()) {
            jmsTopic = ((Topic) jmsMessage.getJMSDestination()).getTopicName().substring(KapuaSecurityBrokerFilter.VT_TOPIC_PREFIX.length());
        } else if (jmsMessage.getDestination().isQueue()) {
            jmsTopic = jmsMessage.getStringProperty(Properties.MESSAGE_ORIGINAL_DESTINATION);
        } else {
            logger.warn("jmsMessage destination is not a Topic or Queue: {}", jmsMessage.getDestination());
        }
        return jmsTopic;
    }

    // =========================================
    // wildcards conversion
    // =========================================

    /**
     * ActiveMQ translate wildcards from jms to mqtt
     * function ActiveMQ MQTT
     * separator . /
     * element * +
     * sub tree &gt; #
     *
     * @param jmsTopic
     * @return
     */
    public static String convertJmsWildCardToMqtt(String jmsTopic) {
        String processedTopic = null;
        if (jmsTopic != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < jmsTopic.length(); i++) {
                sb.append(convertWildcardJmsToMqtt(jmsTopic.charAt(i)));
            }
            processedTopic = sb.toString();
        }
        return processedTopic;
    }

    private static char convertWildcardJmsToMqtt(char c) {
        switch (c) {
        case '.':
            return '/';
        case '/':
            return '.';
        default:
            return c;
        }
    }

    /**
     * ActiveMQ translate wildcards from jms to mqtt
     * function ActiveMQ MQTT
     * separator . /
     * element * +
     * sub tree &gt; #
     *
     * @param mqttTopic
     * @return
     */
    public static String convertMqttWildCardToJms(String mqttTopic) {
        String processedTopic = null;
        if (mqttTopic != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mqttTopic.length(); i++) {
                sb.append(convertWildcardMqttToJms(mqttTopic.charAt(i)));
            }
            processedTopic = sb.toString();
        }
        return processedTopic;
    }

    private static char convertWildcardMqttToJms(char c) {
        switch (c) {
        case '.':
            return '/';
        case '/':
            return '.';
        default:
            return c;
        }
    }

}
