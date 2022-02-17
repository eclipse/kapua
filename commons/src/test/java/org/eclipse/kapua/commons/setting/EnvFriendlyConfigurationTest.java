/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.setting;

import org.apache.commons.configuration.CompositeConfiguration;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class EnvFriendlyConfigurationTest extends Assert {

    CompositeConfiguration compositeConfiguration;
    EnvFriendlyConfiguration envFriendlyConfiguration;

    @Before
    public void createInstanceOfClass() {
        envFriendlyConfiguration = new EnvFriendlyConfiguration();
        compositeConfiguration = new CompositeConfiguration();
    }

    @Test
    public void getKeysTest() {
        assertFalse("The next item exists!", envFriendlyConfiguration.getKeys().hasNext());
        envFriendlyConfiguration.setProperty("Key1", "value1");
        assertTrue("The next item does not exist!", envFriendlyConfiguration.getKeys().hasNext());
    }

    @Test
    public void getPropertyTest() {
        assertNull("Null expected!", envFriendlyConfiguration.getProperty("property"));
        envFriendlyConfiguration.setProperty("key", "value");
        envFriendlyConfiguration.setProperty("KEY_PROPERTY", 10);
        assertEquals("Expected and actual values should be the same!", "value", envFriendlyConfiguration.getProperty("key"));
        assertEquals("Expected and actual values should be the same!", 10, envFriendlyConfiguration.getProperty("key.property"));
    }

    @Test
    public void getPropertyEmptyTest() {
        assertNull("Null expected!", envFriendlyConfiguration.getProperty(""));
        envFriendlyConfiguration.setProperty("", "value");
        assertEquals("Expected and actual values should be the same!", "value", envFriendlyConfiguration.getProperty(""));
    }

    @Test(expected = NullPointerException.class)
    public void getPropertyNullTest() {
        envFriendlyConfiguration.getProperty(null);
    }

    @Test
    public void containsKeyTest() {
        assertFalse("The key is contained!", envFriendlyConfiguration.containsKey("Key"));
        envFriendlyConfiguration.setProperty("Key", "value");
        assertTrue("There is no key!", envFriendlyConfiguration.containsKey("Key"));
        envFriendlyConfiguration.clearProperty("Key");
        assertFalse("The key is contained!", envFriendlyConfiguration.containsKey("Key"));
        envFriendlyConfiguration.setProperty("KEY_PROPERTY", 10);
        assertTrue("There is no key!", envFriendlyConfiguration.containsKey("key.property"));
    }
}
