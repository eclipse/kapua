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
package org.eclipse.kapua.broker;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class BrokerDomainTest extends Assert {

    @Test
    public void gettersTest() {
        BrokerDomain brokerDomain = new BrokerDomain();
        assertEquals("broker", brokerDomain.getName());
        assertEquals("[connect]", brokerDomain.getActions().toString());
        assertFalse(brokerDomain.getGroupable());
    }

    @Test
    public void equalsTest() {
        BrokerDomain brokerDomain1 = new BrokerDomain();
        BrokerDomain brokerDomain2 = new BrokerDomain();
        assertTrue(brokerDomain1.equals(brokerDomain2));
    }

    @Test
    public void hashCodeTest() {
        BrokerDomain brokerDomain = new BrokerDomain();
        assertNotNull(brokerDomain.hashCode());
    }
}
