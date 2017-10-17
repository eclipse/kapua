/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.registry.operation.internal;

import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationProperty;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DeviceManagementOperationPropertyImpl implements DeviceManagementOperationProperty {

    @Basic
    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Basic
    @Column(name = "property_type", nullable = false, updatable = false)
    private String propertyType;

    @Basic
    @Column(name = "property_value", nullable = false, updatable = false)
    private String propertyValue;

    public DeviceManagementOperationPropertyImpl() {
    }

    private DeviceManagementOperationPropertyImpl(DeviceManagementOperationProperty jobStepProperty) {
        setName(jobStepProperty.getName());
        setPropertyType(jobStepProperty.getPropertyType());
        setPropertyValue(jobStepProperty.getPropertyValue());
    }

    public DeviceManagementOperationPropertyImpl(String name, String propertyType, String propertyValue) {
        setName(name);
        setPropertyType(propertyType);
        setPropertyValue(propertyValue);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPropertyType() {
        return propertyType;
    }

    @Override
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    @Override
    public String getPropertyValue() {
        return propertyValue;
    }

    @Override
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public static DeviceManagementOperationPropertyImpl parse(DeviceManagementOperationProperty jobStepProperty) {
        return jobStepProperty != null ?
                (jobStepProperty instanceof DeviceManagementOperationPropertyImpl ?
                        (DeviceManagementOperationPropertyImpl) jobStepProperty :
                        new DeviceManagementOperationPropertyImpl(jobStepProperty)) :
                null;
    }
}
