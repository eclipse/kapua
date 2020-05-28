/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.broker.client.pool.JmsAssistantProducerPool;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class JmsAssistantProducerPoolTest {

    @Test
    public void getIOnstanceTest() {
        JmsAssistantProducerPool pool = JmsAssistantProducerPool.getIOnstance(JmsAssistantProducerPool.DESTINATIONS.NO_DESTINATION);
        //I'm fixing this test but it's wrong. There's no sense to literally test instances returned by a factory.
        //and moreover why???
        Assert.assertEquals("Expected and actual values should be the same.", "org.eclipse.kapua.broker.client.pool.JmsAssistantProducerWrapperFactory<org.eclipse.kapua.broker.client.pool.JmsAssistantProducerWrapper>", pool.getFactoryType());
    }

    @Test
    public void getIOnstanceNullTest() {
        JmsAssistantProducerPool pool = JmsAssistantProducerPool.getIOnstance(null);
        Assert.assertNull("Null expected.", pool);
    }

    @Test
    public void closePools() {
        try {
            JmsAssistantProducerPool.closePools();
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }
}