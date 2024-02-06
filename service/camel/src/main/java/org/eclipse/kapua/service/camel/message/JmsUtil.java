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
package org.eclipse.kapua.service.camel.message;

import org.eclipse.kapua.service.client.message.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Jms message utility class
 *
 * @since 1.0
 */
//TODO: Convert this into an injectable collaborator
public class JmsUtil {

    public static final Logger logger = LoggerFactory.getLogger(JmsUtil.class);

    private JmsUtil() {
    }

    public static String getTopic(org.apache.camel.Message message) throws JMSException {
        String topicOrig = message.getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class);
        if (topicOrig != null) {
            return topicOrig;
        } else {
            Destination destination = message.getHeader(MessageConstants.HEADER_CAMEL_JMS_HEADER_DESTINATION, Destination.class);
            if (destination instanceof Queue) {
                topicOrig = ((Queue) destination).getQueueName();
            } else if (destination instanceof Topic) {
                topicOrig = ((Topic) destination).getTopicName();
            } else {
                logger.warn("jmsMessage destination is null!", destination);
                throw new JMSException(String.format("Unable to extract the destination. Wrong destination %s", destination));
            }
        }
        return topicOrig;
    }

}
