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

import org.eclipse.kapua.message.device.lifecycle.KapuaMissingChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingPayload;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaMissingMessageTest extends Assert {

    @Test
    public void kapuaMissingPayloadConstructor() {
        KapuaMissingPayload kapuaMissingPayload = new KapuaMissingPayloadImpl();

        assertNotNull(kapuaMissingPayload);
    }

    @Test
    public void kapuaMissingMessageConstructor() throws Exception {
        KapuaMissingMessage kapuaMissingMessage = new KapuaMissingMessageImpl();

        assertNotNull(kapuaMissingMessage);
    }

    @Test
    public void kapuaMissingChannelGetterSetters() throws Exception {
        KapuaMissingChannel kapuaMissingChannel = new KapuaMissingChannelImpl();

        kapuaMissingChannel.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaMissingChannel.getClientId());
    }

}
