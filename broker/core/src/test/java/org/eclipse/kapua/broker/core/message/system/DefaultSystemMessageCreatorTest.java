/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.message.system;

import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Map;

@Category(JUnitTests.class)
public class DefaultSystemMessageCreatorTest extends Assert {

    DefaultSystemMessageCreator messageCreator;
    KapuaSecurityContext kapuaSecurityContext;

    @Before
    public void initialize() {
        messageCreator = new DefaultSystemMessageCreator();
        kapuaSecurityContext = new KapuaSecurityContext(new Long(1), "client1");
    }

    @Test(expected = NullPointerException.class)
    public void createMessageNullTest() {
        messageCreator.createMessage(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void createMessageNullMessageTypeTest() {
        messageCreator.createMessage(null, kapuaSecurityContext);
    }

    @Test(expected = NullPointerException.class)
    public void createMessageNullContextTest() {
        messageCreator.createMessage(SystemMessageCreator.SystemMessageType.CONNECT, null);
    }

    @Test
    public void createMessageConnectTypeTest() {
        String result = messageCreator.createMessage(SystemMessageCreator.SystemMessageType.CONNECT, kapuaSecurityContext);
        assertEquals("Expected and actual values should be the same.", "Event,CONNECT,,DeviceId,client1,,Username,null", result);
    }

    @Test
    public void createMessageDisconnectTypeTest() {
        String result = messageCreator.createMessage(SystemMessageCreator.SystemMessageType.DISCONNECT, kapuaSecurityContext);
        assertEquals("Expected and actual values should be the same.", "Event,DISCONNECT,,DeviceId,client1,,Username,null", result);
    }

    @Test
    public void convertFromTest() {
        Map<String, String> map = messageCreator.convertFrom("DeviceId,device1,,Username,user1");
        assertEquals("Expected and actual values should be the same.", "device1", messageCreator.getDeviceId(map));
        assertEquals("Expected and actual values should be the same.", "user1", messageCreator.getUsername(map));
    }

    @Test
    public void convertFromWrongArgumentsTest() {
        String[] values = {"a", "test,test,test,test", "::::", "test,test,,test,test,,test,test", ",,,,", "test, test, test, test, test"};
        for (String value : values) {
            try {
                messageCreator.convertFrom(value);
                fail("Exception expected.");
            } catch (IllegalArgumentException e) {
                assertEquals("IllegalArgumentException expected.", IllegalArgumentException.class, e.getClass());
            }
        }
    }
}