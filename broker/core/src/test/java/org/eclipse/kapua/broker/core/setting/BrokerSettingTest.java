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
package org.eclipse.kapua.broker.core.setting;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class BrokerSettingTest {

    @Test
    public void resetInstanceTest() {
        try {
            String brokerInstance1 = BrokerSetting.getInstance().toString();
            BrokerSetting.resetInstance();
            String brokerInstance2 = BrokerSetting.getInstance().toString();

            Assert.assertNotEquals("The two instances should not be the same!", brokerInstance1, brokerInstance2);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }
}