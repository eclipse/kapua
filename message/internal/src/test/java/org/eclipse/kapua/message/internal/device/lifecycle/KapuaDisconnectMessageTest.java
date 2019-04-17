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

import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectPayload;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaDisconnectMessageTest extends Assert {

    private static final String PAYLOAD_DISPLAY_STR = "" +
            "displayName=A display name" +
            "~~uptime=12";

    @Test
    public void kapuaDisconnectPayloadInitConstructor() {
        KapuaDisconnectPayload kapuaDisconnectPayload = populateKapuaDisconnectPayload();

        assertEquals("12", kapuaDisconnectPayload.getUptime());
        assertEquals("A display name", kapuaDisconnectPayload.getDisplayName());
    }

    @Test
    public void toDisplayString() throws Exception {
        KapuaDisconnectPayload kapuaDisconnectPayload = populateKapuaDisconnectPayload();

        String displayStr = kapuaDisconnectPayload.toDisplayString();
        assertEquals(PAYLOAD_DISPLAY_STR, displayStr);
    }

    @Test
    public void kapuaDisconnectMessageConstructor() throws Exception {
        KapuaDisconnectMessageImpl kapuaDisconnectMessage = new KapuaDisconnectMessageImpl();

        assertNotNull(kapuaDisconnectMessage);
    }

    @Test
    public void kapuaDisconnectMessageGetterSetters() throws Exception {
        KapuaDisconnectMessage kapuaDisconnectMessage = new KapuaDisconnectMessageImpl();

        kapuaDisconnectMessage.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaDisconnectMessage.getClientId());
    }

    @Test
    public void kapuaDisconnectChannelGetterSetters() throws Exception {
        KapuaDisconnectChannel kapuaDisconnectChannel = new KapuaDisconnectChannelImpl();

        kapuaDisconnectChannel.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaDisconnectChannel.getClientId());
    }

    /**
     * Prepare Kapua Disconnect payload data, data is not necessary semantically correct.
     *
     * @return all KapuaDisconnectPayload fields populated with data.
     */
    private static KapuaDisconnectPayload populateKapuaDisconnectPayload() {
        return new KapuaDisconnectPayloadImpl(
                "12",
                "A display name"
        );
    }
}
