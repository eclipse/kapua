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

import org.eclipse.kapua.job.engine.exception.JobStoppingException;
import org.eclipse.kapua.job.engine.exception.KapuaJobEngineErrorCodes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

@XmlRootElement(name = "jobStartingExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobStoppingExceptionInfo extends JobEngineExceptionInfo {

    @XmlElement(name = "executionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    private KapuaId executionId;

    public JobStoppingExceptionInfo() {
        this(null);
    }

    public JobStoppingExceptionInfo(JobStoppingException jobStoppingException) {
        super(Status.INTERNAL_SERVER_ERROR, KapuaJobEngineErrorCodes.JOB_STOPPING, jobStoppingException);
    }

    public KapuaId getExecutionId() {
        return executionId;
    }

    public void setExecutionId(KapuaId executionId) {
        this.executionId = executionId;
    }

}
