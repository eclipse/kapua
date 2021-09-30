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

import org.eclipse.kapua.job.engine.exception.JobStoppingException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "jobStartingExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobStoppingExceptionInfo extends JobScopedEngineExceptionInfo {

    @XmlElement(name = "executionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    private KapuaId executionId;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected JobStoppingExceptionInfo() {
        super();
    }

    /**
     * Constructor.
     *
     * @param jobStoppingException The root exception.
     * @since 1.0.0
     */
    public JobStoppingExceptionInfo(JobStoppingException jobStoppingException) {
        super(Status.INTERNAL_SERVER_ERROR, jobStoppingException);

        this.executionId = jobStoppingException.getJobExecutionId();
    }

    /**
     * Gets the {@link JobStoppingException#getJobExecutionId()}.
     *
     * @return The {@link JobStoppingException#getJobExecutionId()}.
     * @since 1.0.0
     */
    public KapuaId getExecutionId() {
        return executionId;
    }
}
