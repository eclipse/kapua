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
package org.eclipse.kapua.transport.mqtt.test.mqtt.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientErrorCodes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class MqttClientErrorCodesTest extends Assert {

    @Test
    public void alreadyConnectedTest() {
        assertEquals("Expected and actual values should be the same!", "ALREADY_CONNECTED", MqttClientErrorCodes.ALREADY_CONNECTED.name());
    }

    @Test
    public void callbackSetErrorTest() {
        assertEquals("Expected and actual values should be the same!", "CALLBACK_SET_ERROR", MqttClientErrorCodes.CALLBACK_SET_ERROR.name());
    }

    @Test
    public void cleanErrorTest() {
        assertEquals("Expected and actual values should be the same!", "CLEAN_ERROR", MqttClientErrorCodes.CLEAN_ERROR.name());
    }

    @Test
    public void connectErrorTest() {
        assertEquals("Expected and actual values should be the same!", "CONNECT_ERROR", MqttClientErrorCodes.CONNECT_ERROR.name());
    }

    @Test
    public void disconnectErrorTest() {
        assertEquals("Expected and actual values should be the same!", "DISCONNECT_ERROR", MqttClientErrorCodes.DISCONNECT_ERROR.name());
    }

    @Test
    public void notConnectedTest() {
        assertEquals("Expected and actual values should be the same!", "NOT_CONNECTED", MqttClientErrorCodes.NOT_CONNECTED.name());
    }

    @Test
    public void publishExceptionTest() {
        assertEquals("Expected and actual values should be the same!", "PUBLISH_EXCEPTION", MqttClientErrorCodes.PUBLISH_EXCEPTION.name());
    }

    @Test
    public void subscribeErrorTest() {
        assertEquals("Expected and actual values should be the same!", "SUBSCRIBE_ERROR", MqttClientErrorCodes.SUBSCRIBE_ERROR.name());
    }

    @Test
    public void terminateErrorTest() {
        assertEquals("Expected and actual values should be the same!", "TERMINATE_ERROR", MqttClientErrorCodes.TERMINATE_ERROR.name());
    }

    @Test
    public void unsubscribeErrorTest() {
        assertEquals("Expected and actual values should be the same!", "UNSUBSCRIBE_ERROR", MqttClientErrorCodes.UNSUBSCRIBE_ERROR.name());
    }
}