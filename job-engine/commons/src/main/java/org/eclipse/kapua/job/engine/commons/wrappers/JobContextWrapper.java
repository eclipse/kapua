/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.commons.wrappers;

import com.google.common.base.Strings;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.job.engine.commons.exception.ReadJobPropertyException;
import org.eclipse.kapua.job.engine.commons.logger.JobLogger;
import org.eclipse.kapua.job.engine.commons.model.JobTargetSublist;
import org.eclipse.kapua.job.engine.commons.model.JobTransientUserData;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.xml.sax.SAXException;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.util.Properties;

/**
 * {@link JobContextWrapper} wraps the {@link JobContext} and offers utility methods around it.
 *
 * @since 1.0.0
 */
public class JobContextWrapper {

    private JobContext jobContext;

    /**
     * Constructor from the {@code inject}ed {@link JobContext}.
     * <p>
     * Wraps the given {@link JobContext}
     *
     * @param jobContext The {@link JobContext} to wrap.
     * @since 1.1.0
     */
    public JobContextWrapper(JobContext jobContext) {
        this.jobContext = jobContext;
    }

    /**
     * Gets the scope {@link KapuaId} of the {@link org.eclipse.kapua.service.job.execution.JobExecution}.
     *
     * @return The current scope {@link KapuaId} of the {@link org.eclipse.kapua.service.job.execution.JobExecution}.
     * @since 1.0.0
     */
    public KapuaId getScopeId() {
        String scopeIdString = getProperties().getProperty(JobContextPropertyNames.JOB_SCOPE_ID);
        return scopeIdString != null ? KapuaEid.parseCompactId(scopeIdString) : null;
    }

    /**
     * Gets the {@link org.eclipse.kapua.service.job.Job} {@link KapuaId} of the {@link org.eclipse.kapua.service.job.execution.JobExecution}.
     *
     * @return The current {@link org.eclipse.kapua.service.job.Job} {@link KapuaId} of the {@link org.eclipse.kapua.service.job.execution.JobExecution}.
     * @since 1.0.0
     */
    public KapuaId getJobId() {
        String jobIdString = getProperties().getProperty(JobContextPropertyNames.JOB_ID);
        return jobIdString != null ? KapuaEid.parseCompactId(jobIdString) : null;
    }

    /**
     * Gets the {@link JobTargetSublist} of the {@link org.eclipse.kapua.service.job.execution.JobExecution}.
     *
     * @return The current {@link JobTargetSublist} of the {@link org.eclipse.kapua.service.job.execution.JobExecution}.
     * @since 1.0.0
     */
    public JobTargetSublist getTargetSublist() {
        String jobTargetSublistString = getProperties().getProperty(JobContextPropertyNames.JOB_TARGET_SUBLIST);

        try {
            return XmlUtil.unmarshal(jobTargetSublistString, JobTargetSublist.class);
        } catch (JAXBException | XMLStreamException | SAXException e) {
            throw new ReadJobPropertyException(e, JobContextPropertyNames.JOB_TARGET_SUBLIST, jobTargetSublistString);
        }
    }

    /**
     * Gets the {@link JobExecution} {@link KapuaId} to resume
     *
     * @return The {@link JobExecution} {@link KapuaId} to resume
     * @since 1.1.0
     */
    public KapuaId getResumedJobExecutionId() {
        String resumedKapuaExecutionIdString = getProperties().getProperty(JobContextPropertyNames.RESUMED_KAPUA_EXECUTION_ID);
        return Strings.isNullOrEmpty(resumedKapuaExecutionIdString) ? null : KapuaEid.parseCompactId(resumedKapuaExecutionIdString);
    }

    /**
     * Gets whether or not the {@link org.eclipse.kapua.service.job.targets.JobTarget}s needs to be reset to the given {@link #getFromStepIndex()}.
     *
     * @return {@code true} if the {@link org.eclipse.kapua.service.job.targets.JobTarget}s needs to be reset to the given {@link #getFromStepIndex()}, {@code false} otherwise.
     * @since 1.1.0
     */
    public boolean getResetStepIndex() {
        String resetFromIndexString = getProperties().getProperty(JobContextPropertyNames.RESET_STEP_INDEX);
        return resetFromIndexString != null && Boolean.valueOf(resetFromIndexString);
    }

    /**
     * Gets the start step index of the {@link org.eclipse.kapua.service.job.execution.JobExecution}.
     *
     * @return The start step index of the {@link org.eclipse.kapua.service.job.execution.JobExecution}.
     * @since 1.0.0
     */
    public Integer getFromStepIndex() {
        String fromStepIndexString = getProperties().getProperty(JobContextPropertyNames.JOB_STEP_FROM_INDEX);
        return Strings.isNullOrEmpty(fromStepIndexString) ? null : Integer.valueOf(fromStepIndexString);
    }

    /**
     * Gets whether or not this {@link JobExecution} should be enqueued or not.
     *
     * @return {@code true} if this {@link JobExecution} should be enqueued, {@code false} otherwise.
     * @since 1.1.0
     */
    public boolean getEnqueue() {
        String enqueueString = getProperties().getProperty(JobContextPropertyNames.ENQUEUE);
        return enqueueString != null && Boolean.valueOf(enqueueString);
    }

    /**
     * Gets the {@link JobTransientUserData}.
     *
     * @return The {@link JobTransientUserData}.
     * @since 1.1.0
     */
    public JobTransientUserData getJobTransientUserData() {
        JobTransientUserData transientUserData = (JobTransientUserData) getTransientUserData();

        if (transientUserData == null) {
            transientUserData = new JobTransientUserData();
            setTransientUserData(transientUserData);
        }
        return transientUserData;
    }

    /**
     * Gets the {@link JobLogger}.
     * <p>
     * If it does not exist, it instantiates a new one.
     *
     * @return The {@link JobLogger}.
     * @since 1.1.0
     */
    public JobLogger getJobLogger() {

        JobLogger jobLogger = getJobTransientUserData().getJobLogger();

        if (jobLogger == null) {
            jobLogger = new JobLogger(getScopeId(), getJobId(), getJobName());
            getJobTransientUserData().setJobLogger(jobLogger);
        }

        return jobLogger;
    }

    /**
     * @return {@link JobContext#getJobName()}.
     * @see JobContext#getJobName
     * @since 1.0.0
     */
    public String getJobName() {
        return jobContext.getJobName();
    }

    /**
     * @return {@link JobContext#getTransientUserData()}.
     * @see JobContext#getTransientUserData().
     * @since 1.0.0
     */
    private Object getTransientUserData() {
        return jobContext.getTransientUserData();
    }

    /**
     * @param data {@link JobContext#setTransientUserData(Object)}.
     * @see JobContext#setTransientUserData(Object).
     * @since 1.0.0
     */
    private void setTransientUserData(Object data) {
        jobContext.setTransientUserData(data);
    }

    /**
     * @return {@link JobContext#getInstanceId()}.
     * @see JobContext#getInstanceId
     * @since 1.0.0
     */
    public long getInstanceId() {
        return jobContext.getInstanceId();
    }

    /**
     * @return {@link JobContext#getExecutionId()}.
     * @see JobContext#getExecutionId
     * @since 1.0.0
     */
    public long getExecutionId() {
        return jobContext.getExecutionId();
    }

    /**
     * @return {@link JobContext#getProperties()}.
     * @see JobContext#getProperties
     * @since 1.0.0
     */
    public Properties getProperties() {
        return jobContext.getProperties();
    }

    /**
     * @return {@link JobContext#getBatchStatus()}.
     * @see JobContext#getBatchStatus
     * @since 1.0.0
     */
    public BatchStatus getBatchStatus() {
        return jobContext.getBatchStatus();
    }

    /**
     * @return {@link JobContext#getExitStatus()}.
     * @see JobContext#getExitStatus
     * @since 1.0.0
     */
    public String getExitStatus() {
        return jobContext.getExitStatus();
    }

    /**
     * @param status {@link JobContext#setExitStatus(String)}.
     * @see JobContext#setExitStatus(String)
     * @since 1.0.0
     */
    public void setExitStatus(String status) {
        jobContext.setExitStatus(status);
    }

    /**
     * Gets the current {@link JobExecution#getId()}.
     *
     * @return The current {@link JobExecution#getId()}.
     * @since 1.0.0
     */
    public KapuaId getKapuaExecutionId() {
        return (KapuaId) getProperties().get(JobContextPropertyNames.KAPUA_EXECUTION_ID);
    }

    /**
     * Sets the current {@link JobExecution#getId()}.
     *
     * @param kapuaExecutionId The current {@link JobExecution#getId()}.
     * @since 1.0.0
     */
    public void setKapuaExecutionId(KapuaId kapuaExecutionId) {
        getProperties().put(JobContextPropertyNames.KAPUA_EXECUTION_ID, kapuaExecutionId);
    }
}
