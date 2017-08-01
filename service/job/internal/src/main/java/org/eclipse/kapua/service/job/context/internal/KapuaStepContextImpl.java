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
package org.eclipse.kapua.service.job.context.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.job.context.KapuaStepContext;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

public class KapuaStepContextImpl implements KapuaStepContext {

    private int stepId;
    private Integer nextStepId;
    private List<JobStepProperty> jobStepProperties;

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public Integer getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(Integer nextStepId) {
        this.nextStepId = nextStepId;
    }

    public List<JobStepProperty> getStepProperties() {
        if (jobStepProperties == null) {
            jobStepProperties = new ArrayList<>();
        }
        return jobStepProperties;
    }

    public void setStepProperties(List<JobStepProperty> jobStepProperties) {
        this.jobStepProperties = jobStepProperties;
    }

    public <T> T getStepProperty(String stepPropertyName) {

        T stepPropertyValue = null;
        if (jobStepProperties != null) {
            for (JobStepProperty jobStepProperty : jobStepProperties) {
                if (jobStepProperty.getName().equals(stepPropertyName)) {

                }
            }
        }

        return stepPropertyValue;
    }

}
