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
package org.eclipse.kapua.app.api.core.model.data;

import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;
import java.util.UUID;


@Category(JUnitTests.class)
public class JsonDatastoreMessageTest {

    DatastoreMessage datastoreMessage;
    UUID id;
    StorableId storableId;
    Date timestamp, receivedOn, sentOn, capturedOn;
    KapuaId scopeId, deviceId;
    String clientId;
    KapuaPosition kapuaPosition;
    KapuaDataChannel kapuaDataChannel;
    KapuaPayload kapuaPayload;

    @Before
    public void initialize() {
        datastoreMessage = Mockito.mock(DatastoreMessage.class);
        id = new UUID(10L, 100L);
        storableId = Mockito.mock(StorableId.class);
        timestamp = new Date();
        scopeId = KapuaId.ONE;
        deviceId = KapuaId.ONE;
        clientId = "clientID";
        receivedOn = new Date();
        sentOn = new Date();
        capturedOn = new Date();
        kapuaPosition = Mockito.mock(KapuaPosition.class);
        kapuaDataChannel = Mockito.mock(KapuaDataChannel.class);
        kapuaPayload = Mockito.mock(KapuaPayload.class);

        Mockito.when(datastoreMessage.getId()).thenReturn(id);
        Mockito.when(datastoreMessage.getDatastoreId()).thenReturn(storableId);
        Mockito.when(datastoreMessage.getTimestamp()).thenReturn(timestamp);
        Mockito.when(datastoreMessage.getScopeId()).thenReturn(scopeId);
        Mockito.when(datastoreMessage.getDeviceId()).thenReturn(deviceId);
        Mockito.when(datastoreMessage.getClientId()).thenReturn(clientId);
        Mockito.when(datastoreMessage.getReceivedOn()).thenReturn(receivedOn);
        Mockito.when(datastoreMessage.getSentOn()).thenReturn(sentOn);
        Mockito.when(datastoreMessage.getCapturedOn()).thenReturn(capturedOn);
        Mockito.when(datastoreMessage.getPosition()).thenReturn(kapuaPosition);
        Mockito.when(datastoreMessage.getChannel()).thenReturn(kapuaDataChannel);
        Mockito.when(datastoreMessage.getPayload()).thenReturn(kapuaPayload);
    }

    @Test
    public void jsonDatastoreMessageWithoutParameterTest() {
        JsonDatastoreMessage jsonDatastoreMessage = new JsonDatastoreMessage();

        Assert.assertNull("Null expected.", jsonDatastoreMessage.getId());
        Assert.assertNull("Null expected.", jsonDatastoreMessage.getDatastoreId());
        Assert.assertNull("Null expected.", jsonDatastoreMessage.getTimestamp());
        Assert.assertNull("Null expected.", jsonDatastoreMessage.getScopeId());
        Assert.assertNull("Null expected.", jsonDatastoreMessage.getDeviceId());
        Assert.assertNull("Null expected.", jsonDatastoreMessage.getClientId());
        Assert.assertNull("Null expected.", jsonDatastoreMessage.getReceivedOn());
        Assert.assertNull("Null expected.", jsonDatastoreMessage.getSentOn());
        Assert.assertNull("Null expected.", jsonDatastoreMessage.getCapturedOn());
        Assert.assertNull("Null expected.", jsonDatastoreMessage.getPosition());
        Assert.assertNull("Null expected.", jsonDatastoreMessage.getChannel());
        Assert.assertNull("Null expected.", jsonDatastoreMessage.getPayload());
    }

    @Test
    public void jsonDatastoreMessageWithParameterTest() {
        JsonDatastoreMessage jsonDatastoreMessage = new JsonDatastoreMessage(datastoreMessage);

        Assert.assertEquals("Expected and actual values should be the same.", id, jsonDatastoreMessage.getId());
        Assert.assertEquals("Expected and actual values should be the same.", storableId, jsonDatastoreMessage.getDatastoreId());
        Assert.assertEquals("Expected and actual values should be the same.", timestamp, jsonDatastoreMessage.getTimestamp());
        Assert.assertEquals("Expected and actual values should be the same.", scopeId, jsonDatastoreMessage.getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", deviceId, jsonDatastoreMessage.getDeviceId());
        Assert.assertEquals("Expected and actual values should be the same.", clientId, jsonDatastoreMessage.getClientId());
        Assert.assertEquals("Expected and actual values should be the same.", receivedOn, jsonDatastoreMessage.getReceivedOn());
        Assert.assertEquals("Expected and actual values should be the same.", sentOn, jsonDatastoreMessage.getSentOn());
        Assert.assertEquals("Expected and actual values should be the same.", capturedOn, jsonDatastoreMessage.getCapturedOn());
        Assert.assertEquals("Expected and actual values should be the same.", kapuaPosition, jsonDatastoreMessage.getPosition());
        Assert.assertEquals("Expected and actual values should be the same.", kapuaDataChannel, jsonDatastoreMessage.getChannel());
        Assert.assertNotNull("NotNull expected.", jsonDatastoreMessage.getPayload());
    }

    @Test(expected = NullPointerException.class)
    public void jsonDatastoreMessageWithNullParameterTest() {
        new JsonDatastoreMessage(null);
    }

    @Test
    public void setAndGetDatastoreIdTest() {
        StorableId datastoreId = Mockito.mock(StorableId.class);
        JsonDatastoreMessage jsonDatastoreMessage1 = new JsonDatastoreMessage();
        JsonDatastoreMessage jsonDatastoreMessage2 = new JsonDatastoreMessage(datastoreMessage);

        jsonDatastoreMessage1.setDatastoreId(datastoreId);
        jsonDatastoreMessage2.setDatastoreId(datastoreId);

        Assert.assertEquals("Expected and actual values should be the same.", datastoreId, jsonDatastoreMessage1.getDatastoreId());
        Assert.assertEquals("Expected and actual values should be the same.", datastoreId, jsonDatastoreMessage2.getDatastoreId());

        jsonDatastoreMessage1.setDatastoreId(null);
        jsonDatastoreMessage2.setDatastoreId(null);

        Assert.assertNull("Null expected.", jsonDatastoreMessage1.getDatastoreId());
        Assert.assertNull("Null expected.", jsonDatastoreMessage2.getDatastoreId());
    }

    @Test
    public void setAndGetTimestampTest() {
        Date[] dates = {new Date(), new Date(99L), new Date(99999999999999L)};
        JsonDatastoreMessage jsonDatastoreMessage1 = new JsonDatastoreMessage();
        JsonDatastoreMessage jsonDatastoreMessage2 = new JsonDatastoreMessage(datastoreMessage);

        for (Date newTimestamp : dates) {
            jsonDatastoreMessage1.setTimestamp(newTimestamp);
            jsonDatastoreMessage2.setTimestamp(newTimestamp);

            Assert.assertEquals("Expected and actual values should be the same.", newTimestamp, jsonDatastoreMessage1.getTimestamp());
            Assert.assertEquals("Expected and actual values should be the same.", newTimestamp, jsonDatastoreMessage2.getTimestamp());
        }

        jsonDatastoreMessage1.setTimestamp(null);
        jsonDatastoreMessage2.setTimestamp(null);

        Assert.assertNull("Null expected.", jsonDatastoreMessage1.getTimestamp());
        Assert.assertNull("Null expected.", jsonDatastoreMessage2.getTimestamp());
    }
} 