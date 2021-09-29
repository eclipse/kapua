/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.core.exception.model;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.job.engine.exception.JobScopedEngineException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "jobScopedEngineExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobScopedEngineExceptionInfo extends JobEngineExceptionInfo {

    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    private KapuaId scopeId;

    @XmlElement(name = "jobId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    private KapuaId jobId;

    public JobScopedEngineExceptionInfo() {
        super();
    }

    public JobScopedEngineExceptionInfo(Status httpStatus, KapuaErrorCode kapuaErrorCode, JobScopedEngineException jobScopedEngineException) {
        super(httpStatus, kapuaErrorCode, jobScopedEngineException);

        setScopeId(jobScopedEngineException.getScopeId());
        setJobId(jobScopedEngineException.getJobId());
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    private void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    public KapuaId getJobId() {
        return jobId;
    }

    public void setJobId(KapuaId jobId) {
        this.jobId = jobId;
    }

}
