/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin.metric;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ClientMetricTest extends Assert {

    ClientMetric clientMetric;

    @Before
    public void start() {
        clientMetric = ClientMetric.getInstance();
    }

    @Test
    public void getConnectedClientTest() {
        assertEquals(Long.parseLong("0"), clientMetric.getConnectedClient().getCount());
    }

    @Test
    public void getConnectedKapuaSysTest() {
        assertEquals(Long.parseLong("0"), clientMetric.getConnectedKapuasys().getCount());
    }

    @Test
    public void getDisconnectionClientTest() {
        assertEquals(Long.parseLong("0"), clientMetric.getDisconnectionClient().getCount());
    }

    @Test
    public void getDisconnectionKapuasysTest() {
        assertEquals(Long.parseLong("0"), clientMetric.getDisconnectionKapuasys().getCount());
    }
}
