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

import com.ibm.jbatch.container.jobinstance.JobOperatorJobExecution;

import javax.batch.runtime.BatchStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

/**
 * JPA counterpart of the {@link JobOperatorJobExecution} object.
 *
 * @since 1.2.0
 */
@Entity(name = "ExecutionInstanceData")
@Table(name = "jbtc_execution_instance_data")
@NamedQueries({
        @NamedQuery(name = "ExecutionInstanceData.getByJobInstance",
                query = "SELECT eid FROM ExecutionInstanceData eid WHERE eid.jobInstanceId = :jobInstanceId ORDER BY eid.createTime DESC"),
        @NamedQuery(name = "ExecutionInstanceData.getRunningByJobName",
                query = "SELECT eid.id FROM ExecutionInstanceData eid INNER JOIN JobInstanceData jid ON eid.jobInstanceId = jid.id WHERE eid.batchStatus IN (:status1, :status2, :status3) AND jid.name = :jobName")
})
public class JpaExecutionInstanceData extends AbstractJpaJbatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jobexecid", updatable = false, nullable = false)
    private long id;

    @Column(name = "jobinstanceid", updatable = false, nullable = false)
    private long jobInstanceId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createtime")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "starttime")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "endtime")
    private Date endTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatetime")
    private Date updateTime;

    @Lob
    @Column(name = "parameters")
    private byte[] parameters;

    @Enumerated(EnumType.STRING)
    @Column(name = "batchstatus")
    private BatchStatus batchStatus;

    @Column(name = "exitstatus")
    private String exitStatus;

    public JpaExecutionInstanceData() {
        // Required by JPA
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public Timestamp getCreateTime() {
        return createTime != null ? new Timestamp(createTime.getTime()) : null;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getStartTime() {
        return startTime != null ? new Timestamp(startTime.getTime()) : null;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime != null ? new Timestamp(endTime.getTime()) : null;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime != null ? new Timestamp(updateTime.getTime()) : null;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public byte[] getParameters() {
        return parameters;
    }

    public Properties getParametersAsProperties() {
        return readObject(getParameters());
    }

    public void setParameters(byte[] parameters) {
        this.parameters = parameters;
    }

    public void setParameters(Properties parameters) {
        setParameters(writeObject(parameters));
    }

    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(BatchStatus batchStatus) {
        this.batchStatus = batchStatus;
    }

    public String getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(String exitStatus) {
        this.exitStatus = exitStatus;
    }

    public JobOperatorJobExecution toJobExecution() {
        JobOperatorJobExecution jobOperatorJobExecution = new JobOperatorJobExecution(getId(), getJobInstanceId());

        jobOperatorJobExecution.setCreateTime(getCreateTime());
        jobOperatorJobExecution.setStartTime(getStartTime());
        jobOperatorJobExecution.setEndTime(getEndTime());
        jobOperatorJobExecution.setLastUpdateTime(getUpdateTime());
        jobOperatorJobExecution.setJobParameters(getParametersAsProperties());
        jobOperatorJobExecution.setBatchStatus(getBatchStatus().name());
        jobOperatorJobExecution.setExitStatus(getExitStatus());

        return jobOperatorJobExecution;
    }
}
