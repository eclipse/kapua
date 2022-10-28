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
package org.eclipse.kapua.integration.misc;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.event.JsonServiceEventMarshaler;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.StringWriter;


@Category(JUnitTests.class)
public class JsonServiceEventMarshalerTest {

    ServiceEvent serviceEvent;
    JsonServiceEventMarshaler jsonServiceEventMarshaler;
    StringWriter stringWriter;

    @Before
    public void createInstanceOfClasses() {
        serviceEvent = new ServiceEvent();
        jsonServiceEventMarshaler = new JsonServiceEventMarshaler();
        stringWriter = new StringWriter();
    }

    @Test(expected = ServiceEventBusException.class)
    public void marshalJsonWithNullContextTest() throws Exception {
        jsonServiceEventMarshaler.marshal(null);
    }

    @Test
    public void marshalJsonWithoutContextTest() throws ServiceEventBusException {
        stringWriter.write("{\n}");
        String expectedValues = stringWriter.toString();
        XmlUtil.setContextProvider(new TestJAXBContextProvider());
        Assert.assertEquals("Expected and actual values should be the same!", expectedValues, jsonServiceEventMarshaler.marshal(serviceEvent));
    }

    @Test
    public void marshalJsonWithContextTest() throws ServiceEventBusException {
        stringWriter.write("{\n" +
                "   \"id\" : \"id\",\n" +
                "   \"contextId\" : \"contextId\",\n" +
                "   \"entityType\" : \"entityType\",\n" +
                "   \"status\" : \"SENT\",\n" +
                "   \"note\" : \"note\"\n" +
                "}");

        String expectedValues = stringWriter.toString();
        serviceEvent.setId("id");
        serviceEvent.setContextId("contextId");
        serviceEvent.setEntityType("entityType");
        serviceEvent.setStatus(ServiceEvent.EventStatus.SENT);
        serviceEvent.setNote("note");

        XmlUtil.setContextProvider(new TestJAXBContextProvider());
        Assert.assertEquals("Expected and actual values should be the same!", expectedValues, jsonServiceEventMarshaler.marshal(serviceEvent));
    }

    @Test(expected = NullPointerException.class)
    public void unmarshalJsonWithNullContextTest() throws Exception {
        jsonServiceEventMarshaler.unmarshal(null);
    }

    @Test
    public void unmarshalJsonWithContextTest() throws KapuaException {
        XmlUtil.setContextProvider(new TestJAXBContextProvider());
        ServiceEvent elements = jsonServiceEventMarshaler.unmarshal("{\n" +
                "   \"id\" : \"id\",\n" +
                "   \"contextId\" : \"contextId\",\n" +
                "   \"entityType\" : \"entityType\",\n" +
                "   \"status\" : \"SENT\",\n" +
                "   \"note\" : \"note\"\n" +
                "}");

        Assert.assertEquals("Expected and actual values should be the same!", "id", elements.getId());
        Assert.assertEquals("Expected and actual values should be the same!", "contextId", elements.getContextId());
        Assert.assertEquals("Expected and actual values should be the same!", "entityType", elements.getEntityType());
        Assert.assertEquals("Expected and actual values should be the same!", ServiceEvent.EventStatus.SENT, elements.getStatus());
    }

    @Test
    public void getContentTypeTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "application/json", jsonServiceEventMarshaler.getContentType());
    }
}
