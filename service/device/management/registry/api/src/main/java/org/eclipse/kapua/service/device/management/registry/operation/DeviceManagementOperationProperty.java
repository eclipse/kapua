/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry.operation;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "deviceManagementOperationProperty")
public interface DeviceManagementOperationProperty {

    @XmlElement(name = "name")
    String getName();

    void setName(String name);

    @XmlElement(name = "propertyType")
    String getPropertyType();

    void setPropertyType(String propertyType);

    @XmlElement(name = "propertyValue")
    String getPropertyValue();

    void setPropertyValue(String propertyValue);
}
