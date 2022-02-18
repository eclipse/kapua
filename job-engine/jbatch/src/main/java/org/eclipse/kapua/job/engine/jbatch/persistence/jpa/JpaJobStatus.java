/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.jbatch.persistence.jpa;

import com.ibm.jbatch.container.status.JobStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * JPA counterpart for {@link JobStatus} object.
 *
 * @since 1.2.0
 */
@Entity(name = "JobStatus")
@Table(name = "jbtc_job_status")
public class JpaJobStatus extends AbstractJpaJbatchEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private long jobInstanceId;

    @Lob
    @Column(name = "obj")
    private byte[] obj;

    public JpaJobStatus() {
        // Required by JPA
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
