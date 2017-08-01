/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepPropertyImpl;

/**
 * {@link JobStepDefinition} entity.
 * 
 * @since 1.0
 *
 */
@Entity(name = "JobStep")
@Table(name = "jobStep_jobStep")
public class JobStepImpl extends AbstractKapuaNamedEntity implements JobStep {

    private static final long serialVersionUID = -5686107367635300337L;

    @Basic
    @Column(name = "description", nullable = true, updatable = true)
    public String description;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "job_id", nullable = false, updatable = false))
    })
    public KapuaEid jobId;

    public int stepIndex;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "job_step_definition_id", nullable = false, updatable = false))
    })
    public KapuaEid jobStepDefinitionId;

    @Transient
    public List<JobStepPropertyImpl> stepProperties;

    public JobStepImpl(KapuaId scopeId) {
        super(scopeId);
    }

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
        this.jobId = KapuaEid.parseKapuaId(jobId);
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
        this.jobStepDefinitionId = KapuaEid.parseKapuaId(jobStepDefinitionId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<JobStepPropertyImpl> getStepProperties() {
        if (stepProperties == null) {
            stepProperties = new ArrayList<>();
        }

        return stepProperties;
    }

    @Override
    public void setStepProperties(List<JobStepProperty> jobStepProperties) {

        this.stepProperties = new ArrayList<>();

        for (JobStepProperty sp : jobStepProperties) {
            this.stepProperties.add(JobStepPropertyImpl.parse(sp));
        }
    }

}
