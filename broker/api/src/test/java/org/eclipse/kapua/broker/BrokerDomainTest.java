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
package org.eclipse.kapua.broker;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class BrokerDomainTest {

    BrokerDomain brokerDomain;
    BrokerDomain secondBrokerDomain;
    Object[] objects;

    @Before
    public void initialize() {
        brokerDomain = new BrokerDomain();
        secondBrokerDomain = new BrokerDomain();
        objects = new Object[]{0, 10, 100000, "String", 'c', -10, -1000000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false};
    }

    @Test
    public void getNameTest() {
        Assert.assertEquals("Expected and actual values should be the same.", "broker", brokerDomain.getName());
    }

    @Test
    public void getActionsTest() {
        Assert.assertEquals("Expected and actual values should be the same.", "[connect]", brokerDomain.getActions().toString());
    }

    @Test
    public void getGroupableTest() {
        Assert.assertFalse("False expected.", brokerDomain.getGroupable());
    }

    @Test
    public void equalsTest() {
        Assert.assertTrue("True expected.", brokerDomain.equals(secondBrokerDomain));
        for (Object object : objects) {
            Assert.assertFalse("False expected.", brokerDomain.equals(object));
        }
    }

    @Test
    public void hashCodeTest() {
        Assert.assertNotNull("NotNull expected.", brokerDomain.hashCode());
    }
}