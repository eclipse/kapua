/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.job.engine.exception.JobScopedEngineException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.ws.rs.core.Response;
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

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected JobScopedEngineExceptionInfo() {
        super();
    }

    /**
     * Constructor.
     *
     * @param httpStatus               The {@link Status} of the {@link Response}
     * @param jobScopedEngineException The root exception.
     * @since 1.0.0
     */
    public JobScopedEngineExceptionInfo(Status httpStatus, JobScopedEngineException jobScopedEngineException) {
        super(httpStatus, jobScopedEngineException);

        this.scopeId = jobScopedEngineException.getScopeId();
        this.jobId = jobScopedEngineException.getJobId();
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    public KapuaId getJobId() {
        return jobId;
    }


}
