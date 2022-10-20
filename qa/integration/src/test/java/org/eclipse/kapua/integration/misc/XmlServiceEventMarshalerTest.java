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
import org.eclipse.kapua.commons.event.XmlServiceEventMarshaler;
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
public class XmlServiceEventMarshalerTest {

    ServiceEvent serviceEvent;
    XmlServiceEventMarshaler xmlServiceEventMarshaler;
    StringWriter stringWriter;

    @Before
    public void createInstanceOfClasses() {
        serviceEvent = new ServiceEvent();
        xmlServiceEventMarshaler = new XmlServiceEventMarshaler();
        stringWriter = new StringWriter();
    }

    @Test
    public void marshalXmlWithoutContextTest() throws ServiceEventBusException {
        stringWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<serviceEvent/>\n");
        String expectedValues = stringWriter.toString();
        XmlUtil.setContextProvider(new TestJAXBContextProvider());
        Assert.assertEquals("Expected and actual values should be the same!", expectedValues, xmlServiceEventMarshaler.marshal(serviceEvent));
    }

    @Test
    public void marshalJsonWithContextTest() throws ServiceEventBusException {
        stringWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<serviceEvent>\n" +
                "   <id>id</id>\n" +
                "   <contextId>contextId</contextId>\n" +
                "   <entityType>entityType</entityType>\n" +
                "   <status>SENT</status>\n" +
                "   <note>note</note>\n" +
                "</serviceEvent>\n");

        String expectedValues = stringWriter.toString();
        serviceEvent.setId("id");
        serviceEvent.setContextId("contextId");
        serviceEvent.setEntityType("entityType");
        serviceEvent.setStatus(ServiceEvent.EventStatus.SENT);
        serviceEvent.setNote("note");

        XmlUtil.setContextProvider(new TestJAXBContextProvider());
        Assert.assertEquals("Expected and actual values should be the same!", expectedValues, xmlServiceEventMarshaler.marshal(serviceEvent));
    }

    @Test(expected = ServiceEventBusException.class)
    public void unmarshalXmlWithoutJAXBContextProviderTest() throws KapuaException {
        xmlServiceEventMarshaler.unmarshal("message");
    }

    @Test(expected = NullPointerException.class)
    public void unmarshalXmlWithNullContextTest() throws Exception {
        xmlServiceEventMarshaler.unmarshal(null);
    }

    @Test
    public void unmarshalXmlWithContextTest() throws KapuaException {
        XmlUtil.setContextProvider(new TestJAXBContextProvider());
        ServiceEvent elements = xmlServiceEventMarshaler.unmarshal("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<serviceEvent>\n" +
                "   <id>id</id>\n" +
                "   <contextId>contextId</contextId>\n" +
                "   <entityType>entityType</entityType>\n" +
                "   <status>SENT</status>\n" +
                "   <note>note</note>\n" +
                "</serviceEvent>\n");

        Assert.assertEquals("Expected and actual values should be the same!", "id", elements.getId());
        Assert.assertEquals("Expected and actual values should be the same!", "contextId", elements.getContextId());
        Assert.assertEquals("Expected and actual values should be the same!", "entityType", elements.getEntityType());
        Assert.assertEquals("Expected and actual values should be the same!", ServiceEvent.EventStatus.SENT, elements.getStatus());
    }

    @Test
    public void getContentTypeTest() {
        Assert.assertEquals("Expected and actual values should be the same!", "application/xml", xmlServiceEventMarshaler.getContentType());
    }
}
