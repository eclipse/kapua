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

import com.ibm.jbatch.container.context.impl.StepContextImpl;
import com.ibm.jbatch.container.jobinstance.StepExecutionImpl;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.Metric;
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
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * JPA counterpart of {@link javax.batch.runtime.StepExecution}
 *
 * @since 1.2.0
 */
@Entity(name = "StepExecutionInstanceData")
@Table(name = "jbtc_step_execution_instance_data")
@NamedQueries({
        @NamedQuery(name = "StepExecutionInstanceData.selectByJobExecId",
                query = "SELECT seid FROM StepExecutionInstanceData seid WHERE seid.jobExecutionId = :jobExecutionId"),
        @NamedQuery(name = "StepExecutionInstanceData.mostRecentForJobInstance",
                query = "SELECT seid FROM StepExecutionInstanceData seid INNER JOIN ExecutionInstanceData eid ON seid.jobExecutionId = eid.id WHERE eid.jobInstanceId = :jobInstanceId ORDER BY seid.id DESC")
})
public class JpaStepExecutionInstanceData extends AbstractJpaJbatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stepexecid", updatable = false, nullable = false)
    private long id;

    @Column(name = "jobexecid", updatable = false, nullable = false)
    private long jobExecutionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "batchstatus")
    private BatchStatus batchStatus;

    @Column(name = "exitstatus")
    private String exitStatus;

    @Column(name = "stepname")
    private String stepName;

    @Column(name = "readcount")
    private long readCount;

    @Column(name = "writecount")
    private long writeCount;

    @Column(name = "commitcount")
    private long commitCount;

    @Column(name = "rollbackcount")
    private long rollbackCount;

    @Column(name = "readskipcount")
    private long readSkipCount;

    @Column(name = "processskipcount")
    private long processSkipCount;

    @Column(name = "filtercount")
    private long filterCount;

    @Column(name = "writeskipcount")
    private long writeSkipCount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startTime")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "endTime")
    private Date endTime;

    @Lob
    @Column(name = "persistentData")
    private byte[] persistentData;

    public JpaStepExecutionInstanceData() {
        // Required by JPA
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
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

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public long getReadCount() {
        return readCount;
    }

    public void setReadCount(long readCount) {
        this.readCount = readCount;
    }

    public long getWriteCount() {
        return writeCount;
    }

    public void setWriteCount(long writeCount) {
        this.writeCount = writeCount;
    }

    public long getCommitCount() {
        return commitCount;
    }

    public void setCommitCount(long commitCount) {
        this.commitCount = commitCount;
    }

    public long getRollbackCount() {
        return rollbackCount;
    }

    public void setRollbackCount(long rollbackCount) {
        this.rollbackCount = rollbackCount;
    }

    public long getReadSkipCount() {
        return readSkipCount;
    }

    public void setReadSkipCount(long readSkipCount) {
        this.readSkipCount = readSkipCount;
    }

    public long getProcessSkipCount() {
        return processSkipCount;
    }

    public void setProcessSkipCount(long processSkipCount) {
        this.processSkipCount = processSkipCount;
    }

    public long getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(long filterCount) {
        this.filterCount = filterCount;
    }

    public long getWriteskipcount() {
        return writeSkipCount;
    }

    public void setWriteskipcount(long writeskipcount) {
        this.writeSkipCount = writeskipcount;
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

    public byte[] getPersistentData() {
        return persistentData;
    }

    private Serializable getPersistentDataAsObject() {
        return readObject(getPersistentData());
    }

    public void setPersistentData(Serializable persistentUserData) {
        setPersistentData(writeObject(persistentUserData));
    }

    public void setPersistentData(byte[] persistentData) {
        this.persistentData = persistentData;
    }

    public StepExecutionImpl toStepExecution() {
        StepExecutionImpl stepExecution = new StepExecutionImpl(getJobExecutionId(), getId());

        stepExecution.setBatchStatus(getBatchStatus());
        stepExecution.setExitStatus(getExitStatus());
        stepExecution.setStepName(getStepName());
        stepExecution.setReadCount(getReadCount());
        stepExecution.setWriteCount(getWriteCount());
        stepExecution.setCommitCount(getCommitCount());
        stepExecution.setRollbackCount(getRollbackCount());
        stepExecution.setReadSkipCount(getReadSkipCount());
        stepExecution.setProcessSkipCount(getProcessSkipCount());
        stepExecution.setFilterCount(getFilterCount());
        stepExecution.setWriteSkipCount(getWriteskipcount());
        stepExecution.setStartTime(getStartTime());
        stepExecution.setEndTime(getEndTime());
        stepExecution.setPersistentUserData(getPersistentDataAsObject());

        return stepExecution;
    }

    public void readDataFromStepContext(StepContextImpl stepContext) {
        setBatchStatus(stepContext.getBatchStatus() == null ? BatchStatus.STARTING : stepContext.getBatchStatus());
        setExitStatus(stepContext.getExitStatus());
        setStepName(stepContext.getStepName());
        setStartTime(stepContext.getStartTimeTS());
        setEndTime(stepContext.getEndTimeTS());
        setPersistentData(stepContext.getPersistentUserData());

        // Metrics
        setReadCount(readMetric(stepContext.getMetric(Metric.MetricType.READ_COUNT)));
        setWriteCount(readMetric(stepContext.getMetric(Metric.MetricType.WRITE_COUNT)));
        setCommitCount(readMetric(stepContext.getMetric(Metric.MetricType.COMMIT_COUNT)));
        setRollbackCount(readMetric(stepContext.getMetric(Metric.MetricType.ROLLBACK_COUNT)));
        setReadSkipCount(readMetric(stepContext.getMetric(Metric.MetricType.READ_SKIP_COUNT)));
        setProcessSkipCount(readMetric(stepContext.getMetric(Metric.MetricType.PROCESS_SKIP_COUNT)));
        setFilterCount(readMetric(stepContext.getMetric(Metric.MetricType.FILTER_COUNT)));
        setWriteskipcount(readMetric(stepContext.getMetric(Metric.MetricType.WRITE_SKIP_COUNT)));
    }

    private long readMetric(Metric metric) {
        return metric != null ? metric.getValue() : 0;
    }
}
