/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.job.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import java.util.List;

public class GwtJobStep extends GwtUpdatableEntityModel implements IsSerializable {

    private final static String JOB_STEP_NAME = "jobStepName";
    private final static String DESCRIPTION = "description";

    public String getJobStepName() {
        return get(JOB_STEP_NAME);
    }

    public String getUnescapedJobStepName() {
        return (String) getUnescaped(JOB_STEP_NAME);
    }

    public void setJobStepName(String jobStepName) {
        set(JOB_STEP_NAME, jobStepName);
    }

    public String getDescription() {
        return get(DESCRIPTION);
    }

    public String getUnescapedDescription() {
        return (String) getUnescaped(DESCRIPTION);
    }

    public void setDescription(String description) {
        set(DESCRIPTION, description);
    }

    public String getJobId() {
        return get("jobId");
    }

    public void setJobId(String jobId) {
        set("jobId", jobId);
    }

    public int getStepIndex() {
        return get("stepIndex");
    }

    public void setStepIndex(int stepIndex) {
        set("stepIndex", stepIndex);
    }

    public String getJobStepDefinitionId() {
        return get("jobStepDefinitionId");
    }

    public void setJobStepDefinitionId(String jobStepDefinitionId) {
        set("jobStepDefinitionId", jobStepDefinitionId);
    }

    public String getJobStepDefinitionName() {
        return get("jobStepDefinitionName");
    }

    public void setJobStepDefinitionName(String jobStepDefinitionName) {
        set("jobStepDefinitionName", jobStepDefinitionName);
    }

    public <P extends GwtJobStepProperty> List<P> getStepProperties() {
        return get("jobStepProperties");
    }

    public void setStepProperties(List<GwtJobStepProperty> jobStepProperties) {
        set("jobStepProperties", jobStepProperties);
    }

}
