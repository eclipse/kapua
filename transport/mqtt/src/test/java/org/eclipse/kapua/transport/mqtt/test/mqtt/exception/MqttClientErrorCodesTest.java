/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.mqtt.test.mqtt.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientErrorCodes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class MqttClientErrorCodesTest {

    @Test
    public void alreadyConnectedTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "ALREADY_CONNECTED", MqttClientErrorCodes.ALREADY_CONNECTED.name());
    }

    @Test
    public void callbackSetErrorTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "CALLBACK_SET_ERROR", MqttClientErrorCodes.CALLBACK_SET_ERROR.name());
    }

    @Test
    public void cleanErrorTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "CLEAN_ERROR", MqttClientErrorCodes.CLEAN_ERROR.name());
    }

    @Test
    public void connectErrorTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "CONNECT_ERROR", MqttClientErrorCodes.CONNECT_ERROR.name());
    }

    @Test
    public void disconnectErrorTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "DISCONNECT_ERROR", MqttClientErrorCodes.DISCONNECT_ERROR.name());
    }

    @Test
    public void notConnectedTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "NOT_CONNECTED", MqttClientErrorCodes.NOT_CONNECTED.name());
    }

    @Test
    public void publishExceptionTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "PUBLISH_EXCEPTION", MqttClientErrorCodes.PUBLISH_EXCEPTION.name());
    }

    @Test
    public void subscribeErrorTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "SUBSCRIBE_ERROR", MqttClientErrorCodes.SUBSCRIBE_ERROR.name());
    }

    @Test
    public void terminateErrorTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "TERMINATE_ERROR", MqttClientErrorCodes.TERMINATE_ERROR.name());
    }

    @Test
    public void unsubscribeErrorTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "UNSUBSCRIBE_ERROR", MqttClientErrorCodes.UNSUBSCRIBE_ERROR.name());
    }
}
