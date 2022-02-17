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
package org.eclipse.kapua.broker.core.listener;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class DataStoreMetricsTest extends Assert {

    @Test
    public void dataStoreMetricsTest() throws Exception {
        Constructor<DataStoreMetrics> dataStoreMetrics = DataStoreMetrics.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(dataStoreMetrics.getModifiers()));
        dataStoreMetrics.setAccessible(true);
        dataStoreMetrics.newInstance();
    }

    @Test
    public void checkConstantsTest() {
        assertEquals("Expected and actual values should be the same.", "datastore", DataStoreMetrics.METRIC_MODULE_NAME);
        assertEquals("Expected and actual values should be the same.", "datastore", DataStoreMetrics.METRIC_COMPONENT_NAME);
        assertEquals("Expected and actual values should be the same.", "store", DataStoreMetrics.METRIC_STORE);
        assertEquals("Expected and actual values should be the same.", "queue", DataStoreMetrics.METRIC_QUEUE);
        assertEquals("Expected and actual values should be the same.", "communication", DataStoreMetrics.METRIC_COMMUNICATION);
        assertEquals("Expected and actual values should be the same.", "configuration", DataStoreMetrics.METRIC_CONFIGURATION);
        assertEquals("Expected and actual values should be the same.", "generic", DataStoreMetrics.METRIC_GENERIC);
        assertEquals("Expected and actual values should be the same.", "error", DataStoreMetrics.METRIC_ERROR);
        assertEquals("Expected and actual values should be the same.", "count", DataStoreMetrics.METRIC_COUNT);
    }
}