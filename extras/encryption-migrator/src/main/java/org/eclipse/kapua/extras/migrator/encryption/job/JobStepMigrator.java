/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.migrator.encryption.job;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link JobStep} implementation.
 *
 * @since 2.0.0
 */
@Entity(name = "JobStep")
@Table(name = "job_job_step")
public class JobStepMigrator extends AbstractKapuaNamedEntity implements JobStep {

    @ElementCollection
    @CollectionTable(name = "job_job_step_properties", joinColumns = @JoinColumn(name = "step_id", referencedColumnName = "id"))
    private List<JobStepPropertyMigrator> stepProperties;

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    public JobStepMigrator() {
    }

    /**
     * Clone constructor.
     *
     * @param jobStep The {@link JobStep} to clone.
     * @since 2.0.0
     */
    public JobStepMigrator(JobStep jobStep) {
        super(jobStep);

        setStepProperties(jobStep.getStepProperties());
    }

    @Override
    public List<JobStepPropertyMigrator> getStepProperties() {
        if (stepProperties == null) {
            stepProperties = new ArrayList<>();
        }

        return stepProperties;
    }

    @Override
    public void setStepProperties(List<JobStepProperty> jobStepProperties) {

        this.stepProperties = new ArrayList<>();

        for (JobStepProperty sp : jobStepProperties) {
            this.stepProperties.add(JobStepPropertyMigrator.parse(sp));
        }
    }

    //
    // Attributes below do not require migration
    //

    @Override
    public KapuaId getJobId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setJobId(KapuaId jobId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStepIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStepIndex(int stepIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaId getJobStepDefinitionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setJobStepDefinitionId(KapuaId jobDefinitionId) {
        throw new UnsupportedOperationException();
    }
}
