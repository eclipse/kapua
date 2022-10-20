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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.config.ServiceComponentConfiguration;
import org.eclipse.kapua.service.config.ServiceConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;



@Category(JUnitTests.class)
public class ServiceConfigurationFactoryImplTest {

    @Test
    public void newComponentConfigurationInstanceWithStringTest() {
        ServiceConfigurationFactoryImpl configurationFactory = new ServiceConfigurationFactoryImpl();
        ServiceComponentConfiguration componentConfiguration = configurationFactory.newComponentConfigurationInstance("testInstance");
        Assert.assertEquals(componentConfiguration.getId(), "testInstance");
    }

    @Test
    public void newComponentConfigurationInstanceWithNumberTest() {
        ServiceConfigurationFactoryImpl configurationFactory = new ServiceConfigurationFactoryImpl();
        ServiceComponentConfiguration componentConfiguration = configurationFactory.newComponentConfigurationInstance("1");
        Assert.assertEquals(componentConfiguration.getId(), "1");
    }

    @Test
    public void newComponentConfigurationInstanceWithSymbolTest() {
        ServiceConfigurationFactoryImpl configurationFactory = new ServiceConfigurationFactoryImpl();
        ServiceComponentConfiguration componentConfiguration = configurationFactory.newComponentConfigurationInstance("@");
        Assert.assertEquals(componentConfiguration.getId(), "@");
    }

    @Test
    public void newConfigurationInstanceTest() {
        ServiceConfigurationFactoryImpl configurationFactory = new ServiceConfigurationFactoryImpl();
        ServiceConfiguration configuration = configurationFactory.newConfigurationInstance();
        Assert.assertNotNull(configuration);
    }
}
