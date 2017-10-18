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
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaNotifyChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaNotifyPayload;
import org.eclipse.kapua.message.internal.device.management.KapuaNotifyChannelImpl;
import org.eclipse.kapua.message.internal.device.management.KapuaNotifyMessageImpl;
import org.eclipse.kapua.message.internal.device.management.KapuaNotifyPayloadImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class KapuaNotifyMessageTest extends Assert {

    private static final String NOTIFY_MSG_STR = "Client id 'clientId-1' - semantic topic 'part1/part2/part3'";

    @Test
    public void kapuaNotifyPayloadConstructor() {
        KapuaNotifyPayload kapuaNotifyPayload = new KapuaNotifyPayloadImpl();

        assertNotNull(kapuaNotifyPayload);
    }

    @Test
    public void kapuaNotifyMessageConstructor() throws Exception {
        KapuaNotifyMessageImpl kapuaNotifyMessage = new KapuaNotifyMessageImpl();

        assertNotNull(kapuaNotifyMessage);
    }

    @Test
    public void kapuaNotifyChannelGetterSetters() throws Exception {
        KapuaNotifyChannel kapuaNotifyChannel = new KapuaNotifyChannelImpl();

        kapuaNotifyChannel.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaNotifyChannel.getClientId());
    }

    @Test
    public void toStringValue() throws Exception {
        KapuaNotifyChannel kapuaNotifyChannel = new KapuaNotifyChannelImpl();
        List<String> semanticParts = new ArrayList<>();
        semanticParts.add("part1");
        semanticParts.add("part2");
        semanticParts.add("part3");

        kapuaNotifyChannel.setClientId("clientId-1");
        kapuaNotifyChannel.setSemanticParts(semanticParts);
        String displayStr = kapuaNotifyChannel.toString();
        assertEquals(NOTIFY_MSG_STR, displayStr);
    }

}
