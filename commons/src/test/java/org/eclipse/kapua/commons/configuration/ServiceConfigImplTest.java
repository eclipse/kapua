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
public class ServiceConfigImplTest {
    ServiceConfigImpl serviceConfigImpl;

    @Before
    public void createInstenceOfClass() {
        KapuaId id = KapuaId.ONE;
        serviceConfigImpl = new ServiceConfigImpl(id);
    }

    @Test
    public void constructorWithNoValuesTest() {
        ServiceConfigImpl config = new ServiceConfigImpl();
        Assert.assertNull(config.getPid());
    }

    @Test
    public void setPidRegularTest() {
        serviceConfigImpl.setPid("123");
        Assert.assertEquals(serviceConfigImpl.getPid(), "123");
    }

    @Test
    public void setPidNullValueTest() {
        serviceConfigImpl.setPid(null);
        Assert.assertNull(serviceConfigImpl.getPid());
    }

    @Test
    public void setConfigurationsNullValueTest() throws KapuaException {
        serviceConfigImpl.setConfigurations(null);
        Assert.assertEquals(serviceConfigImpl.getConfigurations(), new Properties());
    }

    @Test
    public void setConfigurationsRegularTest() throws KapuaException {
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        serviceConfigImpl.setConfigurations(properties);
        Assert.assertEquals(serviceConfigImpl.getConfigurations(), properties);
    }

    @Test
    public void getTypeTest() {
        Assert.assertEquals(serviceConfigImpl.getType(), ServiceConfig.TYPE);
    }
}
