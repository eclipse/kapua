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
package org.eclipse.kapua.job.engine.commons.wrappers;

import com.google.common.base.Strings;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.job.engine.commons.exception.ReadJobPropertyException;
import org.eclipse.kapua.job.engine.commons.model.JobTargetSublist;
import org.eclipse.kapua.model.id.KapuaId;
import org.xml.sax.SAXException;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.util.Properties;

public class JobContextWrapper {

    private static final String KAPUA_EXECUTION_ID = "KAPUA_EXECUTION_ID";

    private JobContext jobContext;

    public JobContextWrapper(JobContext jobContext) {
        this.jobContext = jobContext;
    }

    public KapuaId getScopeId() {
        Properties jobContextProperties = jobContext.getProperties();
        String scopeIdString = jobContextProperties.getProperty(JobContextPropertyNames.JOB_SCOPE_ID);
        return scopeIdString != null ? KapuaEid.parseCompactId(scopeIdString) : null;
    }

    public KapuaId getJobId() {
        Properties jobContextProperties = jobContext.getProperties();
        String jobIdString = jobContextProperties.getProperty(JobContextPropertyNames.JOB_ID);
        return jobIdString != null ? KapuaEid.parseCompactId(jobIdString) : null;
    }

    public JobTargetSublist getTargetSublist() {
        Properties jobContextProperties = jobContext.getProperties();
        String jobTargetSublistString = jobContextProperties.getProperty(JobContextPropertyNames.JOB_TARGET_SUBLIST);

        try {
            return XmlUtil.unmarshal(jobTargetSublistString, JobTargetSublist.class);
        } catch (JAXBException | XMLStreamException | SAXException e) {
            throw new ReadJobPropertyException(e, JobContextPropertyNames.JOB_TARGET_SUBLIST, jobTargetSublistString);
        }
    }

    public Integer getFromStepIndex() {
        Properties jobContextProperties = jobContext.getProperties();
        String fromStepIndexString = jobContextProperties.getProperty(JobContextPropertyNames.JOB_STEP_FROM_INDEX);

        return Strings.isNullOrEmpty(fromStepIndexString) ? null : Integer.valueOf(fromStepIndexString);
    }

    public String getJobName() {
        return jobContext.getJobName();
    }

    public Object getTransientUserData() {
        return jobContext.getTransientUserData();
    }

    public void setTransientUserData(Object data) {
        jobContext.setTransientUserData(data);
    }

    public long getInstanceId() {
        return jobContext.getInstanceId();
    }

    public long getExecutionId() {
        return jobContext.getExecutionId();
    }

    public Properties getProperties() {
        return jobContext.getProperties();
    }

    public BatchStatus getBatchStatus() {
        return jobContext.getBatchStatus();
    }

    public String getExitStatus() {
        return jobContext.getExitStatus();
    }

    public void setExitStatus(String status) {
        jobContext.setExitStatus(status);
    }

    public KapuaId getKapuaExecutionId() {
        return (KapuaId) getProperties().get(KAPUA_EXECUTION_ID);
    }

    public void setKapuaExecutionId(KapuaId kapuaExecutionId) {
        getProperties().put(KAPUA_EXECUTION_ID, kapuaExecutionId);
    }
}
