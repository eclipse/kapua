/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.message;

import org.apache.activemq.command.ActiveMQTopic;
import org.apache.camel.impl.DefaultMessage;
import org.eclipse.kapua.broker.core.listener.CamelConstants;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;

@Category(JUnitTests.class)
public class CamelUtilTest extends Assert {

    @Test(expected = NullPointerException.class)
    public void getTopicNullTest() throws JMSException {
        String topic = CamelUtil.getTopic(null);
    }

    @Test
    public void getTopicTest() throws JMSException {
        org.apache.camel.Message message = new DefaultMessage();
        Map<String, Object> map = new HashMap();
        map.put("originalTopic", "testTopic");
        message.setHeaders(map);
        String topic = CamelUtil.getTopic(message);
        assertEquals("testTopic", topic);
    }

    @Test(expected = JMSException.class)
    public void getTopicWithOrigTopicTest() throws JMSException {
        org.apache.camel.Message message = new DefaultMessage();
        Map<String, Object> map = new HashMap();
        map.put(MessageConstants.PROPERTY_ORIGINAL_TOPIC, null);
        message.setHeaders(map);
        String topic = CamelUtil.getTopic(message);
    }

    @Test
    public void getTopicWithJmsHeaderDestinationTest() throws JMSException {
        org.apache.camel.Message message = new DefaultMessage();
        Map<String, Object> map = new HashMap();
        map.put(CamelConstants.JMS_HEADER_DESTINATION, new ActiveMQTopic("VirtualTopic.topic1"));
        message.setHeaders(map);
        String topic = CamelUtil.getTopic(message);
        assertEquals("topic1", topic);
    }
}
