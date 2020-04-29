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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Properties;

@Category(JUnitTests.class)
public class ServiceConfigImplTest extends Assert {
    ServiceConfigImpl serviceConfigImpl;

    @Before
    public void createInstenceOfClass() {
        KapuaId id = KapuaId.ONE;
        serviceConfigImpl = new ServiceConfigImpl(id);
    }

    @Test
    public void constructorWithNoValuesTest() {
        ServiceConfigImpl config = new ServiceConfigImpl();
        assertNull(config.getPid());
    }

    @Test
    public void setPidRegularTest() {
        serviceConfigImpl.setPid("123");
        assertEquals(serviceConfigImpl.getPid(), "123");
    }

    @Test
    public void setPidNullValueTest() {
        serviceConfigImpl.setPid(null);
        assertNull(serviceConfigImpl.getPid());
    }

    @Test
    public void setConfigurationsNullValueTest() throws KapuaException {
        serviceConfigImpl.setConfigurations(null);
        assertEquals(serviceConfigImpl.getConfigurations(), new Properties());
    }

    @Test
    public void setConfigurationsRegularTest() throws KapuaException {
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        serviceConfigImpl.setConfigurations(properties);
        assertEquals(serviceConfigImpl.getConfigurations(), properties);
    }

    @Test
    public void getTypeTest() {
        assertEquals(serviceConfigImpl.getType(), ServiceConfig.TYPE);
    }
}
