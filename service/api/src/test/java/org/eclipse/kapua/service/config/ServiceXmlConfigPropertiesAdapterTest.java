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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;


@Category(JUnitTests.class)
public class ServiceXmlConfigPropertiesAdapterTest {

    ServiceXmlConfigPropertiesAdapter serviceXmlConfigPropertiesAdapter;
    Map[] expectedProperties;

    @Before
    public void initialize() {
        serviceXmlConfigPropertiesAdapter = new ServiceXmlConfigPropertiesAdapter();

        Map<String, Object> expectedProperty1 = new HashMap<>();
        Map<String, Object> expectedProperty2 = new HashMap<>();
        Map<String, Object> expectedProperty3 = new HashMap<>();
        Map<String, Object> expectedProperty4 = new HashMap<>();
        Map<String, Object> expectedProperty5 = new HashMap<>();
        Map<String, Object> expectedProperty6 = new HashMap<>();
        Map<String, Object> expectedProperty7 = new HashMap<>();
        Map<String, Object> expectedProperty8 = new HashMap<>();
        Map<String, Object> expectedProperty9 = new HashMap<>();
        Map<String, Object> expectedProperty10 = new HashMap<>();
        expectedProperty2.put(null, "47");
        expectedProperty3.put(null, (long) 47);
        expectedProperty4.put(null, 47d);
        expectedProperty5.put(null, 47f);
        expectedProperty6.put(null, 47);
        expectedProperty7.put(null, (byte) 47);
        expectedProperty8.put(null, '4');
        expectedProperty9.put(null, false);
        expectedProperty10.put(null, (short) 47);
        expectedProperties = new Map[]{expectedProperty1, expectedProperty2, expectedProperty3, expectedProperty4, expectedProperty5, expectedProperty6, expectedProperty7, expectedProperty8, expectedProperty9, expectedProperty10};
    }

    @Test
    public void marshalTest() {
        Map<String, Object> props = new HashMap<>();
        String stringValue = "String Value";
        long longValue = 10L;
        double doubleValue = 10.10d;
        float floatValue = 10f;
        int integerValue = 20;
        byte byteValue = 10;
        char characterValue = 'c';
        boolean booleanValue = true;
        short shortValue = 32767;
        String[] stringArray = {null, "String Value 1", "String Value 2", "String Value 3"};
        Long[] longArray = {null, 10L, 20L, 20200L};
        Double[] doubleArray = {null, 10.123d, 1000.1234556d, 10.100000000000000000000d};
        Float[] floatArray = {null, 10.100000000000000000000f, 1010101010.10f, 1010101010.1f};
        Integer[] integerArray = {null, -2000, -20, 0, 10, 20,};
        Byte[] byteArray = {null, -128, -10, 0, 10, 127};
        Character[] characterArray = {null, 'c', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r'};
        Boolean[] booleanArray = {null, true, false};
        Short[] shortArray = {null, -32768, 1, 0, 10, 32767};

        Object[] values = {stringValue, longValue, doubleValue, floatValue, integerValue, byteValue, characterValue,
                booleanValue, shortValue, stringArray, longArray, doubleArray, floatArray, integerArray, byteArray, characterArray,
                booleanArray, shortArray};

        for (Object value : values) {
            props.put("key", value);
        Assert.assertThat("Instance of ServiceXmlConfigPropertiesAdapted expected.", serviceXmlConfigPropertiesAdapter.marshal(props), IsInstanceOf.instanceOf(ServiceXmlConfigPropertiesAdapted.class));
        }
    }

    @Test
    public void marshalNullPropsTest() {
        Assert.assertThat("Instance of ServiceXmlConfigPropertiesAdapted expected.", serviceXmlConfigPropertiesAdapter.marshal(null), IsInstanceOf.instanceOf(ServiceXmlConfigPropertiesAdapted.class));
    }

    @Test
    public void unmarshalTest() {
        String name = "name";
        String stringValue1 = "47";
        String stringValue2 = "10";
        String[] stringValue = {stringValue1, stringValue2};
        ServiceXmlConfigPropertyAdapted.ConfigPropertyType[] configPropertyType = {null, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.stringType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.longType,
                ServiceXmlConfigPropertyAdapted.ConfigPropertyType.doubleType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.floatType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.integerType,
                ServiceXmlConfigPropertyAdapted.ConfigPropertyType.byteType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.charType, ServiceXmlConfigPropertyAdapted.ConfigPropertyType.booleanType,
                ServiceXmlConfigPropertyAdapted.ConfigPropertyType.shortType};
        ServiceXmlConfigPropertiesAdapted serviceXmlConfigPropertiesAdapted = new ServiceXmlConfigPropertiesAdapted();

        for (int i = 0; i < configPropertyType.length; i++) {
            ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted1 = new ServiceXmlConfigPropertyAdapted();
            ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted2 = new ServiceXmlConfigPropertyAdapted(name, configPropertyType[i], stringValue);
            ServiceXmlConfigPropertyAdapted[] properties1 = {serviceXmlConfigPropertyAdapted1, serviceXmlConfigPropertyAdapted2};

        Assert.assertThat("Instance of Map expected.", serviceXmlConfigPropertiesAdapter.unmarshal(serviceXmlConfigPropertiesAdapted), IsInstanceOf.instanceOf(Map.class));

            serviceXmlConfigPropertiesAdapted.setProperties(properties1);

        Assert.assertThat("Instance of Map expected.", serviceXmlConfigPropertiesAdapter.unmarshal(serviceXmlConfigPropertiesAdapted), IsInstanceOf.instanceOf(Map.class));
            Assert.assertEquals("Expected and actual values should be the same.", expectedProperties[i], serviceXmlConfigPropertiesAdapter.unmarshal(serviceXmlConfigPropertiesAdapted));

            serviceXmlConfigPropertyAdapted1.setArray(true);
            serviceXmlConfigPropertyAdapted2.setArray(true);
            ServiceXmlConfigPropertyAdapted[] properties2 = {serviceXmlConfigPropertyAdapted1, serviceXmlConfigPropertyAdapted2};
            serviceXmlConfigPropertiesAdapted.setProperties(properties2);

        Assert.assertThat("Instance of Map expected.", serviceXmlConfigPropertiesAdapter.unmarshal(serviceXmlConfigPropertiesAdapted), IsInstanceOf.instanceOf(Map.class));
        }
    }

    @Test(expected = NullPointerException.class)
    public void unmarshalNullPropTest() {
        serviceXmlConfigPropertiesAdapter.unmarshal(null);
    }
}
