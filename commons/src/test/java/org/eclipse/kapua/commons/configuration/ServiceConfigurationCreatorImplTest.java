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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Properties;

@Category(JUnitTests.class)
public class ServiceConfigurationCreatorImplTest extends Assert {
    ServiceConfigCreatorImpl configCreator;

    @Before
    public void createInstanceOfClass() {
        KapuaId id = KapuaId.ONE;
        configCreator = new ServiceConfigCreatorImpl(id);
    }

    @Test
    public void setPidTest() {
        configCreator.setPid("1");
        assertEquals(configCreator.getPid(), "1");
    }

    @Test
    public void getPidTest() {
        Assert.assertNull(configCreator.getPid());
        configCreator.setPid("123");
        assertEquals(configCreator.getPid(), "123");
    }

    @Test
    public void setPidToStringTest() {
        Assert.assertNull(configCreator.getPid());
        configCreator.setPid("asdf");
        assertEquals(configCreator.getPid(), "asdf");
    }

    @Test
    public void setPidToSymbolsTest() {
        Assert.assertNull(configCreator.getPid());
        configCreator.setPid("@!#$%&");
        assertEquals(configCreator.getPid(), "@!#$%&");
    }

    @Test
    public void setAndGetConfigurationsTest() {
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        configCreator.setConfigurations(properties);
        assertEquals(configCreator.getConfigurations(), properties);
    }
}