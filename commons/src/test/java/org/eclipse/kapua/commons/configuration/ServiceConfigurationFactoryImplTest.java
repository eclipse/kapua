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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.config.ServiceComponentConfiguration;
import org.eclipse.kapua.service.config.ServiceConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class ServiceConfigurationFactoryImplTest extends Assert {

    @Test
    public void newComponentConfigurationInstanceWithStringTest() {
        ServiceConfigurationFactoryImpl configurationFactory = new ServiceConfigurationFactoryImpl();
        ServiceComponentConfiguration componentConfiguration = configurationFactory.newComponentConfigurationInstance("testInstance");
        assertEquals(componentConfiguration.getId(), "testInstance");
    }

    @Test
    public void newComponentConfigurationInstanceWithNumberTest() {
        ServiceConfigurationFactoryImpl configurationFactory = new ServiceConfigurationFactoryImpl();
        ServiceComponentConfiguration componentConfiguration = configurationFactory.newComponentConfigurationInstance("1");
        assertEquals(componentConfiguration.getId(), "1");
    }

    @Test
    public void newComponentConfigurationInstanceWithSymbolTest() {
        ServiceConfigurationFactoryImpl configurationFactory = new ServiceConfigurationFactoryImpl();
        ServiceComponentConfiguration componentConfiguration = configurationFactory.newComponentConfigurationInstance("@");
        assertEquals(componentConfiguration.getId(), "@");
    }

    @Test
    public void newConfigurationInstanceTest() {
        ServiceConfigurationFactoryImpl configurationFactory = new ServiceConfigurationFactoryImpl();
        ServiceConfiguration configuration = configurationFactory.newConfigurationInstance();
        assertNotNull(configuration);
    }
}