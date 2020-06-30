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
package org.eclipse.kapua.commons.setting;

import org.apache.commons.configuration.CompositeConfiguration;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class EnvFriendlyConfigurationTest extends Assert {

    @Before
    public void createInstanceOfClasses() {
        envFriendlyConfiguration = new EnvFriendlyConfiguration();
        compositeConfiguration = new CompositeConfiguration();
    }

    CompositeConfiguration compositeConfiguration;
    EnvFriendlyConfiguration envFriendlyConfiguration;

    @Test
    public void getKeysTest() {
        assertFalse("The next item exists!", envFriendlyConfiguration.getKeys().hasNext());
    }

    @Test
    public void getPropertyTest() {
        assertNull("Null expected!", envFriendlyConfiguration.getProperty("property"));
    }

    @Test
    public void getPropertyEmptyTest() {
        assertNull("Null expected!", envFriendlyConfiguration.getProperty(""));
    }

    @Test(expected = NullPointerException.class)
    public void getPropertyNullTest() {
        assertNull("Null expected!", envFriendlyConfiguration.getProperty(null));
    }

    @Test
    public void containsKeyTest() {
        assertFalse("The key is contained!", envFriendlyConfiguration.containsKey("key"));
    }
}
