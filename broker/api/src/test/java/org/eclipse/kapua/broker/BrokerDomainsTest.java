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
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;


@Category(JUnitTests.class)
public class BrokerDomainsTest {

    @Test
    public void brokerDomainsTest() throws Exception {
        Constructor<BrokerDomains> brokerDomains = BrokerDomains.class.getDeclaredConstructor();
        Assert.assertTrue("True expected.", Modifier.isPrivate(brokerDomains.getModifiers()));
        brokerDomains.setAccessible(true);
        brokerDomains.newInstance();
    }

    @Test
    public void brokerDomainTest() {
        BrokerDomain brokerDomain = BrokerDomains.BROKER_DOMAIN;
        Assert.assertNotNull("NotNull expected.", brokerDomain);
    }
}