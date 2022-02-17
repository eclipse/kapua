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
package org.eclipse.kapua.broker.core.converter;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class ConverterMetricsTest extends Assert {

    @Test
    public void converterMetricsTest() throws Exception {
        Constructor<ConverterMetrics> converterMetrics = ConverterMetrics.class.getDeclaredConstructor();
        converterMetrics.setAccessible(true);
        converterMetrics.newInstance();
        assertTrue("True expected.", Modifier.isPrivate(converterMetrics.getModifiers()));
    }

    @Test
    public void checkConstants() {
        assertEquals("Expected and actual values should be the same.", "converter", ConverterMetrics.METRIC_MODULE_NAME);
        assertEquals("Expected and actual values should be the same.", "kapua", ConverterMetrics.METRIC_COMPONENT_NAME);
        assertEquals("Expected and actual values should be the same.", "jms", ConverterMetrics.METRIC_JMS);
        assertEquals("Expected and actual values should be the same.", "message", ConverterMetrics.METRIC_MESSAGE);
        assertEquals("Expected and actual values should be the same.", "messages", ConverterMetrics.METRIC_MESSAGES);
        assertEquals("Expected and actual values should be the same.", "error", ConverterMetrics.METRIC_ERROR);
        assertEquals("Expected and actual values should be the same.", "count", ConverterMetrics.METRIC_COUNT);
        assertEquals("Expected and actual values should be the same.", "kapua_message", ConverterMetrics.METRIC_KAPUA_MESSAGE);
        assertEquals("Expected and actual values should be the same.", "data", ConverterMetrics.METRIC_DATA);
        assertEquals("Expected and actual values should be the same.", "app", ConverterMetrics.METRIC_APP);
        assertEquals("Expected and actual values should be the same.", "birth", ConverterMetrics.METRIC_BIRTH);
        assertEquals("Expected and actual values should be the same.", "dc", ConverterMetrics.METRIC_DC);
        assertEquals("Expected and actual values should be the same.", "missing", ConverterMetrics.METRIC_MISSING);
        assertEquals("Expected and actual values should be the same.", "notify", ConverterMetrics.METRIC_NOTIFY);
    }
}