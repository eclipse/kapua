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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ServiceEventModuleConfigurationTest extends Assert {

    EntityManagerFactory entityManagerFactory;

    ServiceEventClientConfiguration[] serviceEventClientConfiguration = new ServiceEventClientConfiguration[]
            { new ServiceEventClientConfiguration("address", "subscriberName", null),
                    new ServiceEventClientConfiguration("address2", "subscriberName2", null) };

    @Test
    public void serviceEventModuleConfigurationRegularTest() {
        ServiceEventModuleConfiguration moduleConfiguration = new ServiceEventModuleConfiguration("internalAddress", entityManagerFactory, serviceEventClientConfiguration);
        assertEquals("Expected and actual values are not equals!", "internalAddress", moduleConfiguration.getInternalAddress());
        assertNull("Null expected!", moduleConfiguration.getEntityManagerFactory());
        assertArrayEquals("Arrays are not equals!", serviceEventClientConfiguration, moduleConfiguration.getServiceEventClientConfigurations());
    }

    @Test
    public void serviceEventModuleConfigurationNullAddressTest() {
        ServiceEventModuleConfiguration moduleConfiguration = new ServiceEventModuleConfiguration(null, entityManagerFactory, serviceEventClientConfiguration);
        assertNull("Null expected!", moduleConfiguration.getInternalAddress());
        assertNull("Null expected!", moduleConfiguration.getEntityManagerFactory());
        assertArrayEquals(serviceEventClientConfiguration, moduleConfiguration.getServiceEventClientConfigurations());
    }

    @Test
    public void serviceEventModuleConfigurationNullEntityTest() {
        ServiceEventModuleConfiguration moduleConfiguration = new ServiceEventModuleConfiguration("internalAddress", null, serviceEventClientConfiguration);
        assertEquals("Expected and actual values are not equals!", "internalAddress", moduleConfiguration.getInternalAddress());
        assertNull("Null expected!", moduleConfiguration.getEntityManagerFactory());
        assertArrayEquals("Arrays are not equals!", serviceEventClientConfiguration, moduleConfiguration.getServiceEventClientConfigurations());
    }

    @Test
    public void constructorClientConfigNullTest() {
        ServiceEventModuleConfiguration moduleConfiguration = new ServiceEventModuleConfiguration("internalAddress", entityManagerFactory, null);
        assertEquals("Expected and actual values are not equals!", "internalAddress", moduleConfiguration.getInternalAddress());
        assertNull("Null expected!", moduleConfiguration.getEntityManagerFactory());
        assertNull("Null expected!", moduleConfiguration.getServiceEventClientConfigurations());
    }

    @Test
    public void serviceEventModuleConfigurationNullEntityAndAddressTest() {
        ServiceEventModuleConfiguration moduleConfiguration = new ServiceEventModuleConfiguration(null, null, serviceEventClientConfiguration);
        assertNull("Null expected!", moduleConfiguration.getInternalAddress());
        assertNull("Null expected!", moduleConfiguration.getEntityManagerFactory());
        assertArrayEquals("Arrays are not equals!", serviceEventClientConfiguration, moduleConfiguration.getServiceEventClientConfigurations());
    }

    @Test
    public void constructorEntityAndClientConfigNullTest() {
        ServiceEventModuleConfiguration moduleConfiguration = new ServiceEventModuleConfiguration("internalAddress", null, null);
        assertEquals("Expected and actual values are not equals!", "internalAddress", moduleConfiguration.getInternalAddress());
        assertNull("Null expected!", moduleConfiguration.getEntityManagerFactory());
        assertNull("Null expected!", moduleConfiguration.getServiceEventClientConfigurations());
    }

    @Test
    public void serviceEventModuleConfigurationAllNullTest() {
        ServiceEventModuleConfiguration moduleConfiguration = new ServiceEventModuleConfiguration(null, null, null);
        assertNull("Null expected!", moduleConfiguration.getInternalAddress());
        assertNull("Null expected!", moduleConfiguration.getEntityManagerFactory());
        assertNull("Null expected!", moduleConfiguration.getServiceEventClientConfigurations());
    }

    @Test
    public void constructorAddressCharCheckTest() {
        String[] permittedValues = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            ServiceEventModuleConfiguration moduleConfiguration = new ServiceEventModuleConfiguration(value, entityManagerFactory, serviceEventClientConfiguration);
            assertEquals("Expected and actual values are not equals!", value, moduleConfiguration.getInternalAddress());
            assertNull("Null expected!", moduleConfiguration.getEntityManagerFactory());
            assertArrayEquals("Arrays are not equals!", serviceEventClientConfiguration, moduleConfiguration.getServiceEventClientConfigurations());
        }
    }
}
