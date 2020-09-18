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
package org.eclipse.kapua.service.config;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ServiceXmlConfigPropertiesAdaptedTest extends Assert {

    @Test
    public void serviceXmlConfigPropertiesAdaptedTest() {
        ServiceXmlConfigPropertiesAdapted serviceXmlConfigPropertiesAdapted = new ServiceXmlConfigPropertiesAdapted();
        assertNull("Null expected.", serviceXmlConfigPropertiesAdapted.getProperties());
    }

    @Test
    public void setAndGetPropertiesTest() {
        String[] names = {"name", null};
        String[][] stringValues = {null, {"String Values 1", "String Values 2"}};
        ServiceXmlConfigPropertyAdapted.ConfigPropertyType[] configPropertyType = {null, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.stringType,
                ServiceXmlConfigPropertyAdapted.ConfigPropertyType.longType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.doubleType,
                ServiceXmlConfigPropertyAdapted.ConfigPropertyType.floatType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.integerType,
                ServiceXmlConfigPropertyAdapted.ConfigPropertyType.byteType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.charType,
                ServiceXmlConfigPropertyAdapted.ConfigPropertyType.booleanType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.shortType};
        ServiceXmlConfigPropertiesAdapted serviceXmlConfigPropertiesAdapted = new ServiceXmlConfigPropertiesAdapted();

        for (String name : names) {
            for (String[] value : stringValues) {
                for (ServiceXmlConfigPropertyAdapted.ConfigPropertyType type : configPropertyType) {
                    ServiceXmlConfigPropertyAdapted[] properties = {null, new ServiceXmlConfigPropertyAdapted(), new ServiceXmlConfigPropertyAdapted(name, type, value)};
                    serviceXmlConfigPropertiesAdapted.setProperties(properties);
                    assertEquals("Expected and actual values should be the same.", properties, serviceXmlConfigPropertiesAdapted.getProperties());

                    serviceXmlConfigPropertiesAdapted.setProperties(null);
                    assertNull("Null expected", serviceXmlConfigPropertiesAdapted.getProperties());
                }
            }
        }
    }
}