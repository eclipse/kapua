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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;

@Category(JUnitTests.class)
public class DefaultBrokerIpResolverTest extends Assert {

    private static final String BROKER_IP_PROP_KEY = "broker.ip";

    @Test
    public void defaultBrokerIpResolverTest() throws KapuaException {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_IP_PROP_KEY, "192.168.33.10");

        Tests.runWithProperties(properties, () -> {
            DefaultBrokerIpResolver defaultBrokerIpResolver = new DefaultBrokerIpResolver();
            assertEquals("Expected and actual values should be the same", "192.168.33.10", defaultBrokerIpResolver.getBrokerIpOrHostName());
        });
    }

    @Test(expected = KapuaException.class)
    public void defaultBrokerIpResolverExceptionTest() throws KapuaException {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_IP_PROP_KEY, "");

        Tests.runWithProperties(properties, DefaultBrokerIpResolver::new);
    }

    @Test(expected = NullPointerException.class)
    public void defaultBrokerIpResolverNullTest() throws KapuaException {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_IP_PROP_KEY, null);

        Tests.runWithProperties(properties, DefaultBrokerIpResolver::new);
    }
}
