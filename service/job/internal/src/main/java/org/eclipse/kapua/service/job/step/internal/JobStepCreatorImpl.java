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
package org.eclipse.kapua.service.job.step.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionCreator;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

/**
 * {@link JobStepDefinitionCreator} encapsulates all the information needed to create a new JobStepDefinition in the system.<br>
 * The data provided will be used to seed the new JobStepDefinition.
 * 
 * @since 1.0.0
 *
 */
@KapuaProvider
public class JobStepCreatorImpl extends AbstractKapuaNamedEntityCreator<JobStep> implements JobStepCreator {

    private static final long serialVersionUID = 3119071638220738358L;

    protected JobStepCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    private String description;
    private KapuaId jobId;
    private int stepIndex;
    private KapuaId jobStepDefinitionId;
    private List<JobStepProperty> jobStepProperty;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
    public int getStepIndex() {
        return stepIndex;
    }

    @Override
    public void setStepIndex(int stepIndex) {
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
    public List<JobStepProperty> getJobStepProperties() {
        if (jobStepProperty == null) {
            this.jobStepProperty = new ArrayList<>();
        }

        return jobStepProperty;
    }

    @Override
    public void setJobStepProperties(List<JobStepProperty> jobStepProperty) {
        this.jobStepProperty = jobStepProperty;
    }

}
