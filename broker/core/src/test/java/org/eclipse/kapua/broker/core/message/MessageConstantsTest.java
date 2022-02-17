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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class MessageConstantsTest extends Assert {

    @Test
    public void messageConstantsTest() throws Exception {
        Constructor<MessageConstants> messageConstants = MessageConstants.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(messageConstants.getModifiers()));
        messageConstants.setAccessible(true);
        messageConstants.newInstance();
    }

    @Test
    public void constantsTest() {
        assertEquals("Expected and actual values should be the same.", "username", MessageConstants.METRIC_USERNAME);
        assertEquals("Expected and actual values should be the same.", "account", MessageConstants.METRIC_ACCOUNT);
        assertEquals("Expected and actual values should be the same.", "clientId", MessageConstants.METRIC_CLIENT_ID);
        assertEquals("Expected and actual values should be the same.", "ip", MessageConstants.METRIC_IP);
        assertEquals("Expected and actual values should be the same.", "originalTopic", MessageConstants.PROPERTY_ORIGINAL_TOPIC);
        assertEquals("Expected and actual values should be the same.", "enqueuedTimestamp", MessageConstants.PROPERTY_ENQUEUED_TIMESTAMP);
        assertEquals("Expected and actual values should be the same.", "brokerId", MessageConstants.PROPERTY_BROKER_ID);
        assertEquals("Expected and actual values should be the same.", "clientId", MessageConstants.PROPERTY_CLIENT_ID);
        assertEquals("Expected and actual values should be the same.", "scopeId", MessageConstants.PROPERTY_SCOPE_ID);
        assertEquals("Expected and actual values should be the same.", "userId", MessageConstants.METRIC_USER_ID);
        assertEquals("Expected and actual values should be the same.", "nodeId", MessageConstants.METRIC_NODE_ID);
        assertEquals("Expected and actual values should be the same.", "KAPUA_CONNECTION_ID", MessageConstants.HEADER_KAPUA_CONNECTION_ID);
        assertEquals("Expected and actual values should be the same.", "KAPUA_RECEIVED_TIMESTAMP", MessageConstants.HEADER_KAPUA_RECEIVED_TIMESTAMP);
        assertEquals("Expected and actual values should be the same.", "KAPUA_CLIENT_ID", MessageConstants.HEADER_KAPUA_CLIENT_ID);
        assertEquals("Expected and actual values should be the same.", "KAPUA_DEVICE_PROTOCOL", MessageConstants.HEADER_KAPUA_CONNECTOR_DEVICE_PROTOCOL);
        assertEquals("Expected and actual values should be the same.", "KAPUA_SESSION", MessageConstants.HEADER_KAPUA_SESSION);
        assertEquals("Expected and actual values should be the same.", "KAPUA_BROKER_CONTEXT", MessageConstants.HEADER_KAPUA_BROKER_CONTEXT);
        assertEquals("Expected and actual values should be the same.", "KAPUA_PROCESSING_EXCEPTION", MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION);
    }
}