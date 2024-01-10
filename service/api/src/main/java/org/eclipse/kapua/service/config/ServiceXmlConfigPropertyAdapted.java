/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.xml.XmlPropertyAdapted;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Represents a typed property.
 * Various flags help in the interpretation and semantics of the property value.
 * For example, a property value might be an array or the property value might have been encrypted.
 *
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceXmlConfigPropertyAdapted extends XmlPropertyAdapted<ServiceXmlConfigPropertyAdapted.ConfigPropertyType> {

    @XmlEnum
    public enum ConfigPropertyType {
        @XmlEnumValue("String") stringType,
        @XmlEnumValue("Long") longType,
        @XmlEnumValue("Double") doubleType,
        @XmlEnumValue("Float") floatType,
        @XmlEnumValue("Integer") integerType,
        @XmlEnumValue("Byte") byteType,
        @XmlEnumValue("Char") charType,
        @XmlEnumValue("Boolean") booleanType,
        @XmlEnumValue("Short") shortType,
//        @XmlEnumValue("Password")passwordType
    }

    public ServiceXmlConfigPropertyAdapted() {
    }

    public ServiceXmlConfigPropertyAdapted(String name, ConfigPropertyType type, String[] values) {
        super(name, type, values);
    }
}
