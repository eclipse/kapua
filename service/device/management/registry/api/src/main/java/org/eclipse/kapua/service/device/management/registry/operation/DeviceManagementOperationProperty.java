/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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