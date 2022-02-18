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
package org.eclipse.kapua.service.job.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.step.JobStep;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link Job} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "Job")
@Table(name = "job_job")
public class JobImpl extends AbstractKapuaNamedEntity implements Job {

    private static final long serialVersionUID = -5686107367635300337L;

    @Transient
    private List<JobStep> jobSteps;

    @Basic
    @Column(name = "job_xml_definition", nullable = true, updatable = true)
    private String jobXmlDefinition;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public JobImpl() {
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link Job}
     * @since 1.0.0
     */
    public JobImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param job
     * @throws KapuaException
     * @since 1.1.0
     */
    public JobImpl(Job job) throws KapuaException {
        super(job);

        setJobSteps(job.getJobSteps());
        setJobXmlDefinition(job.getJobXmlDefinition());
    }

    @Override
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

    @Override
    public String getJobXmlDefinition() {
        return jobXmlDefinition;
    }

    @Override
    public void setJobXmlDefinition(String jobXmlDefinition) {
        this.jobXmlDefinition = jobXmlDefinition;
    }

}
