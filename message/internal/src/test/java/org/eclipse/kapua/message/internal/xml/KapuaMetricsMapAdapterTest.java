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

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.commons.core.Container;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.message.internal.MessageJAXBContextProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KapuaMetricsMapAdapterTest extends Assert {

    private Container container = new Container() {};

    private static final String NEWLINE = System.lineSeparator();

    private static final String METRICS_XML_STR = //
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEWLINE +
                    "<payload>" + NEWLINE +
                    "   <metrics>" + NEWLINE +
                    "      <metric>" + NEWLINE +
                    "         <type>string</type>" + NEWLINE +
                    "         <value>value1</value>" + NEWLINE +
                    "         <name>key1</name>" + NEWLINE +
                    "      </metric>" + NEWLINE +
                    "   </metrics>" + NEWLINE +
                    "</payload>" + NEWLINE;

    @Before
    public void before() throws Exception {
        container.startup();

        XmlUtil.setContextProvider(new MessageJAXBContextProvider());
    }

    @After
    public void after() {
        container.shutdown();
    }

    @Test
    public void marshalWithAdapter() throws Exception {
        KapuaPayload metricsMap = new KapuaPayloadImpl();
        Map<String, Object> metrics = new HashMap<>();
        metrics.put(String.valueOf("key1"), String.valueOf("value1"));
        metricsMap.setProperties(metrics);

        StringWriter strWriter = new StringWriter();
        XmlUtil.marshal(metricsMap, strWriter);
        assertEquals(METRICS_XML_STR, strWriter.toString());
    }

    @Test
    public void unmarshalWithAdapter() throws Exception {
        KapuaPayload metricsMap = new KapuaPayloadImpl();
        Map<String, Object> metrics = new HashMap<>();
        metrics.put(String.valueOf("key1"), String.valueOf("value1"));
        metricsMap.setProperties(metrics);

        KapuaPayload metricsMapResp = XmlUtil.unmarshal(METRICS_XML_STR, KapuaPayload.class);
        assertEquals(metricsMap.getProperties().get("key1"), metricsMapResp.getProperties().get("key1"));
    }

}
