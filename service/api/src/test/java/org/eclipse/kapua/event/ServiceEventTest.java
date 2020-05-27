/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.event;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdStatic;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;
import java.util.Date;

@Category(JUnitTests.class)
public class ServiceEventTest extends Assert {

    @Test
    public void setAndGetIdTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        String[] ids = {"id", null};

        assertNull("Null expected.", serviceEvent.getId());
        for (String id : ids) {
            serviceEvent.setId(id);
            assertEquals("Expected and actual values should be the same.", id, serviceEvent.getId());
        }
    }

    @Test
    public void setAndGetContextIdTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        String[] contextIds = {"contextId.", null};

        assertNull("Null expected", serviceEvent.getContextId());
        for (String contextId : contextIds) {
            serviceEvent.setContextId(contextId);
            assertEquals("Expected and actual values should be the same.", contextId, serviceEvent.getContextId());
        }
    }

    @Test
    public void setAndGetTimestampTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        Date[] timestamps = {new Date(), null};

        assertNull("Null expected.", serviceEvent.getTimestamp());
        for (Date timestamp : timestamps) {
            serviceEvent.setTimestamp(timestamp);
            assertEquals("Expected and actual values should be the same.", timestamp, serviceEvent.getTimestamp());
        }
    }

    @Test
    public void setAndGetUserIdTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        KapuaId[] userIds = {new KapuaIdStatic(BigInteger.ONE), null};

        assertNull("Null expected.", serviceEvent.getUserId());
        for (KapuaId userId : userIds) {
            serviceEvent.setUserId(userId);
            assertEquals("Expected and actual values should be the same.", userId, serviceEvent.getUserId());
        }
    }

    @Test
    public void setAndGetServiceTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        String[] services = {"service", null};

        assertNull("Null expected.", serviceEvent.getService());
        for (String service : services) {
            serviceEvent.setService(service);
            assertEquals("Expected and actual values should be the same.", service, serviceEvent.getService());
        }
    }

    @Test
    public void setAndGetEntityTypeTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        String[] entityTypes = {"entityType", null};

        assertNull("Null expected.", serviceEvent.getEntityType());
        for (String entityType : entityTypes) {
            serviceEvent.setEntityType(entityType);
            assertEquals("Expected and actual values should be the same.", entityType, serviceEvent.getEntityType());
        }
    }

    @Test
    public void setAndGetScopeIdTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        KapuaId[] scopeIds = {new KapuaIdStatic(BigInteger.ONE), null};

        assertNull("Null expected.", serviceEvent.getScopeId());
        for (KapuaId scopeId : scopeIds) {
            serviceEvent.setScopeId(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, serviceEvent.getScopeId());
        }
    }

    @Test
    public void setAndGetEntityScopeIdTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        KapuaId[] entityScopeIds = {new KapuaIdStatic(BigInteger.ONE), null};

        assertNull("Null expected.", serviceEvent.getEntityScopeId());
        for (KapuaId entityScopeId : entityScopeIds) {
            serviceEvent.setEntityScopeId(entityScopeId);
            assertEquals("Expected and actual values should be the same.", entityScopeId, serviceEvent.getEntityScopeId());
        }
    }

    @Test
    public void setAndGetEntityIdTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        KapuaId[] entityIds = {new KapuaIdStatic(BigInteger.ONE), null};

        assertNull("Null expected", serviceEvent.getEntityId());
        for (KapuaId entityId : entityIds) {
            serviceEvent.setEntityId(entityId);
            assertEquals("Expected and actual values should be the same.", entityId, serviceEvent.getEntityId());
        }
    }

    @Test
    public void setAndGetOperationTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        String[] operations = {"operation", null};

        assertNull("Null expected.", serviceEvent.getOperation());
        for (String operation : operations) {
            serviceEvent.setOperation(operation);
            assertEquals("Expected and actual values should be the same.", operation, serviceEvent.getOperation());
        }
    }

    @Test
    public void setAndGetInputsTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        String[] inputs = {"inputs", null};

        assertNull("Null expected.", serviceEvent.getInputs());
        for (String input : inputs) {
            serviceEvent.setInputs(input);
            assertEquals("Expected and actual values should be the same.", input, serviceEvent.getInputs());
        }
    }

    @Test
    public void setAndGetOutputsTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        String[] outputs = {"outputs", null};

        assertNull("Null expected.", serviceEvent.getOutputs());
        for (String output : outputs) {
            serviceEvent.setOutputs(output);
            assertEquals("Expected and actual values should be the same.", output, serviceEvent.getOutputs());
        }
    }

    @Test
    public void setAndGetStatusTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        ServiceEvent.EventStatus[] eventStatus = {null, ServiceEvent.EventStatus.TRIGGERED, ServiceEvent.EventStatus.SEND_ERROR, ServiceEvent.EventStatus.SENT};

        assertNull("Null expected.", serviceEvent.getStatus());
        for (ServiceEvent.EventStatus status : eventStatus) {
            serviceEvent.setStatus(status);
            assertEquals("Expected and actual values should be the same.", status, serviceEvent.getStatus());
        }
    }

    @Test
    public void setAndGetNoteTest() {
        ServiceEvent serviceEvent = new ServiceEvent();
        String[] notes = {"notes", null};

        assertNull("Null expected.", serviceEvent.getNote());
        for (String note : notes) {
            serviceEvent.setNote(note);
            assertEquals("Expected and actual values should be the same.", note, serviceEvent.getNote());
        }
    }
}
