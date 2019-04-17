/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public class MetricServiceFactoryTest extends Assert {

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
