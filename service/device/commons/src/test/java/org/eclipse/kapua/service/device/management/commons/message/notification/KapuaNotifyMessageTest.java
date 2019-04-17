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
package org.eclipse.kapua.service.device.management.commons.message.notification;

import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyPayload;
import org.junit.Assert;
import org.junit.Test;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaNotifyMessageTest extends Assert {

    private static final String NOTIFY_MSG_STR = "Client id 'clientId-1' - semantic topic 'part1/part2/part3'";

    @Test
    public void kapuaNotifyPayloadConstructor() {
        KapuaNotifyPayload kapuaNotifyPayload = new KapuaNotifyPayloadImpl();

        Assert.assertNotNull(kapuaNotifyPayload);
    }

    @Test
    public void kapuaNotifyMessageConstructor() throws Exception {
        KapuaNotifyMessageImpl kapuaNotifyMessage = new KapuaNotifyMessageImpl();

        Assert.assertNotNull(kapuaNotifyMessage);
    }
}
