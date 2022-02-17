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
import com.codahale.metrics.Timer;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class SubscribeMetricTest extends Assert {

    SubscribeMetric subscribeMetric;

    @Before
    public void initialize() {
        subscribeMetric = SubscribeMetric.getInstance();
    }

    @Test
    public void subscribeMetricTest() throws Exception {
        Constructor<SubscribeMetric> subscribeMetric = SubscribeMetric.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(subscribeMetric.getModifiers()));
        subscribeMetric.setAccessible(true);
        subscribeMetric.newInstance();
    }

    @Test
    public void gettersTest() {
        assertEquals("Expected and actual values should be the same.", Long.parseLong("0"), subscribeMetric.getAllowedMessages().getCount());
        assertTrue("Instance of Counter expected.", subscribeMetric.getAllowedMessages() instanceof Counter);

        assertEquals("Expected and actual values should be the same.", Long.parseLong("0"), subscribeMetric.getNotAllowedMessages().getCount());
        assertTrue("Instance of Counter expected.", subscribeMetric.getNotAllowedMessages() instanceof Counter);

        assertEquals("Expected and actual values should be the same.", Long.parseLong("0"), subscribeMetric.getTime().getCount());
        assertTrue("Instance of Timer expected.", subscribeMetric.getTime() instanceof Timer);
    }
}