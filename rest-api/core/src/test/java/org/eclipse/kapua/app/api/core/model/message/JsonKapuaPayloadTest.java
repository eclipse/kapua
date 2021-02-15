/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.core.model.message;

import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.xml.XmlAdaptedMetric;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Category(JUnitTests.class)
public class JsonKapuaPayloadTest extends Assert {

    KapuaPayload kapuaPayload;
    byte[] body;
    Map<String, Object> map;

    @Before
    public void initialize() {
        kapuaPayload = Mockito.mock(KapuaPayload.class);
        body = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        map = new HashMap<>();
    }

    @Test
    public void jsonKapuaPayloadWithoutParameterTest() {
        JsonKapuaPayload jsonKapuaPayload = new JsonKapuaPayload();

        assertTrue("True expected.", jsonKapuaPayload.getMetrics().isEmpty());
        assertNull("Null expected.", jsonKapuaPayload.getBody());
    }

    @Test
    public void jsonKapuaPayloadWithParameterEmptyMapTest() {
        Mockito.when(kapuaPayload.getBody()).thenReturn(body);
        Mockito.when(kapuaPayload.getMetrics()).thenReturn(map);

        JsonKapuaPayload jsonKapuaPayload = new JsonKapuaPayload(kapuaPayload);

        assertTrue("True expected.", jsonKapuaPayload.getMetrics().isEmpty());
        assertEquals("Expected and actual values should be the same.", body, jsonKapuaPayload.getBody());
    }

    @Test
    public void jsonKapuaPayloadWithParameterNullMapValuesTest() {
        map.put(null, null);

        Mockito.when(kapuaPayload.getBody()).thenReturn(body);
        Mockito.when(kapuaPayload.getMetrics()).thenReturn(map);

        JsonKapuaPayload jsonKapuaPayload = new JsonKapuaPayload(kapuaPayload);
        assertTrue("True expected.", jsonKapuaPayload.getMetrics().isEmpty());
        assertEquals("Expected and actual values should be the same.", body, jsonKapuaPayload.getBody());
    }

    @Test
    public void jsonKapuaPayloadWithParameterTest() {
        map.put("key1", "value2");
        map.put("key2", "value2");

        Mockito.when(kapuaPayload.getBody()).thenReturn(body);
        Mockito.when(kapuaPayload.getMetrics()).thenReturn(map);

        JsonKapuaPayload jsonKapuaPayload = new JsonKapuaPayload(kapuaPayload);
        assertFalse("False expected.", jsonKapuaPayload.getMetrics().isEmpty());
        assertEquals("Expected and actual values should be the same.", body, jsonKapuaPayload.getBody());
    }

    @Test(expected = NullPointerException.class)
    public void jsonKapuaPayloadWithNullParameterTest() {
        new JsonKapuaPayload(null);
    }

    @Test
    public void setAndGetMetricsTest() {
        JsonKapuaPayload jsonKapuaPayload = new JsonKapuaPayload(kapuaPayload);
        List<XmlAdaptedMetric> metrics = new LinkedList<>();
        XmlAdaptedMetric jsonMetric = Mockito.mock(XmlAdaptedMetric.class);

        metrics.add(jsonMetric);

        assertTrue("True expected.", jsonKapuaPayload.getMetrics().isEmpty());
        jsonKapuaPayload.setMetrics(metrics);
        assertFalse("False expected.", jsonKapuaPayload.getMetrics().isEmpty());
    }

    @Test
    public void setAndGetBody() {
        JsonKapuaPayload jsonKapuaPayload = new JsonKapuaPayload(kapuaPayload);
        byte[] newBody = {1, 2, 3, 4, 5};

        assertNull("Null expected.", jsonKapuaPayload.getBody());
        jsonKapuaPayload.setBody(newBody);
        assertEquals("Expected and actual values should be the same.", newBody, jsonKapuaPayload.getBody());
    }
}