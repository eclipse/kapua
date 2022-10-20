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
package org.eclipse.kapua.integration.misc;

import org.apache.activemq.broker.Broker;
import org.eclipse.kapua.broker.core.plugin.KapuaApplicationBrokerFilter;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;


@Category(JUnitTests.class)
public class KapuaApplicationBrokerFilterTest {

    @Test
    public void kapuaApplicationBrokerFilterTest() {
        Broker broker = Mockito.mock(Broker.class);

        try {
            new KapuaApplicationBrokerFilter(broker);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void kapuaApplicationBrokerFilterNullTest() {
        try {
            new KapuaApplicationBrokerFilter(null);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void stopTest() throws Exception {
        KapuaApplicationBrokerFilter kapuaApplicationBrokerFilter = new KapuaApplicationBrokerFilter(Mockito.mock(Broker.class));
        try {
            kapuaApplicationBrokerFilter.stop();
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test(expected = NullPointerException.class)
    public void stopNullTest() throws Exception {
        KapuaApplicationBrokerFilter kapuaApplicationBrokerFilter = new KapuaApplicationBrokerFilter(null);
        kapuaApplicationBrokerFilter.stop();
    }
}