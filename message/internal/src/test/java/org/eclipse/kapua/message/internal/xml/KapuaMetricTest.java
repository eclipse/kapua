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
 *
 *******************************************************************************/
package org.eclipse.kapua.message.internal.xml;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.message.internal.MessageJAXBContextProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.math.BigDecimal;

public class KapuaMetricTest extends Assert {

    private static final String newline = System.lineSeparator();

    private static final byte[] BYTES = {'b', 'y', 't', 'e', 's'};

    private static final String BASE64_BYTES = "Ynl0ZXM=";

    private static final String METRIC_XML_STR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + newline +
            "<metric>" + newline +
            "   <name>name</name>" + newline +
            "   <type>string</type>" + newline +
            "   <value>value</value>" + newline +
            "</metric>" + newline;

    @Before
    public void before() throws Exception {
        XmlUtil.setContextProvider(new MessageJAXBContextProvider());
    }

    @Test
    public void defaultConstructor() throws Exception {
        KapuaMetric kapuaMetric = new KapuaMetric();

        assertNull(kapuaMetric.getName());
        assertNull(kapuaMetric.getValue());
    }

    @Test
    public void stringTypeConstructor() throws Exception {
        KapuaMetric kapuaMetric = new KapuaMetric("name", "string", "value");

        assertEquals("name", kapuaMetric.getName());
        assertEquals("value", kapuaMetric.getValue());
        assertTrue(kapuaMetric.getValue() instanceof String);
    }

    @Test
    public void doubleTypeConstructor() throws Exception {
        KapuaMetric kapuaMetric = new KapuaMetric("name", "double", "42.42");

        assertEquals("name", kapuaMetric.getName());
        assertEquals(new Double("42.42"), kapuaMetric.getValue());
        assertTrue(kapuaMetric.getValue() instanceof Double);
    }

    @Test
    public void intTypeConstructor() throws Exception {
        KapuaMetric kapuaMetric = new KapuaMetric("name", "int", "42");

        assertEquals("name", kapuaMetric.getName());
        assertEquals(new Integer(42), kapuaMetric.getValue());
        assertTrue(kapuaMetric.getValue() instanceof Integer);
    }

    @Test
    public void floatTypeConstructor() throws Exception {
        KapuaMetric kapuaMetric = new KapuaMetric("name", "float", "42.42");

        assertEquals("name", kapuaMetric.getName());
        assertEquals(new Float(42.42), kapuaMetric.getValue());
        assertTrue(kapuaMetric.getValue() instanceof Float);
    }

    @Test
    public void longTypeConstructor() throws Exception {
        KapuaMetric kapuaMetric = new KapuaMetric("name", "long", "42");

        assertEquals("name", kapuaMetric.getName());
        assertEquals(new Long(42), kapuaMetric.getValue());
        assertTrue(kapuaMetric.getValue() instanceof Long);
    }

    @Test
    public void booleanTypeConstructor() throws Exception {
        KapuaMetric kapuaMetric = new KapuaMetric("name", "boolean", "true");

        assertEquals("name", kapuaMetric.getName());
        assertEquals(Boolean.TRUE, kapuaMetric.getValue());
        assertTrue(kapuaMetric.getValue() instanceof Boolean);
    }

    @Test
    public void byteArrayTypeConstructor() throws Exception {
        KapuaMetric kapuaMetric = new KapuaMetric("name", "base64Binary", BASE64_BYTES);

        assertEquals("name", kapuaMetric.getName());
        assertArrayEquals(BYTES, (byte[]) kapuaMetric.getValue());
        assertTrue(kapuaMetric.getValue() instanceof byte[]);
    }

    @Test
    public void unknownTypeConstructor() throws Exception {
        KapuaMetric kapuaMetric = new KapuaMetric("name", "bigdecimal", "42.42");

        assertEquals("name", kapuaMetric.getName());
        assertEquals("42.42", kapuaMetric.getValue());
        assertTrue(kapuaMetric.getValue() instanceof String);
    }

    @Test
    public void initWithTypeConstructor() throws Exception {
        KapuaMetric kapuaMetric = new KapuaMetric("name", String.class, "value");

        assertEquals("name", kapuaMetric.getName());
        assertEquals("value", kapuaMetric.getValue());
        assertTrue(kapuaMetric.getValue() instanceof String);
    }

    @Test
    public void getMetricTypeString() throws Exception {

        assertEquals("string", KapuaMetric.getMetricType(String.class));
    }

    @Test
    public void getMetricTypeDouble() throws Exception {

        assertEquals("double", KapuaMetric.getMetricType(Double.class));
    }

    @Test
    public void getMetricTypeInteger() throws Exception {

        assertEquals("int", KapuaMetric.getMetricType(Integer.class));
    }

    @Test
    public void getMetricTypeFloat() throws Exception {

        assertEquals("float", KapuaMetric.getMetricType(Float.class));
    }

    @Test
    public void getMetricTypeLong() throws Exception {

        assertEquals("long", KapuaMetric.getMetricType(Long.class));
    }

    @Test
    public void getMetricTypeBoolean() throws Exception {

        assertEquals("boolean", KapuaMetric.getMetricType(Boolean.class));
    }

    @Test
    public void getMetricTypeByteArray() throws Exception {

        assertEquals("base64Binary", KapuaMetric.getMetricType(BYTES.getClass()));
    }

    @Test
    public void getMetricTypeUnknown() throws Exception {

        // FIXME Unknown types should be hadeled
        assertEquals("class java.math.BigDecimal", KapuaMetric.getMetricType(BigDecimal.class));
    }

    @Test
    public void getMetricTypeNull() throws Exception {

        assertNull(KapuaMetric.getMetricType(null));
    }

    @Test
    public void getStringValueNull() throws Exception {

        assertNull(KapuaMetric.getStringValue(null));
    }

    @Test
    public void getStringValueOfByteArray() throws Exception {

        assertEquals(BASE64_BYTES, KapuaMetric.getStringValue(BYTES));
    }

    @Test
    public void marshallMetric() throws Exception {
        KapuaMetric kapuaMetric = new KapuaMetric("name", "string", "value");

        StringWriter strWriter = new StringWriter();
        XmlUtil.marshal(kapuaMetric, strWriter);
        assertEquals(METRIC_XML_STR, strWriter.toString());
    }
}
