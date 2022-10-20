/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

import com.codahale.metrics.MetricRegistry;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class MetricsServiceImplTest {

    MetricRegistry metricRegistry;
    MetricsServiceImpl metricServiceImpl;

    @Before
    public void createInstanceOfClasses() {
        metricServiceImpl = new MetricsServiceImpl();
        metricRegistry = new MetricRegistry();
    }

    @Test
    public void getMetricRegistry() {
        Assert.assertNotNull(metricServiceImpl.getMetricRegistry());
    }

    @Test
    public void getCounterTest() {
        Assert.assertNotNull("Counter object should be returned!", metricServiceImpl.getCounter("module", "component", "name1", "name2", "name3"));
        Assert.assertTrue("The keys does not exist!", metricServiceImpl.getMetricRegistry().getMetrics().containsKey("module.component.name1.name2.name3"));
    }

    @Test
    public void getHistogramTest() {
        Assert.assertNotNull("Histogram object should be returned!", metricServiceImpl.getHistogram("module", "component", "name1", "name2", "name3"));
        Assert.assertTrue("The keys does not exist!", metricServiceImpl.getMetricRegistry().getMetrics().containsKey("module.component.name1.name2.name3"));

    }

    @Test
    public void getTimerTest() {
        Assert.assertNotNull("Timer object should be returned!", metricServiceImpl.getTimer("module", "component", "name1", "name2", "name3"));
        Assert.assertTrue("TThe keys does not exist!", metricServiceImpl.getMetricRegistry().getMetrics().containsKey("module.component.name1.name2.name3"));
    }
}
