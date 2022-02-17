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
package org.eclipse.kapua.service.job.step.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link JobStepCreator} implementation
 *
 * @since 1.0.0
 */
@KapuaProvider
public class JobStepCreatorImpl extends AbstractKapuaNamedEntityCreator<JobStep> implements JobStepCreator {

    private static final long serialVersionUID = 3119071638220738358L;

    private KapuaId jobId;
    private Integer stepIndex;
    private KapuaId jobStepDefinitionId;
    private List<JobStepProperty> jobStepProperty;

    public JobStepCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getJobId() {
        return jobId;
    }

    @Override
    public void setJobId(KapuaId jobId) {
        this.jobId = jobId;
    }

    @Override
    public Integer getStepIndex() {
        return stepIndex;
    }

    @Override
    public void setStepIndex(Integer stepIndex) {
        this.stepIndex = stepIndex;
    }

    @Override
    public KapuaId getJobStepDefinitionId() {
        return jobStepDefinitionId;
    }

    @Override
    public void setJobStepDefinitionId(KapuaId jobStepDefinitionId) {
        this.jobStepDefinitionId = jobStepDefinitionId;
    }

    @Override
    public List<JobStepProperty> getStepProperties() {
        if (jobStepProperty == null) {
            jobStepProperty = new ArrayList<>();
        }

        return jobStepProperty;
    }

    @Override
    public void setJobStepProperties(List<JobStepProperty> jobStepProperty) {
        this.jobStepProperty = jobStepProperty;
    }

}
