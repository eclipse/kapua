/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaIllegalStateException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@Category(JUnitTests.class)
public class XmlUtilTest {

    @Test
    public void setContextProviderTest() {
        XmlUtil.setContextProvider(new XmlUtilTestJAXBContextProvider());

        Assert.assertNotNull(XmlUtil.getContextProvider());
    }

    @Test(expected = KapuaIllegalStateException.class)
    public void setContextProviderTestNull() {
        XmlUtil.setContextProvider(null);

        XmlUtil.getContextProvider();
    }

    @Test
    public void marshalTest() throws JAXBException {
        XmlUtil.setContextProvider(new XmlUtilTestJAXBContextProvider());
        XmlUtilTestObject object = XmlUtilTestObject.create();
        String xmlObject = XmlUtil.marshal(object);

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
        XmlUtil.setContextProvider(new XmlUtilTestJAXBContextProvider());
        XmlUtilTestObject object = XmlUtilTestObject.create();

        try (StringWriter stringWriter = new StringWriter()) {
            XmlUtil.marshal(object, stringWriter);
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
        XmlUtil.setContextProvider(new XmlUtilTestJAXBContextProvider());
        String xmlObject = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<xmlUtilTestObject>\n" +
                "   <string>test</string>\n" +
                "   <integers>\n" +
                "      <integer>1</integer>\n" +
                "      <integer>2</integer>\n" +
                "   </integers>\n" +
                "</xmlUtilTestObject>\n";
        XmlUtilTestObject object = XmlUtil.unmarshal(xmlObject, XmlUtilTestObject.class);

        Assert.assertNotNull(object);
        Assert.assertEquals("test", object.getString());
        Assert.assertNotNull(object.getIntegers());
        Assert.assertEquals(2, object.getIntegers().size());
        Assert.assertEquals(new Integer(1), object.getIntegers().get(0));
        Assert.assertEquals(new Integer(2), object.getIntegers().get(1));
    }

    @Test
    public void unmarshalReaderTest() throws JAXBException, SAXException {
        XmlUtil.setContextProvider(new XmlUtilTestJAXBContextProvider());
        String xmlObject = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<xmlUtilTestObject>\n" +
                "   <string>test</string>\n" +
                "   <integers>\n" +
                "      <integer>1</integer>\n" +
                "      <integer>2</integer>\n" +
                "   </integers>\n" +
                "</xmlUtilTestObject>\n";

        try (StringReader stringReader = new StringReader(xmlObject)) {
            XmlUtilTestObject object = XmlUtil.unmarshal(stringReader, XmlUtilTestObject.class);

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
        XmlUtil.setContextProvider(new XmlUtilTestJAXBContextProvider());
        XmlUtilTestObject object = XmlUtilTestObject.create();
        String jsonObject = XmlUtil.marshalJson(object);

        Assert.assertNotNull(jsonObject);
        Assert.assertEquals("{\n" +
                "   \"string\" : \"test\",\n" +
                "   \"integers\" : [ 1, 2 ]\n" +
                "}", jsonObject);
    }

    @Test
    public void marshalWriterJsonTest() throws JAXBException, IOException {
        XmlUtil.setContextProvider(new XmlUtilTestJAXBContextProvider());
        XmlUtilTestObject object = XmlUtilTestObject.create();

        try (StringWriter stringWriter = new StringWriter()) {
            XmlUtil.marshalJson(object, stringWriter);
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
        XmlUtil.setContextProvider(new XmlUtilTestJAXBContextProvider());
        String jsonObject = "{\n" +
                "   \"string\" : \"test\",\n" +
                "   \"integers\" : [ 1, 2 ]\n" +
                "}";
        XmlUtilTestObject object = XmlUtil.unmarshalJson(jsonObject, XmlUtilTestObject.class);

        Assert.assertNotNull(object);
        Assert.assertEquals("test", object.getString());
        Assert.assertNotNull(object.getIntegers());
        Assert.assertEquals(2, object.getIntegers().size());
        Assert.assertEquals(new Integer(1), object.getIntegers().get(0));
        Assert.assertEquals(new Integer(2), object.getIntegers().get(1));
    }

    @Test
    public void unmarshalReaderJsonTest() throws JAXBException, SAXException {
        XmlUtil.setContextProvider(new XmlUtilTestJAXBContextProvider());
        String jsonObject = "{\n" +
                "   \"string\" : \"test\",\n" +
                "   \"integers\" : [ 1, 2 ]\n" +
                "}";

        try (StringReader stringReader = new StringReader(jsonObject)) {
            XmlUtilTestObject object = XmlUtil.unmarshalJson(stringReader, XmlUtilTestObject.class);

            Assert.assertNotNull(object);
            Assert.assertEquals("test", object.getString());
            Assert.assertNotNull(object.getIntegers());
            Assert.assertEquals(2, object.getIntegers().size());
            Assert.assertEquals(new Integer(1), object.getIntegers().get(0));
            Assert.assertEquals(new Integer(2), object.getIntegers().get(1));
        }
    }
}
