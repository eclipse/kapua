/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.configuration.metatype.TocdImpl;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;

@Category(Categories.junitTests.class)
public class ServiceComponentConfigurationImplTest extends Assert {

    ServiceComponentConfigurationImpl serviceComponentConfiguration;

    @Before
    public void createInstanceOfClass() {
        serviceComponentConfiguration = new ServiceComponentConfigurationImpl();
    }

    @Test
    public void createInstanceWithSecondConstructorTest() {
        ServiceComponentConfigurationImpl componentConfiguration = new ServiceComponentConfigurationImpl("12");
        assertEquals(componentConfiguration.getId(), "12");
    }

    @Test
    public void getIdIfNullTest() {
        assertNull(serviceComponentConfiguration.getId());
    }

    @Test
    public void getIdIfIsSetTest() {
        serviceComponentConfiguration.setId("1");
        assertEquals(serviceComponentConfiguration.getId(), "1");
    }

    @Test
    public void setIdToNullValueTest() {
        serviceComponentConfiguration.setId(null);
        assertNull(serviceComponentConfiguration.getId());
    }

    @Test
    public void setIdToLargeValueTest() {
        serviceComponentConfiguration.setId("12312312312312331232313123123123");
        assertEquals(serviceComponentConfiguration.getId(), "12312312312312331232313123123123");
    }

    @Test
    public void setIdToSymbolsTest() {
        serviceComponentConfiguration.setId("@!#");
        assertEquals(serviceComponentConfiguration.getId(), "@!#");
    }

    @Test
    public void setIdToEmptyStringTest() {
        serviceComponentConfiguration.setId("");
        assertEquals(serviceComponentConfiguration.getId(), "");
    }

    @Test
    public void getNameIfNullTest() {
        assertNull(serviceComponentConfiguration.getName());
    }

    @Test
    public void setNameIfNotNullTest() {
        serviceComponentConfiguration.setName("name");
        assertEquals(serviceComponentConfiguration.getName(), "name");
    }

    @Test
    public void setNameToNullTest() {
        serviceComponentConfiguration.setName(null);
        assertNull(serviceComponentConfiguration.getName());
    }

    @Test
    public void setNameToNameThatContainsSpacesTest() {
        serviceComponentConfiguration.setName("regular Name");
        assertEquals(serviceComponentConfiguration.getName(), "regular Name");
    }

    @Test
    public void setNameToNameThatContainsSymbolsTest() {
        serviceComponentConfiguration.setName("regular Name !$%&/&@");
        assertEquals(serviceComponentConfiguration.getName(), "regular Name !$%&/&@");
    }

    @Test
    public void setDefinitionToNullTest() {
        serviceComponentConfiguration.setDefinition(null);
        assertNull(serviceComponentConfiguration.getDefinition());
    }

    @Test
    public void setDefinitionToRegularValueTest() {
        KapuaTocd tocd = new TocdImpl();
        serviceComponentConfiguration.setDefinition(tocd);
        assertEquals(serviceComponentConfiguration.getDefinition(), tocd);
    }

    @Test
    public void setPropertiesToNullTest() {
        serviceComponentConfiguration.setProperties(null);
        assertNull(serviceComponentConfiguration.getProperties());
    }

    @Test
    public void setPropertiesToRegularValueTest() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("property1", 10);
        properties.put("property2", "string");
        properties.put("property3", 'c');
        properties.put("property4", (double)10);
        serviceComponentConfiguration.setProperties(properties);
        assertEquals(properties, serviceComponentConfiguration.getProperties());
    }
}
