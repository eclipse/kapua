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
package org.eclipse.kapua.broker.core.setting;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class BrokerSettingTest extends Assert {

    @Test
    public void brokerSettingTest() throws Exception {
        Constructor<BrokerSetting> brokerSetting = BrokerSetting.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(brokerSetting.getModifiers()));
        brokerSetting.setAccessible(true);
        brokerSetting.newInstance();
    }

    @Test
    public void getInstanceTest() {
        Object brokerSetting = BrokerSetting.getInstance();
        assertNotNull("NotNull expected.", brokerSetting);
        assertTrue("True expected.", brokerSetting instanceof BrokerSetting);

        Object brokerSettingInstanceNotNull = BrokerSetting.getInstance();
        assertNotNull("NotNull expected.", brokerSettingInstanceNotNull);
        assertTrue("True expected.", brokerSetting instanceof BrokerSetting);
    }

    @Test
    public void resetInstanceTest() {
        try {
            String brokerInstance1 = BrokerSetting.getInstance().toString();
            BrokerSetting.resetInstance();
            String brokerInstance2 = BrokerSetting.getInstance().toString();

            assertNotEquals("The two instances should not be the same!", brokerInstance1, brokerInstance2);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }
}