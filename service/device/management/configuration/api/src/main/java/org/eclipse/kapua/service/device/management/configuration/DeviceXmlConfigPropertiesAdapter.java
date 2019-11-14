/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.configuration;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.commons.configuration.metatype.Password;
import org.eclipse.kapua.commons.util.CryptoUtil;
import org.eclipse.kapua.service.device.management.configuration.DeviceXmlConfigPropertyAdapted.ConfigPropertyType;

/**
 * Xml configuration properties adapter. It marshal and unmarshal configuration properties in a proper way.
 *
 * @since 1.0
 */
public class DeviceXmlConfigPropertiesAdapter extends XmlAdapter<DeviceXmlConfigPropertiesAdapted, Map<String, Object>> {

    @Override
    public DeviceXmlConfigPropertiesAdapted marshal(Map<String, Object> props) {
        List<DeviceXmlConfigPropertyAdapted> adaptedValues = new ArrayList<>();

        if (props != null) {
            props.forEach((name, value) -> {

                DeviceXmlConfigPropertyAdapted adaptedValue = new DeviceXmlConfigPropertyAdapted();
                adaptedValue.setName(name);

                if (value instanceof String) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.stringType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Long) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.longType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Double) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.doubleType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Float) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.floatType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Integer) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.integerType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Byte) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.byteType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Character) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.charType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Boolean) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.booleanType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Short) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.shortType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Password) {
                    adaptedValue.setArray(false);
                    adaptedValue.setEncrypted(true);
                    adaptedValue.setType(ConfigPropertyType.passwordType);
                    adaptedValue.setValues(new String[] { CryptoUtil.encodeBase64(value.toString()) });
                } else if (value instanceof String[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.stringType);
                    adaptedValue.setValues((String[]) value);
                } else if (value instanceof Long[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.longType);
                    Long[] nativeValues = (Long[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Double[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.doubleType);
                    Double[] nativeValues = (Double[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Float[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.floatType);
                    Float[] nativeValues = (Float[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Integer[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.integerType);
                    Integer[] nativeValues = (Integer[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Byte[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.byteType);
                    Byte[] nativeValues = (Byte[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Character[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.charType);
                    Character[] nativeValues = (Character[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Boolean[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.booleanType);
                    Boolean[] nativeValues = (Boolean[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Short[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.shortType);
                    Short[] nativeValues = (Short[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Password[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setEncrypted(true);
                    adaptedValue.setType(ConfigPropertyType.passwordType);
                    Password[] nativeValues = (Password[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = CryptoUtil.encodeBase64(nativeValues[i].toString());
                        }
                    }
                    adaptedValue.setValues(stringValues);
                }

                adaptedValues.add(adaptedValue);
            });
        }

        DeviceXmlConfigPropertiesAdapted result = new DeviceXmlConfigPropertiesAdapted();
        result.setProperties(adaptedValues.toArray(new DeviceXmlConfigPropertyAdapted[] {}));
        return result;
    }

    @Override
    public Map<String, Object> unmarshal(DeviceXmlConfigPropertiesAdapted adaptedPropsAdapted) {
        DeviceXmlConfigPropertyAdapted[] adaptedProps = adaptedPropsAdapted.getProperties();
        if (adaptedProps == null) {
            return new HashMap<>();
        }

        Map<String, Object> properties = new HashMap<>();
        for (DeviceXmlConfigPropertyAdapted adaptedProp : adaptedProps) {
            String propName = adaptedProp.getName();
            ConfigPropertyType type = adaptedProp.getType();
            if (type != null) {
                Object propValue = null;
                if (!adaptedProp.getArray()) {
                    switch (adaptedProp.getType()) {
                    case stringType:
                        propValue = adaptedProp.getValues()[0];
                        break;
                    case longType:
                        propValue = Long.parseLong(adaptedProp.getValues()[0]);
                        break;
                    case doubleType:
                        propValue = Double.parseDouble(adaptedProp.getValues()[0]);
                        break;
                    case floatType:
                        propValue = Float.parseFloat(adaptedProp.getValues()[0]);
                        break;
                    case integerType:
                        propValue = Integer.parseInt(adaptedProp.getValues()[0]);
                        break;
                    case byteType:
                        propValue = Byte.parseByte(adaptedProp.getValues()[0]);
                        break;
                    case charType:
                        String s = adaptedProp.getValues()[0];
                        propValue = s.charAt(0);
                        break;
                    case booleanType:
                        propValue = Boolean.parseBoolean(adaptedProp.getValues()[0]);
                        break;
                    case shortType:
                        propValue = Short.parseShort(adaptedProp.getValues()[0]);
                        break;
                    case passwordType:
                        propValue = adaptedProp.getValues()[0];
                        propValue =
                                adaptedProp.isEncrypted() ?
                                        new Password(CryptoUtil.decodeBase64((String) propValue)) :
                                        new Password((String) propValue);

                        break;
                    }
                } else {
                    switch (adaptedProp.getType()) {
                    case stringType:
                        propValue = adaptedProp.getValues();
                        break;
                    case longType:
                        Long[] longValues = new Long[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                longValues[i] = Long.parseLong(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = longValues;
                        break;
                    case doubleType:
                        Double[] doubleValues = new Double[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                doubleValues[i] = Double.parseDouble(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = doubleValues;
                        break;
                    case floatType:
                        Float[] floatValues = new Float[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                floatValues[i] = Float.parseFloat(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = floatValues;
                        break;
                    case integerType:
                        Integer[] intValues = new Integer[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                intValues[i] = Integer.parseInt(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = intValues;
                        break;
                    case byteType:
                        Byte[] byteValues = new Byte[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                byteValues[i] = Byte.parseByte(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = byteValues;
                        break;
                    case charType:
                        Character[] charValues = new Character[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                String s = adaptedProp.getValues()[i];
                                charValues[i] = s.charAt(0);
                            }
                        }
                        propValue = charValues;
                        break;
                    case booleanType:
                        Boolean[] booleanValues = new Boolean[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                booleanValues[i] = Boolean.parseBoolean(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = booleanValues;
                        break;
                    case shortType:
                        Short[] shortValues = new Short[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                shortValues[i] = Short.parseShort(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = shortValues;
                        break;
                    case passwordType:
                        Password[] pwdValues = new Password[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                pwdValues[i] =
                                        adaptedProp.isEncrypted() ?
                                                new Password(CryptoUtil.decodeBase64(adaptedProp.getValues()[i])) :
                                                new Password(adaptedProp.getValues()[i]);
                            }
                        }

                        propValue = pwdValues;
                        break;
                    }
                }
                properties.put(propName, propValue);
            }
        }
        return properties;
    }
}
