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
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class ServiceXmlConfigPropertyAdaptedTest {

    ServiceXmlConfigPropertyAdapted.ConfigPropertyType[] configPropertyType;
    String[][] stringValues;
    String[] names;

    @Before
    public void initialize() {
        configPropertyType = new ServiceXmlConfigPropertyAdapted.ConfigPropertyType[]{null, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.stringType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.longType,
                ServiceXmlConfigPropertyAdapted.ConfigPropertyType.doubleType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.floatType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.integerType,
                ServiceXmlConfigPropertyAdapted.ConfigPropertyType.byteType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.charType,
                ServiceXmlConfigPropertyAdapted.ConfigPropertyType.booleanType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.shortType};
        stringValues = new String[][]{{"String Values 1", "String Values 2"}, null};
        names = new String[]{"name", "1234", "!@#$%^&*(", null};
    }

    @Test
    public void serviceXmlConfigPropertyAdaptedTest() {
        ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted = new ServiceXmlConfigPropertyAdapted();
        Assert.assertNull("Null expected.", serviceXmlConfigPropertyAdapted.getName());
        Assert.assertFalse("False expected.", serviceXmlConfigPropertyAdapted.getArray());
        Assert.assertNull("Null expected.", serviceXmlConfigPropertyAdapted.getType());
        Assert.assertFalse("False expected.", serviceXmlConfigPropertyAdapted.isEncrypted());
        Assert.assertNull("Null expected.", serviceXmlConfigPropertyAdapted.getValues());
    }

    @Test
    public void serviceXmlConfigPropertyAdaptedWithParametersTest() {
        for (String name : names) {
            for (ServiceXmlConfigPropertyAdapted.ConfigPropertyType type : configPropertyType) {
                for (String[] value : stringValues) {
                    ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted = new ServiceXmlConfigPropertyAdapted(name, type, value);
                    Assert.assertNull("Null expected.", serviceXmlConfigPropertyAdapted.getName());
                    Assert.assertFalse("False expected.", serviceXmlConfigPropertyAdapted.getArray());
                    Assert.assertEquals("Expected and actual values should be the same.", type, serviceXmlConfigPropertyAdapted.getType());
                    Assert.assertFalse("False expected", serviceXmlConfigPropertyAdapted.isEncrypted());
                    Assert.assertEquals("Expected and actual values should be the same.", value, serviceXmlConfigPropertyAdapted.getValues());
                }
            }
        }
    }

    @Test
    public void setAndGetNameTest() {
        String[] newNames = {"New name", null};

        ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted1 = new ServiceXmlConfigPropertyAdapted();
        for (String name : names) {
            for (ServiceXmlConfigPropertyAdapted.ConfigPropertyType type : configPropertyType) {
                for (String[] value : stringValues) {
                    ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted2 = new ServiceXmlConfigPropertyAdapted(name, type, value);
                    for (String newName : newNames) {
                        serviceXmlConfigPropertyAdapted1.setName(newName);
                        serviceXmlConfigPropertyAdapted2.setName(newName);
                        Assert.assertEquals("Expected and actual values should be the same.", newName, serviceXmlConfigPropertyAdapted1.getName());
                        Assert.assertEquals("Expected and actual values should be the same.", newName, serviceXmlConfigPropertyAdapted2.getName());
                    }
                }
            }
        }
    }

    @Test
    public void setAndGetArrayTest() {
        Boolean[] arrays = {true, false};

        ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted1 = new ServiceXmlConfigPropertyAdapted();
        for (String name : names) {
            for (String[] value : stringValues) {
                for (ServiceXmlConfigPropertyAdapted.ConfigPropertyType type : configPropertyType) {
                    ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted2 = new ServiceXmlConfigPropertyAdapted(name, type, value);
                    for (Boolean array : arrays) {
                        serviceXmlConfigPropertyAdapted1.setArray(array);
                        serviceXmlConfigPropertyAdapted2.setArray(array);
                        Assert.assertEquals("Expected and actual values should be the same.", array, serviceXmlConfigPropertyAdapted1.getArray());
                        Assert.assertEquals("Expected and actual values should be the same.", array, serviceXmlConfigPropertyAdapted2.getArray());
                    }
                }
            }
        }
    }

    @Test
    public void setAndGetTypeTest() {
        ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted1 = new ServiceXmlConfigPropertyAdapted();
        for (String name : names) {
            for (String[] value : stringValues) {
                ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted2 = new ServiceXmlConfigPropertyAdapted(name, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.shortType, value);
                for (ServiceXmlConfigPropertyAdapted.ConfigPropertyType type : configPropertyType) {
                    serviceXmlConfigPropertyAdapted1.setType(type);
                    serviceXmlConfigPropertyAdapted2.setType(type);
                    Assert.assertEquals("Expected and actual values should be the same.", type, serviceXmlConfigPropertyAdapted1.getType());
                    Assert.assertEquals("Expected and actual values should be the same.", type, serviceXmlConfigPropertyAdapted2.getType());
                }
            }
        }
    }

    @Test
    public void isAndSetEncriptedTest() {
        Boolean[] isEncrypted = {true, false};

        ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted1 = new ServiceXmlConfigPropertyAdapted();
        for (String name : names) {
            for (String[] value : stringValues) {
                for (ServiceXmlConfigPropertyAdapted.ConfigPropertyType type : configPropertyType) {
                    ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted2 = new ServiceXmlConfigPropertyAdapted(name, type, value);
                    for (Boolean encrypted : isEncrypted) {
                        serviceXmlConfigPropertyAdapted1.setEncrypted(encrypted);
                        serviceXmlConfigPropertyAdapted2.setEncrypted(encrypted);
                        Assert.assertEquals("Expected and actual values should be the same.", encrypted, serviceXmlConfigPropertyAdapted1.isEncrypted());
                        Assert.assertEquals("Expected and actual values should be the same.", encrypted, serviceXmlConfigPropertyAdapted2.isEncrypted());
                    }
                }
            }
        }
    }

    @Test
    public void setAndGetValues() {
        String[][] newValues = {{"New String Values 1", "New String Values 2"}, null};

        ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted1 = new ServiceXmlConfigPropertyAdapted();
        for (String name : names) {
            for (String[] value : stringValues) {
                for (ServiceXmlConfigPropertyAdapted.ConfigPropertyType type : configPropertyType) {
                    ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted2 = new ServiceXmlConfigPropertyAdapted(name, type, value);
                    for (String[] newValue : newValues) {
                        serviceXmlConfigPropertyAdapted1.setValues(newValue);
                        serviceXmlConfigPropertyAdapted2.setValues(newValue);
                        Assert.assertEquals("Expected and actual values should be the same.", newValue, serviceXmlConfigPropertyAdapted1.getValues());
                        Assert.assertEquals("Expected and actual values should be the same.", newValue, serviceXmlConfigPropertyAdapted2.getValues());
                    }
                }
            }
        }
    }
}
