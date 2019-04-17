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
package org.eclipse.kapua.message.internal.device.data;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataMessageFactory;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

@Category(JUnitTests.class)
public class KapuaDeviceDataTest extends Assert {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final KapuaDataMessageFactory KAPUA_DATA_MESSAGE_FACTORY = LOCATOR.getFactory(KapuaDataMessageFactory.class);

    @Test
    public void kapuaDataChanneltoString() throws Exception {
        KapuaDataChannel kapuaDataChannel = KAPUA_DATA_MESSAGE_FACTORY.newKapuaDataChannel();
        List<String> semanticParts = new ArrayList<>();
        semanticParts.add("part1");
        semanticParts.add("part2");
        semanticParts.add("part3");

        kapuaDataChannel.setSemanticParts(semanticParts);
        assertEquals("part1/part2/part3", kapuaDataChannel.toString());
    }

    @Test
    public void kapuaDataMesssageGetterSetters() {
        KapuaDataMessage kapuaDataMessage = KAPUA_DATA_MESSAGE_FACTORY.newKapuaDataMessage();

        kapuaDataMessage.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaDataMessage.getClientId());
    }

    @Test
    public void kapuaDataPayloadDefaultConstructor() {
        KapuaDataPayload kapuaDataPayload = KAPUA_DATA_MESSAGE_FACTORY.newKapuaDataPayload();

        assertNotNull(kapuaDataPayload);
    }
}
