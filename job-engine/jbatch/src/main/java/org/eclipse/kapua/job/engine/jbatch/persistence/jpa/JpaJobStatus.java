/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.persistence.jpa;

import com.ibm.jbatch.container.status.JobStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity(name = "JobStatus")
@Table(name = "JOBSTATUS")
public class JpaJobStatus extends AbstractJpaJbatchEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private long jobInstanceId;

    @Lob
    @Column(name = "obj")
    private byte[] obj;

    public JpaJobStatus() {
    }

    public long getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public byte[] getObj() {
        return obj;
    }

    public JobStatus getObjAsJobStatus() {
        return readObject(getObj());
    }

    public void setObj(byte[] obj) {
        this.obj = obj;
    }

    public void setObj(JobStatus jobStatus) {
        setObj(writeObject(jobStatus));
    }

    public JobStatus toJobStatus() {
        return getObjAsJobStatus();
    }
}
