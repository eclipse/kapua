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
package org.eclipse.kapua.broker.core.plugin.metric;

import com.codahale.metrics.Counter;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class ClientMetricTest extends Assert {

    ClientMetric clientMetric;

    @Before
    public void initialize() {
        clientMetric = ClientMetric.getInstance();
    }

    @Test
    public void clientMetricTest() throws Exception {
        Constructor<ClientMetric> clientMetric = ClientMetric.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(clientMetric.getModifiers()));
        clientMetric.setAccessible(true);
        clientMetric.newInstance();
    }

    @Test
    public void gettersTest() {
        assertEquals("Expected and actual values should be the same.", Long.parseLong("0"), clientMetric.getConnectedClient().getCount());
        assertTrue("Instance of Counter expected.", clientMetric.getConnectedClient() instanceof Counter);

        assertEquals("Expected and actual values should be the same.", Long.parseLong("0"), clientMetric.getConnectedKapuasys().getCount());
        assertTrue("Instance of Counter expected.", clientMetric.getConnectedKapuasys() instanceof Counter);

        assertEquals("Expected and actual values should be the same.", Long.parseLong("0"), clientMetric.getDisconnectedClient().getCount());
        assertTrue("Instance of Counter expected.", clientMetric.getDisconnectedClient() instanceof Counter);

        assertEquals("Expected and actual values should be the same.", Long.parseLong("0"), clientMetric.getDisconnectedKapuasys().getCount());
        assertTrue("Instance of Counter expected.", clientMetric.getDisconnectedKapuasys() instanceof Counter);
    }
}