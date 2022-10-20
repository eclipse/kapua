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
package org.eclipse.kapua.service.config;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class ServiceXmlConfigPropertiesAdaptedTest {

    @Test
    public void serviceXmlConfigPropertiesAdaptedTest() {
        ServiceXmlConfigPropertiesAdapted serviceXmlConfigPropertiesAdapted = new ServiceXmlConfigPropertiesAdapted();
        Assert.assertNull("Null expected.", serviceXmlConfigPropertiesAdapted.getProperties());
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
                    Assert.assertEquals("Expected and actual values should be the same.", properties, serviceXmlConfigPropertiesAdapted.getProperties());

                    serviceXmlConfigPropertiesAdapted.setProperties(null);
                    Assert.assertNull("Null expected", serviceXmlConfigPropertiesAdapted.getProperties());
                }
            }
        }
    }
}
