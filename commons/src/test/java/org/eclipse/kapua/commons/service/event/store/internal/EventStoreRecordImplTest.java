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
package org.eclipse.kapua.commons.service.event.store.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecord;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.math.BigInteger;
import java.util.Date;


@Category(JUnitTests.class)
public class EventStoreRecordImplTest {

    EventStoreRecord eventStoreRecord;

    @Before
    public void initialize() {
        eventStoreRecord = Mockito.mock(EventStoreRecord.class);

        Mockito.when(eventStoreRecord.getContextId()).thenReturn("contextId");
        Mockito.when(eventStoreRecord.getTimestamp()).thenReturn(new Date());
        Mockito.when(eventStoreRecord.getUserId()).thenReturn(new KapuaEid(BigInteger.TEN));
        Mockito.when(eventStoreRecord.getService()).thenReturn("service");
        Mockito.when(eventStoreRecord.getEntityType()).thenReturn("entityType");
        Mockito.when(eventStoreRecord.getEntityId()).thenReturn(new KapuaEid(BigInteger.TEN));
        Mockito.when(eventStoreRecord.getOperation()).thenReturn("operation");
        Mockito.when(eventStoreRecord.getInputs()).thenReturn("input");
        Mockito.when(eventStoreRecord.getOutputs()).thenReturn("output");
        Mockito.when(eventStoreRecord.getStatus()).thenReturn(ServiceEvent.EventStatus.TRIGGERED);
        Mockito.when(eventStoreRecord.getNote()).thenReturn("note");
    }

    @Test
    public void eventStoreRecordImplTest1() {
        EventStoreRecordImpl eventStoreRecordImpl = new EventStoreRecordImpl();
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.TRIGGERED, eventStoreRecordImpl.getStatus());
    }

    @Test
    public void eventStoreRecordImplTest2() {
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordImpl eventStoreRecordImpl = new EventStoreRecordImpl(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, eventStoreRecordImpl.getScopeId());
        }
    }

    @Test
    public void eventStoreRecordImplTest3() throws KapuaException {
        EventStoreRecordImpl eventStoreRecordImpl = new EventStoreRecordImpl(eventStoreRecord);

        Assert.assertEquals("Expected and actual values should be the same.", "contextId", eventStoreRecordImpl.getContextId());
        Assert.assertEquals("Expected and actual values should be the same.", new Date().toString(), eventStoreRecordImpl.getTimestamp().toString());
        Assert.assertEquals("Expected and actual values should be the same.", new KapuaEid(BigInteger.TEN), eventStoreRecordImpl.getUserId());
        Assert.assertEquals("Expected and actual values should be the same.", "service", eventStoreRecordImpl.getService());
        Assert.assertEquals("Expected and actual values should be the same.", "entityType", eventStoreRecordImpl.getEntityType());
        Assert.assertEquals("Expected and actual values should be the same.", new KapuaEid(BigInteger.TEN), eventStoreRecordImpl.getEntityId());
        Assert.assertEquals("Expected and actual values should be the same.", "operation", eventStoreRecordImpl.getOperation());
        Assert.assertEquals("Expected and actual values should be the same.", "input", eventStoreRecordImpl.getInputs());
        Assert.assertEquals("Expected and actual values should be the same.", "output", eventStoreRecordImpl.getOutputs());
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.TRIGGERED, eventStoreRecordImpl.getStatus());
        Assert.assertEquals("Expected and actual values should be the same.", "note", eventStoreRecordImpl.getNote());
    }

    @Test
    public void setAndGetContextIdTest() throws KapuaException {
        String contextId = "ContextId";
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl(scopeId);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getContextId());
            eventStoreRecordImpl1.setContextId(contextId);
            Assert.assertEquals("Expected and actual values should be the same.", contextId, eventStoreRecordImpl1.getContextId());
            eventStoreRecordImpl1.setContextId(null);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getContextId());
        }

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl();
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getContextId());
        eventStoreRecordImpl2.setContextId(contextId);
        Assert.assertEquals("Expected and actual values should be the same.", contextId, eventStoreRecordImpl2.getContextId());
        eventStoreRecordImpl2.setContextId(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getContextId());

        EventStoreRecordImpl eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertEquals("Expected and actual values should be the same.", "contextId", eventStoreRecordImpl3.getContextId());
        eventStoreRecordImpl3.setContextId(contextId);
        Assert.assertEquals("Expected and actual values should be the same.", contextId, eventStoreRecordImpl3.getContextId());
        eventStoreRecordImpl3.setContextId(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getContextId());
    }

    @Test
    public void setAndGetTimestampTest() throws KapuaException {
        Date timeStamp = new Date();
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl(scopeId);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getTimestamp());
            eventStoreRecordImpl1.setTimestamp(timeStamp);
            Assert.assertEquals("Expected and actual values should be the same.", timeStamp, eventStoreRecordImpl1.getTimestamp());
            eventStoreRecordImpl1.setTimestamp(null);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getTimestamp());
        }

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl();
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getTimestamp());
        eventStoreRecordImpl2.setTimestamp(timeStamp);
        Assert.assertEquals("Expected and actual values should be the same.", timeStamp, eventStoreRecordImpl2.getTimestamp());
        eventStoreRecordImpl2.setTimestamp(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getTimestamp());

        EventStoreRecord eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertEquals(new Date().toString(), eventStoreRecordImpl3.getTimestamp().toString());
        eventStoreRecordImpl3.setTimestamp(timeStamp);
        Assert.assertEquals("Expected and actual values should be the same.", timeStamp, eventStoreRecordImpl3.getTimestamp());
        eventStoreRecordImpl3.setTimestamp(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getTimestamp());
    }

    @Test
    public void setAndGetUserIdTest() throws KapuaException {
        KapuaId userId = new KapuaIdImpl(BigInteger.ONE);
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl(scopeId);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getUserId());
            eventStoreRecordImpl1.setUserId(userId);
            Assert.assertEquals("Expected and actual values should be the same.", userId, eventStoreRecordImpl1.getUserId());
            eventStoreRecordImpl1.setUserId(null);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getUserId());
        }

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl();
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getUserId());
        eventStoreRecordImpl2.setUserId(userId);
        Assert.assertEquals("Expected and actual values should be the same.", userId, eventStoreRecordImpl2.getUserId());
        eventStoreRecordImpl2.setUserId(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getUserId());

        EventStoreRecord eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertEquals("Expected and actual values should be the same.", new KapuaEid(BigInteger.TEN), eventStoreRecordImpl3.getUserId());
        eventStoreRecordImpl3.setUserId(userId);
        Assert.assertEquals("Expected and actual values should be the same.", userId, eventStoreRecordImpl3.getUserId());
        eventStoreRecordImpl3.setUserId(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getUserId());
    }

    @Test
    public void setAndGetServiceTest() throws KapuaException {
        String service = "Service";
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl(scopeId);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getService());
            eventStoreRecordImpl1.setService(service);
            Assert.assertEquals("Expected and actual values should be the same.", service, eventStoreRecordImpl1.getService());
            eventStoreRecordImpl1.setService(null);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getService());
        }

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl();
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getService());
        eventStoreRecordImpl2.setService(service);
        Assert.assertEquals("Expected and actual values should be the same.", service, eventStoreRecordImpl2.getService());
        eventStoreRecordImpl2.setService(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getService());

        EventStoreRecord eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertEquals("Expected and actual values should be the same.", "service", eventStoreRecordImpl3.getService());
        eventStoreRecordImpl3.setService(service);
        Assert.assertEquals("Expected and actual values should be the same.", service, eventStoreRecordImpl3.getService());
        eventStoreRecordImpl3.setService(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getService());
    }

    @Test
    public void setAndGetEntityTypeTest() throws KapuaException {
        String entityType = "EntityType";
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl(scopeId);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getEntityType());
            eventStoreRecordImpl1.setEntityType(entityType);
            Assert.assertEquals("Expected and actual values should be the same.", entityType, eventStoreRecordImpl1.getEntityType());
            eventStoreRecordImpl1.setEntityType(null);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getEntityType());
        }

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl();
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getEntityType());
        eventStoreRecordImpl2.setEntityType(entityType);
        Assert.assertEquals("Expected and actual values should be the same.", entityType, eventStoreRecordImpl2.getEntityType());
        eventStoreRecordImpl2.setEntityType(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getEntityType());

        EventStoreRecord eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertEquals("entityType", eventStoreRecordImpl3.getEntityType());
        eventStoreRecordImpl3.setEntityType(entityType);
        Assert.assertEquals("Expected and actual values should be the same.", entityType, eventStoreRecordImpl3.getEntityType());
        eventStoreRecordImpl3.setEntityType(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getEntityType());
    }

    @Test
    public void setAndGetScopeIdTest() throws KapuaException {
        KapuaId scopeId1 = new KapuaIdImpl(BigInteger.ONE);
        KapuaId scopeId2 = new KapuaIdImpl(BigInteger.TEN);

        EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl(scopeId1);
        Assert.assertEquals("Expected and actual values should be the same.", scopeId1, eventStoreRecordImpl1.getScopeId());
        eventStoreRecordImpl1.setScopeId(scopeId2);
        Assert.assertEquals("Expected and actual values should be the same.", scopeId2, eventStoreRecordImpl1.getScopeId());
        eventStoreRecordImpl1.setScopeId(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl1.getScopeId());

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl();
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getScopeId());
        eventStoreRecordImpl2.setScopeId(scopeId2);
        Assert.assertEquals("Expected and actual values should be the same.", scopeId2, eventStoreRecordImpl2.getScopeId());
        eventStoreRecordImpl2.setScopeId(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getScopeId());

        EventStoreRecord eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getScopeId());
        eventStoreRecordImpl3.setScopeId(scopeId2);
        Assert.assertEquals("Expected and actual values should be the same.", scopeId2, eventStoreRecordImpl3.getScopeId());
        eventStoreRecordImpl3.setScopeId(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getScopeId());
    }

    @Test
    public void setAndEntityIdTest() throws KapuaException {
        KapuaId entityId = new KapuaIdImpl(BigInteger.ONE);
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl(scopeId);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getEntityId());
            eventStoreRecordImpl1.setEntityId(entityId);
            Assert.assertEquals("Expected and actual values should be the same.", entityId, eventStoreRecordImpl1.getEntityId());
            eventStoreRecordImpl1.setEntityId(null);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getEntityId());
        }

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl();
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getEntityId());
        eventStoreRecordImpl2.setEntityId(entityId);
        Assert.assertEquals("Expected and actual values should be the same.", entityId, eventStoreRecordImpl2.getEntityId());
        eventStoreRecordImpl2.setEntityId(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getEntityId());

        EventStoreRecord eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertEquals("Expected and actual values should be the same.", new KapuaEid(BigInteger.TEN), eventStoreRecordImpl3.getEntityId());
        eventStoreRecordImpl3.setEntityId(entityId);
        Assert.assertEquals("Expected and actual values should be the same.", entityId, eventStoreRecordImpl3.getEntityId());
        eventStoreRecordImpl3.setEntityId(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getEntityId());
    }

    @Test
    public void setAndGetOperationIdTest() throws KapuaException {
        String operation = "Operation";
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl(scopeId);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getOperation());
            eventStoreRecordImpl1.setOperation(operation);
            Assert.assertEquals("Expected and actual values should be the same.", operation, eventStoreRecordImpl1.getOperation());
            eventStoreRecordImpl1.setOperation(null);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getOperation());
        }

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl();
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getOperation());
        eventStoreRecordImpl2.setOperation(operation);
        Assert.assertEquals("Expected and actual values should be the same.", operation, eventStoreRecordImpl2.getOperation());
        eventStoreRecordImpl2.setOperation(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getOperation());

        EventStoreRecord eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertEquals("operation", eventStoreRecordImpl3.getOperation());
        eventStoreRecordImpl3.setOperation(operation);
        Assert.assertEquals("Expected and actual values should be the same.", operation, eventStoreRecordImpl3.getOperation());
        eventStoreRecordImpl3.setOperation(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getOperation());
    }

    @Test
    public void setAndGetInputsTest() throws KapuaException {
        String inputs = "Inputs";
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl(scopeId);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getInputs());
            eventStoreRecordImpl1.setInputs(inputs);
            Assert.assertEquals("Expected and actual values should be the same.", inputs, eventStoreRecordImpl1.getInputs());
            eventStoreRecordImpl1.setInputs(null);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getInputs());
        }

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl();
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getInputs());
        eventStoreRecordImpl2.setInputs(inputs);
        Assert.assertEquals("Expected and actual values should be the same.", inputs, eventStoreRecordImpl2.getInputs());
        eventStoreRecordImpl2.setInputs(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getInputs());

        EventStoreRecord eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertEquals("Expected and actual values should be the same.", "input", eventStoreRecordImpl3.getInputs());
        eventStoreRecordImpl3.setInputs(inputs);
        Assert.assertEquals("Expected and actual values should be the same.", inputs, eventStoreRecordImpl3.getInputs());
        eventStoreRecordImpl3.setInputs(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getInputs());
    }

    @Test
    public void setAndGetOutputsTest() throws KapuaException {
        String outputs = "Outputs";
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl(scopeId);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getOutputs());
            eventStoreRecordImpl1.setOutputs(outputs);
            Assert.assertEquals("Expected and actual values should be the same.", outputs, eventStoreRecordImpl1.getOutputs());
            eventStoreRecordImpl1.setOutputs(null);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getOutputs());
        }

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl();
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getOutputs());
        eventStoreRecordImpl2.setOutputs(outputs);
        Assert.assertEquals("Expected and actual values should be the same.", outputs, eventStoreRecordImpl2.getOutputs());
        eventStoreRecordImpl2.setOutputs(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getOutputs());

        EventStoreRecord eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertEquals("output", eventStoreRecordImpl3.getOutputs());
        eventStoreRecordImpl3.setOutputs(outputs);
        Assert.assertEquals("Expected and actual values should be the same.", outputs, eventStoreRecordImpl3.getOutputs());
        eventStoreRecordImpl3.setOutputs(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getOutputs());
    }

    @Test
    public void setStatusTest() throws KapuaException {
        EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl();
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.TRIGGERED, eventStoreRecordImpl1.getStatus());
        eventStoreRecordImpl1.setStatus(ServiceEvent.EventStatus.SENT);
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.SENT, eventStoreRecordImpl1.getStatus());
        eventStoreRecordImpl1.setStatus(ServiceEvent.EventStatus.SEND_ERROR);
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.SEND_ERROR, eventStoreRecordImpl1.getStatus());
        eventStoreRecordImpl1.setStatus(ServiceEvent.EventStatus.TRIGGERED);
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.TRIGGERED, eventStoreRecordImpl1.getStatus());
        eventStoreRecordImpl1.setStatus(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl1.getStatus());

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl(new KapuaIdImpl(BigInteger.ONE));
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getStatus());
        eventStoreRecordImpl2.setStatus(ServiceEvent.EventStatus.SENT);
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.SENT, eventStoreRecordImpl2.getStatus());
        eventStoreRecordImpl2.setStatus(ServiceEvent.EventStatus.SEND_ERROR);
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.SEND_ERROR, eventStoreRecordImpl2.getStatus());
        eventStoreRecordImpl2.setStatus(ServiceEvent.EventStatus.TRIGGERED);
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.TRIGGERED, eventStoreRecordImpl2.getStatus());
        eventStoreRecordImpl2.setStatus(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getStatus());

        EventStoreRecord eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.TRIGGERED, eventStoreRecordImpl3.getStatus());
        eventStoreRecordImpl3.setStatus(ServiceEvent.EventStatus.SENT);
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.SENT, eventStoreRecordImpl3.getStatus());
        eventStoreRecordImpl3.setStatus(ServiceEvent.EventStatus.SEND_ERROR);
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.SEND_ERROR, eventStoreRecordImpl3.getStatus());
        eventStoreRecordImpl3.setStatus(ServiceEvent.EventStatus.TRIGGERED);
        Assert.assertEquals("Expected and actual values should be the same.", ServiceEvent.EventStatus.TRIGGERED, eventStoreRecordImpl3.getStatus());
        eventStoreRecordImpl3.setStatus(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getStatus());
    }

    @Test
    public void setAndGetNoteTest() throws KapuaException {
        String note = "Note";
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordImpl eventStoreRecordImpl1 = new EventStoreRecordImpl(scopeId);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getNote());
            eventStoreRecordImpl1.setNote(note);
            Assert.assertEquals("Expected and actual values should be the same.", note, eventStoreRecordImpl1.getNote());
            eventStoreRecordImpl1.setNote(null);
            Assert.assertNull("Null expected.", eventStoreRecordImpl1.getNote());
        }

        EventStoreRecordImpl eventStoreRecordImpl2 = new EventStoreRecordImpl();
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getNote());
        eventStoreRecordImpl2.setNote(note);
        Assert.assertEquals("Expected and actual values should be the same.", note, eventStoreRecordImpl2.getNote());
        eventStoreRecordImpl2.setNote(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl2.getNote());

        EventStoreRecord eventStoreRecordImpl3 = new EventStoreRecordImpl(eventStoreRecord);
        Assert.assertEquals("note", eventStoreRecordImpl3.getNote());
        eventStoreRecordImpl3.setNote(note);
        Assert.assertEquals("Expected and actual values should be the same.", note, eventStoreRecordImpl3.getNote());
        eventStoreRecordImpl3.setNote(null);
        Assert.assertNull("Null expected.", eventStoreRecordImpl3.getNote());
    }
} 
