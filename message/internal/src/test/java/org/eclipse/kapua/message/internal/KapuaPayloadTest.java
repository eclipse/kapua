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
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.message.KapuaPayload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Map;

import static org.eclipse.kapua.message.internal.KapuaMessageUtil.populatePayload;
import static org.eclipse.kapua.message.internal.KapuaMessageUtil.populatePayloadWithAllTypesOfMetrics;

public class KapuaPayloadTest extends Assert {

    private static final int PAYLOAD_DISPLAY_STR_LEN = 109;

    private static final String KAPUA_PAYLOAD_XML_STR = "missing";

    @Before
    public void before() throws Exception {
        XmlUtil.setContextProvider(new MessageJAXBContextProvider());
    }

    @Test
    public void payloadGetterSetters() throws Exception {
        KapuaPayload kapuaPayload = new KapuaPayloadImpl();
        populatePayload(kapuaPayload);

        Map<String, Object> properties = kapuaPayload.getProperties();
        byte[] body = kapuaPayload.getBody();
        assertEquals("value1", properties.get("key1"));
        assertEquals("value2", properties.get("key2"));
        assertArrayEquals(new byte[]{'b', 'o', 'd', 'y'}, body);
    }

    @Test
    public void displayString() throws Exception {
        KapuaPayload kapuaPayload = new KapuaPayloadImpl();
        populatePayloadWithAllTypesOfMetrics(kapuaPayload);

        String displayStr = kapuaPayload.toDisplayString();
        assertEquals("Length not equal, content probably too.", PAYLOAD_DISPLAY_STR_LEN, displayStr.length());
    }

    @Test
    @Ignore("KapuaPayload marshaling not working")
    public void marshallPayload() throws Exception {
        KapuaPayload kapuaPayload = new KapuaPayloadImpl();
        populatePayload(kapuaPayload);

        StringWriter strWriter = new StringWriter();
        XmlUtil.marshal(kapuaPayload, strWriter);
        assertEquals(KAPUA_PAYLOAD_XML_STR, strWriter.toString());
    }
}
