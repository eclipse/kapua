/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
public class KapuaDataMessageFactoryTest {

    private KapuaDataMessageFactory kapuaDataMessageFactory;

    @Before
    public void before() throws KapuaException {
        kapuaDataMessageFactory = new KapuaDataMessageFactoryImpl();
    }

    @Test
    public void newMessage() throws Exception {
        KapuaDataMessage message = kapuaDataMessageFactory.newKapuaDataMessage();

        Assert.assertNotNull(message);
    }

    @Test
    public void newChannel() throws Exception {
        KapuaDataChannel channel = kapuaDataMessageFactory.newKapuaDataChannel();

        Assert.assertNotNull(channel);
    }

    @Test
    public void newPayload() throws Exception {
        KapuaDataPayload payload = kapuaDataMessageFactory.newKapuaDataPayload();

        Assert.assertNotNull(payload);
    }
}
