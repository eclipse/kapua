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
package org.eclipse.kapua.service.job.internal;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;

/**
 * {@link JobStepDefinition} entity.
 * 
 * @since 1.0
 *
 */
@Entity(name = "Job")
@Table(name = "job_job")
public class JobImpl extends AbstractKapuaNamedEntity implements Job {

    private static final long serialVersionUID = -5686107367635300337L;

    @Basic
    @Column(name = "description", nullable = true, updatable = true)
    private String description;

    @Transient
    private List<JobStep> jobSteps;

    @Basic
    @Column(name = "job_xml_definition", nullable = true, updatable = true)
    private String jobXmlDefinition;

    public JobImpl() {
    }

    public JobImpl(KapuaId scopeId) {
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

    public List<JobStep> getJobSteps() {
        if (jobSteps == null) {
            jobSteps = new ArrayList<>();
        }

        return jobSteps;
    }

    @Override
    public void setJobSteps(List<JobStep> jobSteps) {
        this.jobSteps = jobSteps;
    }

    public String getJobXmlDefinition() {
        return jobXmlDefinition;
    }

    public void setJobXmlDefinition(String jobXmlDefinition) {
        this.jobXmlDefinition = jobXmlDefinition;
    }

}
