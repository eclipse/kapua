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
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class KapuaMetricValueTest extends Assert {

    private static final String newline = System.lineSeparator();

    private static final Long TIMESTAMP = ZonedDateTime.of(2017, 1, 18, 13, 10, 46, 0, ZoneId.systemDefault()).
            toEpochSecond();

    private static final String UUID = "11111111-2222-3333-4444-555555555555";

    private static final byte[] BYTES = {'b', 'y', 't', 'e', 's'};

    private static final String BASE64_BYTES = "Ynl0ZXM=";

    private static final String METRIC_VALUE_XML_STR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + newline +
            "<metricValue>" + newline +
            "   <timestamp>" + TIMESTAMP + "</timestamp>" + newline +
            "   <value>value</value>" + newline +
            "   <uuid>11111111-2222-3333-4444-555555555555</uuid>" + newline +
            "</metricValue>" + newline;

    @Before
    public void before() throws Exception {
        XmlUtil.setContextProvider(new MessageJAXBContextProvider());
    }

    @Test
    public void defaultConstructor() throws Exception {
        KapuaMetricValue kapuaMetricValue = new KapuaMetricValue();

        assertNull(kapuaMetricValue.getValue());
        assertNull(kapuaMetricValue.getTimestamp());
        assertNull(kapuaMetricValue.getUUID());
    }

    @Test
    public void initConstructor() throws Exception {
        KapuaMetricValue kapuaMetricValue = new KapuaMetricValue(TIMESTAMP, "value", UUID);

        assertEquals(TIMESTAMP, kapuaMetricValue.getTimestamp());
        assertEquals("value", kapuaMetricValue.getValue());
        assertEquals(UUID, kapuaMetricValue.getUUID());
    }

    @Test
    public void initNullValueConstructor() throws Exception {
        KapuaMetricValue kapuaMetricValue = new KapuaMetricValue(TIMESTAMP, null, UUID);

        assertEquals(TIMESTAMP, kapuaMetricValue.getTimestamp());
        assertNull(kapuaMetricValue.getValue());
        assertEquals(UUID, kapuaMetricValue.getUUID());
    }

    @Test
    public void initByteValueConstructor() throws Exception {
        KapuaMetricValue kapuaMetricValue = new KapuaMetricValue(TIMESTAMP, BYTES, UUID);

        assertEquals(TIMESTAMP, kapuaMetricValue.getTimestamp());
        assertEquals(BASE64_BYTES, kapuaMetricValue.getValue());
        assertEquals(UUID, kapuaMetricValue.getUUID());
    }

    @Test
    public void getNullStringValue() throws Exception {

        assertNull(KapuaMetricValue.getStringValue(null));
    }

    @Test
    public void getByteArrayStringValue() throws Exception {

        assertEquals(BASE64_BYTES, KapuaMetricValue.getStringValue(BYTES));
    }

    @Test
    public void getIntegerStringValue() throws Exception {

        assertEquals("42", KapuaMetricValue.getStringValue(new Integer(42)));
    }

    @Test
    public void getStringStringValue() throws Exception {

        assertEquals("Big brown fox", KapuaMetricValue.getStringValue("Big brown fox"));
    }

    @Test
    public void getDoubleStringValue() throws Exception {

        assertEquals("42.42", KapuaMetricValue.getStringValue(new Double(42.42d)));
    }

    @Test
    public void getBooleanStringValue() throws Exception {

        assertEquals("true", KapuaMetricValue.getStringValue(true));
    }

    @Test
    public void marshallMetricValue() throws Exception {
        KapuaMetricValue kapuaMetricValue = new KapuaMetricValue(TIMESTAMP, "value", UUID);

        StringWriter strWriter = new StringWriter();
        XmlUtil.marshal(kapuaMetricValue, strWriter);
        assertEquals(METRIC_VALUE_XML_STR, strWriter.toString());
    }
}
