/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataMessageFactory;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.eclipse.kapua.message.internal.device.data.KapuaDataMessageFactoryImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaDataMessageFactoryTest extends Assert {

    private KapuaDataMessageFactory kapuaDataMessageFactory;

    @Before
    public void before() throws KapuaException {
        kapuaDataMessageFactory = new KapuaDataMessageFactoryImpl();
    }

    @Test
    public void newMessage() throws Exception {
        KapuaDataMessage message = kapuaDataMessageFactory.newKapuaDataMessage();

        assertNotNull(message);
    }

    @Test
    public void newChannel() throws Exception {
        KapuaDataChannel channel = kapuaDataMessageFactory.newKapuaDataChannel();

        assertNotNull(channel);
    }

    @Test
    public void newPayload() throws Exception {
        KapuaDataPayload payload = kapuaDataMessageFactory.newKapuaDataPayload();

        assertNotNull(payload);
    }
}
