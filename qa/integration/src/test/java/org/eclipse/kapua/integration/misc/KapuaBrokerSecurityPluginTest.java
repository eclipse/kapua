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
package org.eclipse.kapua.integration.misc;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.BrokerId;
import org.eclipse.kapua.broker.core.KapuaBrokerSecurityPlugin;
import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(Categories.junitTests.class)
public class KapuaBrokerSecurityPluginTest extends Assert {

    KapuaBrokerSecurityPlugin kapuaBrokerApplicationPlugin;
    Broker broker;
    BrokerId brokerId;
    BrokerService brokerService;

    @Before
    public void initialize() {
        kapuaBrokerApplicationPlugin = new KapuaBrokerSecurityPlugin();
        broker = Mockito.mock(Broker.class);
        brokerId = new BrokerId();
        brokerService = new BrokerService();
    }

    @Test
    public void installPluginTest() throws Exception {
        Mockito.when(broker.getBrokerName()).thenReturn("name");
        Mockito.when(broker.getBrokerId()).thenReturn(brokerId);
        Mockito.when(broker.getBrokerService()).thenReturn(brokerService);

        assertTrue("Instance of Broker expected.", kapuaBrokerApplicationPlugin.installPlugin(broker) instanceof Broker);
        assertEquals("Expected and actual values should be the same.", "name", kapuaBrokerApplicationPlugin.installPlugin(broker).getBrokerName());
        assertEquals("Expected and actual values should be the same.", brokerId, kapuaBrokerApplicationPlugin.installPlugin(broker).getBrokerId());
        assertEquals("Expected and actual values should be the same.", brokerService, kapuaBrokerApplicationPlugin.installPlugin(broker).getBrokerService());
    }

    @Test
    public void installPluginNullTest() throws Exception {
        assertTrue("Instance of Broker expected.", kapuaBrokerApplicationPlugin.installPlugin(null) instanceof Broker);

        try {
            kapuaBrokerApplicationPlugin.installPlugin(null).getBrokerName();
            fail("NullPointer exception expected.");
        } catch (Exception e) {
            assertEquals("NullPointer exception expected.", new NullPointerException().toString(), e.toString());
        }

        try {
            kapuaBrokerApplicationPlugin.installPlugin(null).getBrokerId();
            fail("NullPointer exception expected.");
        } catch (Exception e) {
            assertEquals("NullPointer exception expected.", new NullPointerException().toString(), e.toString());
        }

        try {
            kapuaBrokerApplicationPlugin.installPlugin(null).getBrokerService();
            fail("NullPointer exception expected.");
        } catch (Exception e) {
            assertEquals("NullPointer exception expected.", new NullPointerException().toString(), e.toString());
        }
    }
}