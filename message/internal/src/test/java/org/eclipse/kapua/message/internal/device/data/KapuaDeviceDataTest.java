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
package org.eclipse.kapua.message.internal.device.data;

import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class KapuaDeviceDataTest extends Assert {

    @Test
    public void kapuaDataChannelGetterSetters() throws Exception {
        KapuaDataChannelImpl kapuaDataChannel = new KapuaDataChannelImpl();

        kapuaDataChannel.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaDataChannel.getClientId());
    }

    @Test
    public void kapuaDataChanneltoString() throws Exception {
        KapuaDataChannelImpl kapuaDataChannel = new KapuaDataChannelImpl();
        List<String> semanticParts = new ArrayList<>();
        semanticParts.add("part1");
        semanticParts.add("part2");
        semanticParts.add("part3");

        kapuaDataChannel.setClientId("clientId-1");
        kapuaDataChannel.setSemanticParts(semanticParts);
        assertEquals("semantic topic 'part1/part2/part3/'", kapuaDataChannel.toString());
    }

    @Test
    public void kapuaDataMesssageGetterSetters() {
        KapuaDataMessage kapuaDataMessage = new KapuaDataMessageImpl();

        kapuaDataMessage.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaDataMessage.getClientId());
    }

    @Test
    public void kapuaDataPayloadDefaultConstructor() {
        KapuaDataPayload kapuaDataPayload = new KapuaDataPayloadImpl();

        assertNotNull(kapuaDataPayload);
    }
}
