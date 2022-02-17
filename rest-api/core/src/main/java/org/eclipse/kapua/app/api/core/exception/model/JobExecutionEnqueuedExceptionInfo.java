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

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.job.engine.exception.JobExecutionEnqueuedException;
import org.eclipse.kapua.job.engine.exception.KapuaJobEngineErrorCodes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

@XmlRootElement(name = "jobExecutionEnqueuedExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobExecutionEnqueuedExceptionInfo extends JobEngineExceptionInfo {

    @XmlElement(name = "executionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    private KapuaId executionId;

    @XmlElement(name = "enqueuedExecutionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    private KapuaId enqueuedJobExecutionId;

    public JobExecutionEnqueuedExceptionInfo() {
        this(null);
    }

    public JobExecutionEnqueuedExceptionInfo(JobExecutionEnqueuedException jobExecutionEnqueuedException) {
        super(Status.INTERNAL_SERVER_ERROR, KapuaJobEngineErrorCodes.JOB_EXECUTION_ENQUEUED, jobExecutionEnqueuedException);
    }

    public KapuaId getExecutionId() {
        return executionId;
    }

    public void setExecutionId(KapuaId executionId) {
        this.executionId = executionId;
    }

    public KapuaId getEnqueuedJobExecutionId() {
        return enqueuedJobExecutionId;
    }

    public void setEnqueuedJobExecutionId(KapuaId enqueuedJobExecutionId) {
        this.enqueuedJobExecutionId = enqueuedJobExecutionId;
    }

}
