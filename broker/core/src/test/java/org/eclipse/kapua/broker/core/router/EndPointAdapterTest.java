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
package org.eclipse.kapua.broker.core.router;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.persistence.jaxb.JAXBMarshaller;
import org.eclipse.persistence.jaxb.JAXBUnmarshaller;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import org.w3c.dom.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

@Category(JUnitTests.class)
public class EndPointAdapterTest extends Assert {

    EndPointAdapter endPointAdapter;
    Element element;
    NodeList nodeList;
    DocumentBuilderFactory documentBuilderFactory;
    DocumentBuilder documentBuilder;

    @Before
    public void initialize() throws ParserConfigurationException {
        endPointAdapter = new EndPointAdapter();
        element = Mockito.mock(Element.class);
        nodeList = Mockito.mock(NodeList.class);
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

    @Test(expected = NullPointerException.class)
    public void marshalNullTest() throws Exception {
        endPointAdapter.marshal(null);
    }

    @Test(expected = NullPointerException.class)
    public void unmarshalNullTest() throws Exception {
        endPointAdapter.unmarshal(null);
    }

    @Test
    public void unmarshalReturnEmptyListTest() throws Exception {
        Text textNode1 = Mockito.mock(Text.class);
        Text textNode2 = Mockito.mock(Text.class);
        Text textNode3 = Mockito.mock(Text.class);

        Mockito.when(element.getChildNodes()).thenReturn(nodeList);
        Mockito.when(element.getChildNodes().getLength()).thenReturn(3);
        Mockito.when(element.getChildNodes().item(0)).thenReturn(textNode1);
        Mockito.when(element.getChildNodes().item(1)).thenReturn(textNode2);
        Mockito.when(element.getChildNodes().item(2)).thenReturn(textNode3);

        assertThat("Instance of List expected.", endPointAdapter.unmarshal(element), IsInstanceOf.instanceOf(List.class));
        assertTrue("True expected", endPointAdapter.unmarshal(element).isEmpty());
    }

    @Test
    public void unmarshalTest() throws Exception {
        Document documentNode1 = documentBuilder.newDocument();
        Document documentNode2 = documentBuilder.newDocument();
        Document documentNode3 = documentBuilder.newDocument();

        Mockito.when(element.getChildNodes()).thenReturn(nodeList);
        Mockito.when(element.getChildNodes().getLength()).thenReturn(3);
        Mockito.when(element.getChildNodes().item(0)).thenReturn(documentNode1);
        Mockito.when(element.getChildNodes().item(1)).thenReturn(documentNode2);
        Mockito.when(element.getChildNodes().item(2)).thenReturn(documentNode3);

        assertThat("Instance of List expected.", endPointAdapter.unmarshal(element), IsInstanceOf.instanceOf(List.class));
        assertFalse("False expected", endPointAdapter.unmarshal(element).isEmpty());
    }

    @Test
    public void jaxbContextHandlerTest() {
        Marshaller marshaller = JaxbContextHandler.getMarshaller();
        Unmarshaller unmarshaller = JaxbContextHandler.getUnmarshaller();
        assertNotNull("Null not expected.", marshaller);
        assertNotNull("Null not expected.", unmarshaller);
        assertEquals("Expected and actual values should be the same.", JAXBMarshaller.class, marshaller.getClass());
        assertEquals("Expected and actual values should be the same.", JAXBUnmarshaller.class, unmarshaller.getClass());
    }
}