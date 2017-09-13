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
package org.eclipse.kapua.service.job.execution.internal;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.execution.JobExecution;

@Entity(name = "JobExecution")
@Table(name = "job_job_execution")
public class JobExecutionImpl extends AbstractKapuaUpdatableEntity implements JobExecution {

    private static final long serialVersionUID = -5686107367635300337L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "job_id", nullable = false, updatable = false))
    })
    private KapuaEid jobId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "started_on", nullable = false, updatable = false)
    public Date startedOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ended_on", nullable = true, updatable = true)
    public Date endedOn;

    public JobExecutionImpl() {
    }

    public JobExecutionImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getJobId() {
        return jobId;
    }

    @Override
    public void setJobId(KapuaId jobId) {
        this.jobId = KapuaEid.parseKapuaId(jobId);
    }

    public Date getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(Date startedOn) {
        this.startedOn = startedOn;
    }

    public Date getEndedOn() {
        return endedOn;
    }

    public void setEndedOn(Date endedOn) {
        this.endedOn = endedOn;
    }

}
