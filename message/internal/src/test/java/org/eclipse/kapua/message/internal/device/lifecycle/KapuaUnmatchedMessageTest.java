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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedPayload;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaUnmatchedMessageTest extends Assert {

    private static final String UNMATCHED_MSG_STR = "Client id 'clientId-1' - semantic topic 'part1/part2/part3'";

    @Test
    public void kapuaNotifyPayloadConstructor() {
        KapuaUnmatchedPayload kapuaUnmatchedPayload = new KapuaUnmatchedPayloadImpl();

        assertNotNull(kapuaUnmatchedPayload);
    }

    @Test
    public void kapuaUnmatchedMessageConstructor() throws Exception {
        KapuaUnmatchedMessageImpl kapuaUnmatchedMessage = new KapuaUnmatchedMessageImpl();

        assertNotNull(kapuaUnmatchedMessage);
    }

    @Test
    public void kapuaUnmatchedChannelGetterSetters() throws Exception {
        KapuaUnmatchedChannel kapuaUnmatchedChannel = new KapuaUnmatchedChannelImpl();

        kapuaUnmatchedChannel.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaUnmatchedChannel.getClientId());
    }

    @Test
    public void toStringValue() throws Exception {
        KapuaUnmatchedChannel kapuaUnmatchedChannel = new KapuaUnmatchedChannelImpl();
        List<String> semanticParts = new ArrayList<>();
        semanticParts.add("part1");
        semanticParts.add("part2");
        semanticParts.add("part3");

        kapuaUnmatchedChannel.setClientId("clientId-1");
        kapuaUnmatchedChannel.setSemanticParts(semanticParts);
        String displayStr = kapuaUnmatchedChannel.toString();
        assertEquals(UNMATCHED_MSG_STR, displayStr);
    }

}
