/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.step.definition.internal;

import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class JobStepPropertyImpl implements JobStepProperty {

    @Basic
    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Basic
    @Column(name = "property_type", nullable = false, updatable = false)
    private String propertyType;

    @Basic
    @Column(name = "property_value", nullable = false, updatable = false)
    private String propertyValue;

    @Basic
    @Column(name = "example_value", nullable = false, updatable = false)
    private String propertyExampleValue;

    public JobStepPropertyImpl() {
    }

    private JobStepPropertyImpl(JobStepProperty jobStepProperty) {
        setName(jobStepProperty.getName());
        setPropertyType(jobStepProperty.getPropertyType());
        setPropertyValue(jobStepProperty.getPropertyValue());
        setExampleValue(jobStepProperty.getExampleValue());
    }

    public JobStepPropertyImpl(String name, String propertyType, String propertyValue, String propertyExampleValue) {
        setName(name);
        setPropertyType(propertyType);
        setPropertyValue(propertyValue);
        setExampleValue(propertyExampleValue);
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

    @Override
    public String getExampleValue() {
        return propertyExampleValue;
    }

    @Override
    public void setExampleValue(String exampleValue) {
        this.propertyExampleValue = exampleValue;
    }

    public static JobStepPropertyImpl parse(JobStepProperty jobStepProperty) {
        return jobStepProperty != null ? (jobStepProperty instanceof JobStepPropertyImpl ? (JobStepPropertyImpl) jobStepProperty : new JobStepPropertyImpl(jobStepProperty)) : null;
    }
}
