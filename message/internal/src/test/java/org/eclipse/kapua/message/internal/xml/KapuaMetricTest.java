/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal.xml;

import java.math.BigDecimal;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.message.internal.MessageJAXBContextProvider;
import org.eclipse.kapua.message.xml.XmlAdaptedMetric;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaMetricTest extends Assert {

    private static final String NEWLINE = System.lineSeparator();

    private static final byte[] BYTES = { 'b', 'y', 't', 'e', 's' };

    private static final String BASE64_BYTES = "Ynl0ZXM=";

    @SuppressWarnings("unused")
    private static final String METRIC_XML_STR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEWLINE +
            "<metric>" + NEWLINE +
            "   <name>name</name>" + NEWLINE +
            "   <type>string</type>" + NEWLINE +
            "   <value>value</value>" + NEWLINE +
            "</metric>" + NEWLINE;

    @Before
    public void before() throws Exception {
        XmlUtil.setContextProvider(new MessageJAXBContextProvider());
    }

    @Test
    public void defaultConstructor() throws Exception {
        XmlAdaptedMetric kapuaMetric = new XmlAdaptedMetric();

        assertNull(kapuaMetric.getName());
        assertNull(kapuaMetric.getValue());
    }

    @Test
    public void stringTypeConstructor() throws Exception {
        XmlAdaptedMetric kapuaMetric = new XmlAdaptedMetric();

        kapuaMetric.setName("name");
        kapuaMetric.setValueType(String.class);
        kapuaMetric.setValue("value");

        assertEquals("name", kapuaMetric.getName());
        assertEquals("value", kapuaMetric.getValue());
        assertTrue(kapuaMetric.getValue() instanceof String);
    }

    @Test
    public void doubleTypeConstructor() throws Exception {
        XmlAdaptedMetric kapuaMetric = new XmlAdaptedMetric();

        kapuaMetric.setName("name");
        kapuaMetric.setValueType(Double.class);
        kapuaMetric.setValue("42.42");

        assertEquals("name", kapuaMetric.getName());
        assertEquals(Double.valueOf("42.42"), kapuaMetric.getCastedValue());
        assertEquals(kapuaMetric.getValueType(), Double.class);
    }

    @Test
    public void intTypeConstructor() throws Exception {
        XmlAdaptedMetric kapuaMetric = new XmlAdaptedMetric();

        kapuaMetric.setName("name");
        kapuaMetric.setValueType(Integer.class);
        kapuaMetric.setValue("42");

        assertEquals("name", kapuaMetric.getName());
        assertEquals(Integer.valueOf(42), kapuaMetric.getCastedValue());
        assertEquals(kapuaMetric.getValueType(), Integer.class);
    }

    @Test
    public void floatTypeConstructor() throws Exception {
        XmlAdaptedMetric kapuaMetric = new XmlAdaptedMetric();

        kapuaMetric.setName("name");
        kapuaMetric.setValueType(Float.class);
        kapuaMetric.setValue("42.42");

        assertEquals("name", kapuaMetric.getName());
        assertEquals(Float.valueOf("42.42"), kapuaMetric.getCastedValue());
        assertEquals(kapuaMetric.getValueType(), Float.class);
    }

    @Test
    public void longTypeConstructor() throws Exception {
        XmlAdaptedMetric kapuaMetric = new XmlAdaptedMetric();

        kapuaMetric.setName("name");
        kapuaMetric.setValueType(Long.class);
        kapuaMetric.setValue("42");

        assertEquals("name", kapuaMetric.getName());
        assertEquals(Long.valueOf("42"), kapuaMetric.getCastedValue());
        assertEquals(kapuaMetric.getValueType(), Long.class);
    }

    @Test
    public void booleanTypeConstructor() throws Exception {
        XmlAdaptedMetric kapuaMetric = new XmlAdaptedMetric();

        kapuaMetric.setName("name");
        kapuaMetric.setValueType(Boolean.class);
        kapuaMetric.setValue("true");

        assertEquals("name", kapuaMetric.getName());
        assertEquals(Boolean.TRUE, kapuaMetric.getCastedValue());
        assertEquals(kapuaMetric.getValueType(), Boolean.class);
    }

    @Test
    public void byteArrayTypeConstructor() throws Exception {
        XmlAdaptedMetric kapuaMetric = new XmlAdaptedMetric();

        kapuaMetric.setName("name");
        kapuaMetric.setValueType(byte[].class);
        kapuaMetric.setValue(BASE64_BYTES);

        assertEquals("name", kapuaMetric.getName());
        assertArrayEquals(BYTES, (byte[]) kapuaMetric.getCastedValue());
        assertEquals(kapuaMetric.getValueType(), byte[].class);
    }

    @Test
    public void unknownTypeConstructor() throws Exception {
        XmlAdaptedMetric kapuaMetric = new XmlAdaptedMetric();

        kapuaMetric.setName("name");
        kapuaMetric.setValueType(BigDecimal.class);
        kapuaMetric.setValue("10");

        assertEquals("name", kapuaMetric.getName());
        assertEquals("10", kapuaMetric.getCastedValue());
        assertEquals(kapuaMetric.getValue().getClass(), String.class);
        assertEquals(kapuaMetric.getValueType(), BigDecimal.class);
    }
    //
    // @Test
    // public void initWithTypeConstructor() throws Exception {
    // KapuaMetric kapuaMetric = new KapuaMetric("name", String.class, "value");
    //
    // assertEquals("name", kapuaMetric.getName());
    // assertEquals("value", kapuaMetric.getValue());
    // assertTrue(kapuaMetric.getValue() instanceof String);
    // }
    //
    // @Test
    // public void getMetricTypeString() throws Exception {
    //
    // assertEquals("string", KapuaMetric.getMetricType(String.class));
    // }
    //
    // @Test
    // public void getMetricTypeDouble() throws Exception {
    //
    // assertEquals("double", KapuaMetric.getMetricType(Double.class));
    // }
    //
    // @Test
    // public void getMetricTypeInteger() throws Exception {
    //
    // assertEquals("int", KapuaMetric.getMetricType(Integer.class));
    // }
    //
    // @Test
    // public void getMetricTypeFloat() throws Exception {
    //
    // assertEquals("float", KapuaMetric.getMetricType(Float.class));
    // }
    //
    // @Test
    // public void getMetricTypeLong() throws Exception {
    //
    // assertEquals("long", KapuaMetric.getMetricType(Long.class));
    // }
    //
    // @Test
    // public void getMetricTypeBoolean() throws Exception {
    //
    // assertEquals("boolean", KapuaMetric.getMetricType(Boolean.class));
    // }
    //
    // @Test
    // public void getMetricTypeByteArray() throws Exception {
    //
    // assertEquals("base64Binary", KapuaMetric.getMetricType(BYTES.getClass()));
    // }
    //
    // @Test
    // public void getMetricTypeUnknown() throws Exception {
    //
    // // FIXME Unknown types should be hadeled
    // assertEquals("class java.math.BigDecimal", KapuaMetric.getMetricType(BigDecimal.class));
    // }
    //
    // @Test
    // public void getMetricTypeNull() throws Exception {
    //
    // assertNull(KapuaMetric.getMetricType(null));
    // }
    //
    // @Test
    // public void getStringValueNull() throws Exception {
    //
    // assertNull(KapuaMetric.getStringValue(null));
    // }
    //
    // @Test
    // public void getStringValueOfByteArray() throws Exception {
    //
    // assertEquals(BASE64_BYTES, KapuaMetric.getStringValue(BYTES));
    // }
    //
    // @Test
    // public void marshallMetric() throws Exception {
    // KapuaMetric kapuaMetric = new KapuaMetric("name", "string", "value");
    //
    // StringWriter strWriter = new StringWriter();
    // XmlUtil.marshal(kapuaMetric, strWriter);
    // assertEquals(METRIC_XML_STR, strWriter.toString());
    // }
}
