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
package org.eclipse.kapua.service.job.context.internal;

import java.util.Properties;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.context.JobContextPropertyNames;
import org.eclipse.kapua.service.job.context.KapuaJobContext;

public class KapuaJobContextImpl implements KapuaJobContext {

    private static final String KAPUA_EXECUTION_ID = "KAPUA_EXECUTION_ID";

    private JobContext jobContext;

    public KapuaJobContextImpl(JobContext jobContext) {
        this.jobContext = jobContext;
    }

    @Override
    public KapuaId getScopeId() {
        Properties jobContextProperties = jobContext.getProperties();
        String scopeIdString = jobContextProperties.getProperty(JobContextPropertyNames.JOB_SCOPE_ID);
        return scopeIdString != null ? KapuaEid.parseCompactId(scopeIdString) : null;
    }

    @Override
    public KapuaId getJobId() {
        Properties jobContextProperties = jobContext.getProperties();
        String jobIdString = jobContextProperties.getProperty(JobContextPropertyNames.JOB_ID);
        return jobIdString != null ? KapuaEid.parseCompactId(jobIdString) : null;
    }

    @Override
    public String getJobName() {
        return jobContext.getJobName();
    }

    @Override
    public Object getTransientUserData() {
        return jobContext.getTransientUserData();
    }

    @Override
    public void setTransientUserData(Object data) {
        jobContext.setTransientUserData(data);
    }

    @Override
    public long getInstanceId() {
        return jobContext.getInstanceId();
    }

    @Override
    public long getExecutionId() {
        return jobContext.getExecutionId();
    }

    @Override
    public Properties getProperties() {
        return jobContext.getProperties();
    }

    @Override
    public BatchStatus getBatchStatus() {
        return jobContext.getBatchStatus();
    }

    @Override
    public String getExitStatus() {
        return jobContext.getExitStatus();
    }

    @Override
    public void setExitStatus(String status) {
        jobContext.setExitStatus(status);
    }

    @Override
    public KapuaId getKapuaExecutionId() {
        return (KapuaId) getProperties().get(KAPUA_EXECUTION_ID);
    }

    @Override
    public void setKapuaExecutionId(KapuaId kapuaExecutionId) {
        getProperties().put(KAPUA_EXECUTION_ID, kapuaExecutionId);
    }
}
