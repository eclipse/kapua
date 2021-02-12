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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class DefaultBrokerIpResolverTest extends Assert {

    @Test
    public void defaultBrokerIpResolverTest() throws KapuaException {
        System.setProperty("broker.ip", "192.168.33.10");
        DefaultBrokerIpResolver defaultBrokerIpResolver = new DefaultBrokerIpResolver();

        assertEquals("Expected and actual values should be the same", "192.168.33.10", defaultBrokerIpResolver.getBrokerIpOrHostName());
    }

    @Test(expected = KapuaException.class)
    public void defaultBrokerIpResolverExceptionTest() throws KapuaException {
        System.setProperty("broker.ip", "");

        new DefaultBrokerIpResolver();
    }

    @Test(expected = NullPointerException.class)
    public void defaultBrokerIpResolverNullTest() throws KapuaException {
        System.setProperty("broker.ip", null);
        new DefaultBrokerIpResolver();
    }
}