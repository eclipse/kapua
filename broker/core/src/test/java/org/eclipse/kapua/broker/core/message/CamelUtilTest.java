/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.message;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.camel.impl.DefaultMessage;
import org.eclipse.kapua.broker.core.listener.CamelConstants;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.jms.JMSException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@Category(JUnitTests.class)
public class CamelUtilTest extends Assert {

    @Test
    public void camelUtilTest() throws Exception {
        Constructor<CamelUtil> camelUtil = CamelUtil.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(camelUtil.getModifiers()));
        camelUtil.setAccessible(true);
        camelUtil.newInstance();
    }

    @Test(expected = NullPointerException.class)
    public void getTopicNullTest() throws JMSException {
        CamelUtil.getTopic(null);
    }

    @Test
    public void getTopicTest() throws JMSException {
        org.apache.camel.Message message = new DefaultMessage();
        Map<String, Object> map = new HashMap();
        map.put("originalTopic", "testTopic");
        message.setHeaders(map);
        String topic = CamelUtil.getTopic(message);
        assertEquals("Expected and actual values should be the same.", "testTopic", topic);
    }

    @Test
    public void getTopicWithActiveMQTopicTest() throws JMSException {
        org.apache.camel.Message message = new DefaultMessage();
        Map<String, Object> map = new HashMap();
        map.put(CamelConstants.JMS_HEADER_DESTINATION, new ActiveMQTopic("VirtualTopic.Topic"));
        message.setHeaders(map);
        String topic = CamelUtil.getTopic(message);
        assertEquals("Expected and actual values should be the same.", "Topic", topic);
    }

    @Test
    public void getTopicWithActiveMQDestinationTest() {
        org.apache.camel.Message message = new DefaultMessage();
        Map<String, Object> map = new HashMap();
        map.put(MessageConstants.PROPERTY_ORIGINAL_TOPIC, null);
        map.put(CamelConstants.JMS_HEADER_DESTINATION, Mockito.mock(ActiveMQDestination.class));
        message.setHeaders(map);
        try {
            CamelUtil.getTopic(message);
            fail("JMSException expected.");
        } catch (Exception e) {
            assertTrue("True expected.", e.getMessage().contains("Unable to extract the destination. Wrong destination Mock for ActiveMQDestination"));
        }
    }

    @Test
    public void getTopicNullDestinationTest() {
        org.apache.camel.Message message = new DefaultMessage();
        Map<String, Object> map = new HashMap();
        map.put(MessageConstants.PROPERTY_ORIGINAL_TOPIC, null);
        map.put(CamelConstants.JMS_HEADER_DESTINATION, null);
        message.setHeaders(map);
        try {
            CamelUtil.getTopic(message);
            fail("JMSException expected.");
        } catch (Exception e) {
            assertEquals("Expected and actual values should be the same.", new JMSException("Unable to extract the destination. Wrong destination null").toString(), e.toString());
        }
    }
}