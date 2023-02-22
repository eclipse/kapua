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
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientAlreadyConnectedException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientCallbackSetException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientCleanException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientConnectException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientDisconnectException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientErrorCodes;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientNotConnectedException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientPublishException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientSubscribeException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientTerminateException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientUnsubscribeException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@Category(JUnitTests.class)
public class MqttClientExceptionTest {

    private final Throwable cause = new Throwable("This is the cause");

    private final String clientId = "clientId";
    private final String username = "username";
    private final URI uri;

    private final MqttTopic mqttTopic = new MqttTopic("mqttTopic");
    private final MqttPayload mqttPayload = new MqttPayload("mqttPayload".getBytes());
    private final MqttMessage mqttMessage = new MqttMessage(mqttTopic, new Date(), mqttPayload);

    public MqttClientExceptionTest() throws URISyntaxException {
        this.uri = new URI("mqtt://localhost:1883");
    }

    @Test
    public void testMqttClientErrorCodesHaveMessages() {
        for (MqttClientErrorCodes errorCode : MqttClientErrorCodes.values()) {
            MqttClientException mqttClientException = new MqttClientException(errorCode, clientId);

            Assert.assertNotEquals("Error: clientId", mqttClientException.getMessage());
            Assert.assertNotEquals("Error: clientId", mqttClientException.getLocalizedMessage());
        }
    }

    @Test
    public void testMqttClientAlreadyConnectedException() {
        String exceptionMessage = "MqttClient " + clientId + " is already connected.";

        MqttClientAlreadyConnectedException mqttClientAlreadyConnectedException = new MqttClientAlreadyConnectedException(clientId);

        Assert.assertEquals(MqttClientErrorCodes.ALREADY_CONNECTED, mqttClientAlreadyConnectedException.getCode());
        Assert.assertNull(mqttClientAlreadyConnectedException.getCause());
        Assert.assertEquals(clientId, mqttClientAlreadyConnectedException.getClientId());
        Assert.assertEquals(exceptionMessage, mqttClientAlreadyConnectedException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientAlreadyConnectedException.getLocalizedMessage());
    }

    @Test
    public void testMqttClientCallbackSetException() {
        String exceptionMessage = "MqttClient " + clientId + " cannot set response callback on topic: " + mqttTopic;

        MqttClientCallbackSetException mqttClientCallbackSetException = new MqttClientCallbackSetException(cause, clientId, mqttTopic);

        Assert.assertEquals(MqttClientErrorCodes.CALLBACK_SET_ERROR, mqttClientCallbackSetException.getCode());
        Assert.assertEquals(cause, mqttClientCallbackSetException.getCause());
        Assert.assertEquals(clientId, mqttClientCallbackSetException.getClientId());
        Assert.assertEquals(mqttTopic, mqttClientCallbackSetException.getTopic());
        Assert.assertEquals(exceptionMessage, mqttClientCallbackSetException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientCallbackSetException.getLocalizedMessage());
    }

    @Test
    public void testMqttClientCleanException() {
        String exceptionMessage = "MqttClient " + clientId + " cannot be terminated properly. This can lead to resource leaks.";

        MqttClientCleanException mqttClientCleanException = new MqttClientCleanException(cause, clientId);

        Assert.assertEquals(MqttClientErrorCodes.CLEAN_ERROR, mqttClientCleanException.getCode());
        Assert.assertEquals(cause, mqttClientCleanException.getCause());
        Assert.assertEquals(clientId, mqttClientCleanException.getClientId());
        Assert.assertEquals(exceptionMessage, mqttClientCleanException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientCleanException.getLocalizedMessage());
    }

    @Test
    public void testMqttClientConnectException() {
        String exceptionMessage = "MqttClient " + clientId + " cannot connect to " + uri + " with username " + username;

        // Without cause
        MqttClientConnectException mqttClientConnectException = new MqttClientConnectException(clientId, username, uri);

        Assert.assertEquals(MqttClientErrorCodes.CONNECT_ERROR, mqttClientConnectException.getCode());
        Assert.assertNull(mqttClientConnectException.getCause());
        Assert.assertEquals(clientId, mqttClientConnectException.getClientId());
        Assert.assertEquals(username, mqttClientConnectException.getUsername());
        Assert.assertEquals(uri, mqttClientConnectException.getUri());
        Assert.assertEquals(exceptionMessage, mqttClientConnectException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientConnectException.getLocalizedMessage());

        // With cause
        mqttClientConnectException = new MqttClientConnectException(cause, clientId, username, uri);

        Assert.assertEquals(MqttClientErrorCodes.CONNECT_ERROR, mqttClientConnectException.getCode());
        Assert.assertEquals(cause, mqttClientConnectException.getCause());
        Assert.assertEquals(clientId, mqttClientConnectException.getClientId());
        Assert.assertEquals(username, mqttClientConnectException.getUsername());
        Assert.assertEquals(uri, mqttClientConnectException.getUri());
        Assert.assertEquals(exceptionMessage, mqttClientConnectException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientConnectException.getLocalizedMessage());
    }

    @Test
    public void testMqttClientDisconnectException() {
        String exceptionMessage = "MqttClient " + clientId + " cannot be disconnected properly.";

        MqttClientDisconnectException mqttClientDisconnectException = new MqttClientDisconnectException(cause, clientId);

        Assert.assertEquals(MqttClientErrorCodes.DISCONNECT_ERROR, mqttClientDisconnectException.getCode());
        Assert.assertEquals(cause, mqttClientDisconnectException.getCause());
        Assert.assertEquals(clientId, mqttClientDisconnectException.getClientId());
        Assert.assertEquals(exceptionMessage, mqttClientDisconnectException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientDisconnectException.getLocalizedMessage());
    }

    @Test
    public void testMqttClientException() {
        String exceptionMessage = "MqttClient " + clientId + " cannot connect to {2} with username {1}";

        // Without cause
        MqttClientException mqttClientException = new MqttClientException(MqttClientErrorCodes.CONNECT_ERROR, clientId);

        Assert.assertEquals(MqttClientErrorCodes.CONNECT_ERROR, mqttClientException.getCode());
        Assert.assertNull(mqttClientException.getCause());
        Assert.assertEquals(clientId, mqttClientException.getClientId());
        Assert.assertEquals(exceptionMessage, mqttClientException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientException.getLocalizedMessage());

        // With cause
        mqttClientException = new MqttClientException(MqttClientErrorCodes.CONNECT_ERROR, cause, clientId);

        Assert.assertEquals(MqttClientErrorCodes.CONNECT_ERROR, mqttClientException.getCode());
        Assert.assertEquals(cause, mqttClientException.getCause());
        Assert.assertEquals(clientId, mqttClientException.getClientId());
        Assert.assertEquals(exceptionMessage, mqttClientException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientException.getLocalizedMessage());
    }

    @Test
    public void testMqttClientNotConnectedException() {
        String exceptionMessage = "MqttClient " + clientId + " is not connected.";

        MqttClientNotConnectedException mqttClientNotConnectedException = new MqttClientNotConnectedException(clientId);

        Assert.assertEquals(MqttClientErrorCodes.NOT_CONNECTED, mqttClientNotConnectedException.getCode());
        Assert.assertNull(mqttClientNotConnectedException.getCause());
        Assert.assertEquals(clientId, mqttClientNotConnectedException.getClientId());
        Assert.assertEquals(exceptionMessage, mqttClientNotConnectedException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientNotConnectedException.getLocalizedMessage());
    }

    @Test
    public void testMqttClientPublishException() {
        String exceptionMessage = "MqttClient " + clientId + " cannot publish MqttMessage(" + mqttPayload.getBody().length + " bytes) to topic: " + mqttTopic;

        MqttClientPublishException mqttClientPublishException = new MqttClientPublishException(cause, clientId, mqttTopic, mqttMessage);

        Assert.assertEquals(MqttClientErrorCodes.PUBLISH_ERROR, mqttClientPublishException.getCode());
        Assert.assertEquals(cause, mqttClientPublishException.getCause());
        Assert.assertEquals(clientId, mqttClientPublishException.getClientId());
        Assert.assertEquals(mqttTopic.toString(), mqttClientPublishException.getTopic());
        Assert.assertEquals(mqttMessage, mqttClientPublishException.getMqttMessage());
        Assert.assertEquals(exceptionMessage, mqttClientPublishException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientPublishException.getLocalizedMessage());
    }

    @Test
    public void testMqttClientSubscribeException() {
        String exceptionMessage = "MqttClient " + clientId + " cannot subscribe to topic: " + mqttTopic;

        MqttClientSubscribeException mqttClientSubscribeException = new MqttClientSubscribeException(cause, clientId, mqttTopic);

        Assert.assertEquals(MqttClientErrorCodes.SUBSCRIBE_ERROR, mqttClientSubscribeException.getCode());
        Assert.assertEquals(cause, mqttClientSubscribeException.getCause());
        Assert.assertEquals(clientId, mqttClientSubscribeException.getClientId());
        Assert.assertEquals(mqttTopic, mqttClientSubscribeException.getTopic());
        Assert.assertEquals(exceptionMessage, mqttClientSubscribeException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientSubscribeException.getLocalizedMessage());
    }

    @Test
    public void testMqttClientTerminateException() {
        String exceptionMessage = "MqttClient " + clientId + " cannot be terminated properly. This can lead to resource leaks.";

        MqttClientTerminateException mqttClientTerminateException = new MqttClientTerminateException(cause, clientId);

        Assert.assertEquals(MqttClientErrorCodes.TERMINATE_ERROR, mqttClientTerminateException.getCode());
        Assert.assertEquals(cause, mqttClientTerminateException.getCause());
        Assert.assertEquals(clientId, mqttClientTerminateException.getClientId());
        Assert.assertEquals(exceptionMessage, mqttClientTerminateException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientTerminateException.getLocalizedMessage());
    }

    @Test
    public void testMqttClientUnsubscribeException() {
        String exceptionMessage = "MqttClient " + clientId + " cannot unsubscribe from topic: " + mqttTopic;

        MqttClientUnsubscribeException mqttClientSubscribeException = new MqttClientUnsubscribeException(cause, clientId, mqttTopic);

        Assert.assertEquals(MqttClientErrorCodes.UNSUBSCRIBE_ERROR, mqttClientSubscribeException.getCode());
        Assert.assertEquals(cause, mqttClientSubscribeException.getCause());
        Assert.assertEquals(clientId, mqttClientSubscribeException.getClientId());
        Assert.assertEquals(mqttTopic, mqttClientSubscribeException.getTopic());
        Assert.assertEquals(exceptionMessage, mqttClientSubscribeException.getMessage());
        Assert.assertEquals(exceptionMessage, mqttClientSubscribeException.getLocalizedMessage());
    }
}
