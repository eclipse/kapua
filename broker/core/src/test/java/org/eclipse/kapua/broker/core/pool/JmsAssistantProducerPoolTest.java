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
package org.eclipse.kapua.broker.core.pool;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class JmsAssistantProducerPoolTest extends Assert {

    @Test
    public void jmsAssistantProducerPoolTest() {
        String[] destinations = {"test", "destination1234", "","destination test",
                "destinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationdestinationd",
                "asd#$$%%/(@", "123", "-23", null};
        for (String destination : destinations) {
            JmsAssistantProducerPool pool = new JmsAssistantProducerPool(new JmsAssistantProducerWrapperFactory(destination));
            assertEquals("Expected and actual values should be the same.", 10, pool.getMaxTotal());
            assertEquals("Expected and actual values should be the same.", 10, pool.getMaxIdle());
            assertEquals("Expected and actual values should be the same.", 5, pool.getMinIdle());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void jmsAssistantProducerPoolNullTest() {
       new JmsAssistantProducerPool(null);
    }

    @Test
    public void getIOnstanceTest() {
        JmsAssistantProducerPool pool = JmsAssistantProducerPool.getIOnstance(JmsAssistantProducerPool.DESTINATIONS.NO_DESTINATION);
        assertEquals("Expected and actual values should be the same.", "org.eclipse.kapua.broker.core.pool.JmsAssistantProducerWrapperFactory<org.eclipse.kapua.broker.core.pool.JmsAssistantProducerWrapper>", pool.getFactoryType());
    }

    @Test
    public void getIOnstanceNullTest() {
        JmsAssistantProducerPool pool = JmsAssistantProducerPool.getIOnstance(null);
        assertNull("Null expected.", pool);
    }

    @Test
    public void closePools() {
        try {
            JmsAssistantProducerPool.closePools();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}