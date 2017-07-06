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

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link MetricsService} factory.
 * 
 * @since 1.0
 */
public class MetricServiceFactoryTest extends Assert {

    public static MetricsService instance2;

    @Test
    public void testConstructor() throws Exception {
        Constructor<MetricServiceFactory> metricFactory = MetricServiceFactory.class.getDeclaredConstructor();
        metricFactory.setAccessible(true);
        MetricServiceFactory metricFactoryTest = metricFactory.newInstance();
    }

    @Test
    public void testMetricService() {
        Assert.assertNotNull(MetricServiceFactory.getInstance());
    }
}
