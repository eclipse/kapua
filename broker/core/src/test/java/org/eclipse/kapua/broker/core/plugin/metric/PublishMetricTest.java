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
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Timer;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class PublishMetricTest extends Assert {

    PublishMetric publishMetric;

    @Before
    public void initialize() {
        publishMetric = PublishMetric.getInstance();
    }

    @Test
    public void publishMetricTest() throws Exception {
        Constructor<PublishMetric> publishMetric = PublishMetric.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(publishMetric.getModifiers()));
        publishMetric.setAccessible(true);
        publishMetric.newInstance();
    }

    @Test
    public void gettersTest() {
        assertTrue("Instance of Counter expected.", publishMetric.getAllowedMessages() instanceof Counter);
        assertTrue("Instance of Counter expected.", publishMetric.getNotAllowedMessages() instanceof Counter);
        assertTrue("Instance of Timer expected.", publishMetric.getTime() instanceof Timer);
        assertTrue("Instance of Histogram expected.", publishMetric.getMessageSizeAllowed() instanceof Histogram);
        assertTrue("Instance of Histogram expected.", publishMetric.getMessageSizeNotAllowed() instanceof Histogram);
    }
}