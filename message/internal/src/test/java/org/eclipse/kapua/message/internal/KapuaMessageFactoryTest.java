/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaMessageFactoryTest extends Assert {

    private KapuaMessageFactory kapuaMessageFactory;

    @Before
    public void before() throws KapuaException {
        kapuaMessageFactory = new KapuaMessageFactoryImpl();
    }

    @Test
    public void newMessage() throws Exception {
        KapuaMessage<?, ?> message = kapuaMessageFactory.newMessage();

        assertNotNull(message);
    }

    @Test
    public void newChannel() throws Exception {
        KapuaChannel channel = kapuaMessageFactory.newChannel();

        assertNotNull(channel);
    }

    @Test
    public void newPayload() throws Exception {
        KapuaPayload payload = kapuaMessageFactory.newPayload();

        assertNotNull(payload);
    }

    @Test
    public void newPosition() throws Exception {
        KapuaPosition position = kapuaMessageFactory.newPosition();

        assertNotNull(position);
    }

}
