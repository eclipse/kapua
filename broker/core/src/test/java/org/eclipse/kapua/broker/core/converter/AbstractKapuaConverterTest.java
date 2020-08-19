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
package org.eclipse.kapua.broker.core.converter;

import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(JUnitTests.class)
public class AbstractKapuaConverterTest extends Assert {

    AbstractKapuaConverter converter;

    @Before
    public void start() {
        converter = Mockito.mock(AbstractKapuaConverter.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void convertToJmsMessageNullTest() {
        try {
            converter.convertToJmsMessage(null, null);
            fail("Exception should be thrown");
        } catch (Exception e) {
            // pass
        }
    }

    @Test
    public void metricServiceTest() {
        Object metricService = AbstractKapuaConverter.METRICS_SERVICE;
        assertTrue(metricService instanceof MetricsService);
    }

}
