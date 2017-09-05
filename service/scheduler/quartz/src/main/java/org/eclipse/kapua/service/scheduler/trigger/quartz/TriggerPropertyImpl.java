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
package org.eclipse.kapua.service.scheduler.trigger.quartz;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.eclipse.kapua.service.scheduler.trigger.TriggerProperty;

@Embeddable
public class TriggerPropertyImpl implements TriggerProperty {

    @Basic
    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Basic
    @Column(name = "property_type", nullable = false, updatable = false)
    private String propertyType;

    @Basic
    @Column(name = "property_value", nullable = false, updatable = false)
    private String propertyValue;

    public TriggerPropertyImpl() {
    }

    private TriggerPropertyImpl(TriggerProperty jobStepProperty) {
        setName(jobStepProperty.getName());
        setPropertyType(jobStepProperty.getPropertyType());
        setPropertyValue(jobStepProperty.getPropertyValue());
    }

    public TriggerPropertyImpl(String name, String propertyType, String propertyValue) {
        setName(name);
        setPropertyType(propertyType);
        setPropertyValue(propertyValue);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public static TriggerPropertyImpl parse(TriggerProperty jobStepProperty) {
        return jobStepProperty != null ? (jobStepProperty instanceof TriggerPropertyImpl ? (TriggerPropertyImpl) jobStepProperty : new TriggerPropertyImpl(jobStepProperty)) : null;
    }
}
