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
import org.eclipse.kapua.service.config.ServiceXmlConfigPropertyAdapted.ConfigPropertyType;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;


@Category(JUnitTests.class)
public class ServiceXmlConfigPropertiesAdapterTest {

    @Test
    public void marshalTest() throws Exception {
        ServiceXmlConfigPropertiesAdapter instance = new ServiceXmlConfigPropertiesAdapter();
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

        Map<ConfigPropertyType, Object> typeToValues = new HashMap<ConfigPropertyType, Object>() {{
            put(ConfigPropertyType.stringType, stringValue);
            put(ConfigPropertyType.longType, longValue);
            put(ConfigPropertyType.doubleType, doubleValue);
            put(ConfigPropertyType.floatType, floatValue);
            put(ConfigPropertyType.byteType, byteValue);
            put(ConfigPropertyType.integerType, integerValue);
            put(ConfigPropertyType.charType, characterValue);
            put(ConfigPropertyType.booleanType, booleanValue);
            put(ConfigPropertyType.shortType, shortValue);
            put(ConfigPropertyType.stringType, stringArray);
            put(ConfigPropertyType.longType, longArray);
            put(ConfigPropertyType.doubleType, doubleArray);
            put(ConfigPropertyType.floatType, floatArray);
            put(ConfigPropertyType.byteType, byteArray);
            put(ConfigPropertyType.charType, characterArray);
            put(ConfigPropertyType.integerType, integerArray);
            put(ConfigPropertyType.booleanType, booleanArray);
            put(ConfigPropertyType.shortType, shortArray);
        }};
        for (final Map.Entry<ConfigPropertyType, Object> typeToValue : typeToValues.entrySet()) {
            Map<String, Object> props = new HashMap<>();
            props.put("key", typeToValue.getValue());
            final ServiceXmlConfigPropertiesAdapted marshalled = instance.marshal(props);
            Assert.assertThat("Instance of ServiceXmlConfigPropertiesAdapted expected.", marshalled, IsInstanceOf.instanceOf(ServiceXmlConfigPropertiesAdapted.class));
            Assert.assertEquals(typeToValue.getKey(), marshalled.getProperties()[0].getType());
        }
    }

    @Test
    public void marshalNullPropsTest() throws Exception {
        ServiceXmlConfigPropertiesAdapter instance = new ServiceXmlConfigPropertiesAdapter();
        Assert.assertThat("Instance of ServiceXmlConfigPropertiesAdapted expected.", instance.marshal(null), IsInstanceOf.instanceOf(ServiceXmlConfigPropertiesAdapted.class));
    }

    Map[] expectedProperties;

    @Test
    public void unmarshalTest() throws Exception {

        ServiceXmlConfigPropertiesAdapter instance = new ServiceXmlConfigPropertiesAdapter();

        Map<String, Object> expectedProperty0 = new HashMap<>();
        Map<String, Object> expectedProperty1 = new HashMap<>();
        Map<String, Object> expectedProperty2 = new HashMap<>();
        Map<String, Object> expectedProperty3 = new HashMap<>();
        Map<String, Object> expectedProperty4 = new HashMap<>();
        Map<String, Object> expectedProperty5 = new HashMap<>();
        Map<String, Object> expectedProperty6 = new HashMap<>();
        Map<String, Object> expectedProperty7 = new HashMap<>();
        Map<String, Object> expectedProperty8 = new HashMap<>();
        expectedProperty0.put("name0", new String[]{"47", "10"});
        expectedProperty1.put("name1", new Long[]{47l, 10l});
        expectedProperty2.put("name2", new Double[]{47d, 10d});
        expectedProperty3.put("name3", new Float[]{47f, 10f});
        expectedProperty4.put("name4", new Integer[]{47, 10});
        expectedProperty5.put("name5", new Byte[]{(byte) 47, (byte) 10});
        expectedProperty6.put("name6", new Character[]{'4', '1'});
        expectedProperty7.put("name7", new Boolean[]{false, false});
        expectedProperty8.put("name8", new Short[]{(short) 47, (short) 10});
        expectedProperties = new Map[]{expectedProperty0, expectedProperty1, expectedProperty2, expectedProperty3, expectedProperty4, expectedProperty5, expectedProperty6, expectedProperty7, expectedProperty8};

        String name = "name";
        String stringValue1 = "47";
        String stringValue2 = "10";
        String[] stringValue = {stringValue1, stringValue2};
        ConfigPropertyType[] configPropertyType = {
                ConfigPropertyType.stringType,
                ConfigPropertyType.longType,
                ConfigPropertyType.doubleType,
                ConfigPropertyType.floatType,
                ConfigPropertyType.integerType,
                ConfigPropertyType.byteType,
                ConfigPropertyType.charType,
                ConfigPropertyType.booleanType,
                ConfigPropertyType.shortType};
        ServiceXmlConfigPropertiesAdapted serviceXmlConfigPropertiesAdapted = new ServiceXmlConfigPropertiesAdapted();

        for (int i = 0; i < configPropertyType.length; i++) {
            ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted1 = new ServiceXmlConfigPropertyAdapted("fooname", configPropertyType[0], "foovalue");
            ServiceXmlConfigPropertyAdapted serviceXmlConfigPropertyAdapted2 = new ServiceXmlConfigPropertyAdapted(name + i, configPropertyType[i], stringValue);
            ServiceXmlConfigPropertyAdapted[] properties1 = new ServiceXmlConfigPropertyAdapted[]{serviceXmlConfigPropertyAdapted2};

            Assert.assertThat("Instance of Map expected.", instance.unmarshal(serviceXmlConfigPropertiesAdapted), IsInstanceOf.instanceOf(Map.class));

            serviceXmlConfigPropertiesAdapted.setProperties(properties1);

            Assert.assertThat("Instance of Map expected.", instance.unmarshal(serviceXmlConfigPropertiesAdapted), IsInstanceOf.instanceOf(Map.class));
            final Map expectedProperty = expectedProperties[i];
            final Map<String, Object> got = instance.unmarshal(serviceXmlConfigPropertiesAdapted);
            Assert.assertEquals("Expected and actual values should be the same.", expectedProperty.keySet(), got.keySet());
            Assert.assertArrayEquals("Expected and actual values should be the same.", expectedProperty.values().toArray(), got.values().toArray());

            serviceXmlConfigPropertyAdapted1.setArray(true);
            serviceXmlConfigPropertyAdapted2.setArray(true);
            ServiceXmlConfigPropertyAdapted[] properties2 = new ServiceXmlConfigPropertyAdapted[]{serviceXmlConfigPropertyAdapted1, serviceXmlConfigPropertyAdapted2};
            serviceXmlConfigPropertiesAdapted.setProperties(properties2);

            Assert.assertThat("Instance of Map expected.", instance.unmarshal(serviceXmlConfigPropertiesAdapted), IsInstanceOf.instanceOf(Map.class));
        }
    }

    @Test(expected = NullPointerException.class)
    public void unmarshalNullPropTest() throws Exception {
        new ServiceXmlConfigPropertiesAdapter().unmarshal(null);
    }
}
