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
package org.eclipse.kapua.transport.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.transport.exception.model.TestCodesTransportClientException;
import org.eclipse.kapua.transport.exception.model.TestTransportMessage;
import org.eclipse.kapua.transport.message.TransportMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.text.MessageFormat;

/**
 * {@link TransportException}s tests.
 *
 * @since 1.0.0
 */
@Category(JUnitTests.class)
public class TransportExceptionTest {

    private final Throwable cause = new Throwable("This is the cause");

    private final String serverIp = "serverIp";
    private final Long aTimeout = 15000L;
    private final TransportMessage<?, ?> transportMessage = new TestTransportMessage();

    @Test
    public void testTransportErrorCodesHaveMessages() {
        for (TransportErrorCodes errorCode : TransportErrorCodes.values()) {
            TransportException mqttClientException = new TestCodesTransportClientException(errorCode);

            Assert.assertNotEquals("TransportErrorCodes." + errorCode + " doesn't have an error message", "Error: ", mqttClientException.getMessage());
            Assert.assertNotEquals("TransportErrorCodes." + errorCode + " doesn't have an error message", "Error: ", mqttClientException.getLocalizedMessage());
        }
    }

    @Test
    public void testTransportClientGetException() {
        String exceptionMessage = "Cannot get an instance of the TransportClient to connect to host: " + serverIp;

        // Without cause
        TransportClientGetException transportClientGetException = new TransportClientGetException(serverIp);

        Assert.assertEquals(TransportErrorCodes.CLIENT_GET, transportClientGetException.getCode());
        Assert.assertNull(transportClientGetException.getCause());
        Assert.assertEquals(serverIp, transportClientGetException.getServerIp());
        Assert.assertEquals(exceptionMessage, transportClientGetException.getMessage());
        Assert.assertEquals(exceptionMessage, transportClientGetException.getLocalizedMessage());

        // With cause
        exceptionMessage = exceptionMessage + ". Caused by: " + cause.getMessage();

        transportClientGetException = new TransportClientGetException(cause, serverIp);

        Assert.assertEquals(TransportErrorCodes.CLIENT_GET_WITH_CAUSE, transportClientGetException.getCode());
        Assert.assertEquals(cause, transportClientGetException.getCause());
        Assert.assertEquals(serverIp, transportClientGetException.getServerIp());
        Assert.assertEquals(exceptionMessage, transportClientGetException.getMessage());
        Assert.assertEquals(exceptionMessage, transportClientGetException.getLocalizedMessage());
    }

    @Test
    public void testTransportClientPoolExhaustedException() {
        String exceptionMessage = "Cannot get an instance of the TransportClient to connect to host " + serverIp + " within the configured timeout of " + MessageFormat.format("{0}", aTimeout) + "ms";

        // Without cause
        TransportClientPoolExhaustedException transportClientPoolExhaustedException = new TransportClientPoolExhaustedException(serverIp, aTimeout);

        Assert.assertEquals(TransportErrorCodes.CLIENT_POOL_EXHAUSTED, transportClientPoolExhaustedException.getCode());
        Assert.assertNull(transportClientPoolExhaustedException.getCause());
        Assert.assertEquals(serverIp, transportClientPoolExhaustedException.getServerIp());
        Assert.assertEquals(aTimeout, transportClientPoolExhaustedException.getBorrowWaitTimeout());
        Assert.assertEquals(exceptionMessage, transportClientPoolExhaustedException.getMessage());
        Assert.assertEquals(exceptionMessage, transportClientPoolExhaustedException.getLocalizedMessage());

        // With cause
        transportClientPoolExhaustedException = new TransportClientPoolExhaustedException(cause, serverIp, aTimeout);

        Assert.assertEquals(TransportErrorCodes.CLIENT_POOL_EXHAUSTED, transportClientPoolExhaustedException.getCode());
        Assert.assertEquals(cause, transportClientPoolExhaustedException.getCause());
        Assert.assertEquals(serverIp, transportClientPoolExhaustedException.getServerIp());
        Assert.assertEquals(aTimeout, transportClientPoolExhaustedException.getBorrowWaitTimeout());
        Assert.assertEquals(exceptionMessage, transportClientPoolExhaustedException.getMessage());
        Assert.assertEquals(exceptionMessage, transportClientPoolExhaustedException.getLocalizedMessage());
    }

    @Test
    public void testTransportSendException() {
        String exceptionMessage = "An error occurred when sending the message: " + transportMessage;

        // Without cause
        TransportSendException transportSendException = new TransportSendException(transportMessage);

        Assert.assertEquals(TransportErrorCodes.SEND_ERROR, transportSendException.getCode());
        Assert.assertNull(transportSendException.getCause());
        Assert.assertEquals(transportMessage, transportSendException.getRequestMessage());
        Assert.assertEquals(exceptionMessage, transportSendException.getMessage());
        Assert.assertEquals(exceptionMessage, transportSendException.getLocalizedMessage());

        // With cause
        exceptionMessage = exceptionMessage + ". Caused by: " + cause.getMessage();

        transportSendException = new TransportSendException(cause, transportMessage);

        Assert.assertEquals(TransportErrorCodes.SEND_ERROR_WITH_CAUSE, transportSendException.getCode());
        Assert.assertEquals(cause, transportSendException.getCause());
        Assert.assertEquals(transportMessage, transportSendException.getRequestMessage());
        Assert.assertEquals(exceptionMessage, transportSendException.getMessage());
        Assert.assertEquals(exceptionMessage, transportSendException.getLocalizedMessage());
    }

    @Test
    public void testTransportTimeoutException() {
        String exceptionMessage = "The request has not received a response within the timeout of: " + MessageFormat.format("{0}", aTimeout) + "ms";

        TransportTimeoutException transportTimeoutException = new TransportTimeoutException(aTimeout);

        Assert.assertEquals(TransportErrorCodes.TIMEOUT, transportTimeoutException.getCode());
        Assert.assertNull(transportTimeoutException.getCause());
        Assert.assertEquals(aTimeout, transportTimeoutException.getTimeout());
        Assert.assertEquals(exceptionMessage, transportTimeoutException.getMessage());
        Assert.assertEquals(exceptionMessage, transportTimeoutException.getLocalizedMessage());
    }
}
