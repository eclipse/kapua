/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.consumer.commons.message.system;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Map;

@Category(JUnitTests.class)
public class DefaultSystemMessageCreatorTest {

    DefaultSystemMessageCreator messageCreator;

    @Before
    public void initialize() {
        messageCreator = new DefaultSystemMessageCreator();
    }

    @Test(expected = NullPointerException.class)
    public void createMessageNullTest() {
        messageCreator.createMessage(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void createMessageNullContextTest() {
        messageCreator.createMessage(SystemMessageCreator.SystemMessageType.CONNECT, null);
    }

    @Test
    public void convertFromTest() {
        Map<String, String> map = messageCreator.convertFrom("DeviceId,device1,,Username,user1");
        Assert.assertEquals("Expected and actual values should be the same.", "device1", messageCreator.getDeviceId(map));
        Assert.assertEquals("Expected and actual values should be the same.", "user1", messageCreator.getUsername(map));
    }

    @Test
    public void convertFromWrongArgumentsTest() {
        String[] values = {"a", "test,test,test,test", "::::", "test,test,,test,test,,test,test", ",,,,", "test, test, test, test, test"};
        for (String value : values) {
            try {
                messageCreator.convertFrom(value);
                Assert.fail("Exception expected.");
            } catch (IllegalArgumentException e) {
                Assert.assertEquals("IllegalArgumentException expected.", IllegalArgumentException.class, e.getClass());
            }
        }
    }
}