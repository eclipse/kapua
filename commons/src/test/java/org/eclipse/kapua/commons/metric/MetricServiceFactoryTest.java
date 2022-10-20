/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.metric;

import java.lang.reflect.Constructor;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * {@link MetricsService} factory.
 */

@Category(JUnitTests.class)
public class MetricServiceFactoryTest {

    public static MetricsService instance2;

    @Test
    public void testConstructor() throws Exception {
        Constructor<MetricServiceFactory> metricFactory = MetricServiceFactory.class.getDeclaredConstructor();
        metricFactory.setAccessible(true);
        @SuppressWarnings("unused")
        MetricServiceFactory metricFactoryTest = metricFactory.newInstance();
    }

    @Test
    public void testMetricService() {
        Assert.assertNotNull(MetricServiceFactory.getInstance());
    }
}
