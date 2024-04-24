/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.util.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBException;

import org.eclipse.kapua.KapuaIllegalStateException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.xml.sax.SAXException;

@Category(JUnitTests.class)
public class XmlUtilTest {

    @Test
    public void setContextProviderTest() {
        new XmlUtil(new XmlUtilTestJAXBContextProvider());
    }

    @Test(expected = KapuaIllegalStateException.class)
    public void setContextProviderTestNull() {
        final XmlUtil xmlUtil = new XmlUtil(null);

        xmlUtil.getContextProvider();
    }

    @Test
    public void marshalTest() throws JAXBException {
        final XmlUtil xmlUtil = new XmlUtil(new XmlUtilTestJAXBContextProvider());
        XmlUtilTestObject object = XmlUtilTestObject.create();
        String xmlObject = xmlUtil.marshal(object);

        Assert.assertNotNull(xmlObject);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<xmlUtilTestObject>\n" +
                "   <string>test</string>\n" +
                "   <integers>\n" +
                "      <integer>1</integer>\n" +
                "      <integer>2</integer>\n" +
                "   </integers>\n" +
                "</xmlUtilTestObject>\n", xmlObject);
    }

    @Test
    public void marshalWriterTest() throws JAXBException, IOException {
        final XmlUtil xmlUtil = new XmlUtil(new XmlUtilTestJAXBContextProvider());
        XmlUtilTestObject object = XmlUtilTestObject.create();

        try (StringWriter stringWriter = new StringWriter()) {
            xmlUtil.marshal(object, stringWriter);
            String xmlObject = stringWriter.toString();

            Assert.assertNotNull(xmlObject);
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<xmlUtilTestObject>\n" +
                    "   <string>test</string>\n" +
                    "   <integers>\n" +
                    "      <integer>1</integer>\n" +
                    "      <integer>2</integer>\n" +
                    "   </integers>\n" +
                    "</xmlUtilTestObject>\n", xmlObject);
        }
    }

    @Test
    public void unmarshalTest() throws JAXBException, SAXException {
        final XmlUtil xmlUtil = new XmlUtil(new XmlUtilTestJAXBContextProvider());
        String xmlObject = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<xmlUtilTestObject>\n" +
                "   <string>test</string>\n" +
                "   <integers>\n" +
                "      <integer>1</integer>\n" +
                "      <integer>2</integer>\n" +
                "   </integers>\n" +
                "</xmlUtilTestObject>\n";
        XmlUtilTestObject object = xmlUtil.unmarshal(xmlObject, XmlUtilTestObject.class);

        Assert.assertNotNull(object);
        Assert.assertEquals("test", object.getString());
        Assert.assertNotNull(object.getIntegers());
        Assert.assertEquals(2, object.getIntegers().size());
        Assert.assertEquals(new Integer(1), object.getIntegers().get(0));
        Assert.assertEquals(new Integer(2), object.getIntegers().get(1));
    }

    @Test
    public void unmarshalReaderTest() throws JAXBException, SAXException {
        final XmlUtil xmlUtil = new XmlUtil(new XmlUtilTestJAXBContextProvider());
        String xmlObject = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<xmlUtilTestObject>\n" +
                "   <string>test</string>\n" +
                "   <integers>\n" +
                "      <integer>1</integer>\n" +
                "      <integer>2</integer>\n" +
                "   </integers>\n" +
                "</xmlUtilTestObject>\n";

        try (StringReader stringReader = new StringReader(xmlObject)) {
            XmlUtilTestObject object = xmlUtil.unmarshal(stringReader, XmlUtilTestObject.class);

            Assert.assertNotNull(object);
            Assert.assertEquals("test", object.getString());
            Assert.assertNotNull(object.getIntegers());
            Assert.assertEquals(2, object.getIntegers().size());
            Assert.assertEquals(new Integer(1), object.getIntegers().get(0));
            Assert.assertEquals(new Integer(2), object.getIntegers().get(1));
        }
    }

    @Test
    public void marshalJsonTest() throws JAXBException {
        final XmlUtil xmlUtil = new XmlUtil(new XmlUtilTestJAXBContextProvider());
        XmlUtilTestObject object = XmlUtilTestObject.create();
        String jsonObject = xmlUtil.marshalJson(object);

        Assert.assertNotNull(jsonObject);
        Assert.assertEquals("{\n" +
                "   \"string\" : \"test\",\n" +
                "   \"integers\" : [ 1, 2 ]\n" +
                "}", jsonObject);
    }

    @Test
    public void marshalWriterJsonTest() throws JAXBException, IOException {
        final XmlUtil xmlUtil = new XmlUtil(new XmlUtilTestJAXBContextProvider());
        XmlUtilTestObject object = XmlUtilTestObject.create();

        try (StringWriter stringWriter = new StringWriter()) {
            xmlUtil.marshalJson(object, stringWriter);
            String jsonObject = stringWriter.toString();

            Assert.assertNotNull(jsonObject);
            Assert.assertEquals("{\n" +
                    "   \"string\" : \"test\",\n" +
                    "   \"integers\" : [ 1, 2 ]\n" +
                    "}", jsonObject);
        }
    }

    @Test
    public void unmarshalJsonTest() throws JAXBException, SAXException {
        final XmlUtil xmlUtil = new XmlUtil(new XmlUtilTestJAXBContextProvider());
        String jsonObject = "{\n" +
                "   \"string\" : \"test\",\n" +
                "   \"integers\" : [ 1, 2 ]\n" +
                "}";
        XmlUtilTestObject object = xmlUtil.unmarshalJson(jsonObject, XmlUtilTestObject.class);

        Assert.assertNotNull(object);
        Assert.assertEquals("test", object.getString());
        Assert.assertNotNull(object.getIntegers());
        Assert.assertEquals(2, object.getIntegers().size());
        Assert.assertEquals(new Integer(1), object.getIntegers().get(0));
        Assert.assertEquals(new Integer(2), object.getIntegers().get(1));
    }

    @Test
    public void unmarshalReaderJsonTest() throws JAXBException, SAXException {
        final XmlUtil xmlUtil = new XmlUtil(new XmlUtilTestJAXBContextProvider());
        String jsonObject = "{\n" +
                "   \"string\" : \"test\",\n" +
                "   \"integers\" : [ 1, 2 ]\n" +
                "}";

        try (StringReader stringReader = new StringReader(jsonObject)) {
            XmlUtilTestObject object = xmlUtil.unmarshalJson(stringReader, XmlUtilTestObject.class);

            Assert.assertNotNull(object);
            Assert.assertEquals("test", object.getString());
            Assert.assertNotNull(object.getIntegers());
            Assert.assertEquals(2, object.getIntegers().size());
            Assert.assertEquals(new Integer(1), object.getIntegers().get(0));
            Assert.assertEquals(new Integer(2), object.getIntegers().get(1));
        }
    }
}
