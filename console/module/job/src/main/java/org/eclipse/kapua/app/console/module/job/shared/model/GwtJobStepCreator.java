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
package org.eclipse.kapua.app.console.module.job.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityCreator;

import java.util.List;

public class GwtJobStepCreator extends GwtEntityCreator {

    private String jobName;
    private String jobDescription;
    private String jobId;
    private Integer stepIndex;
    private String jobStepDefinitionId;
    private List<GwtJobStepProperty> properties;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Integer getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(Integer stepIndex) {
        this.stepIndex = stepIndex;
    }

    public String getJobStepDefinitionId() {
        return jobStepDefinitionId;
    }

    public void setJobStepDefinitionId(String jobStepDefinitionId) {
        this.jobStepDefinitionId = jobStepDefinitionId;
    }

    public List<GwtJobStepProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<GwtJobStepProperty> properties) {
        this.properties = properties;
    }
}
