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

import org.eclipse.kapua.job.engine.exception.JobAlreadyRunningException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Set;

@XmlRootElement(name = "jobAlreadyRunningExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobAlreadyRunningExceptionInfo extends JobScopedEngineExceptionInfo {

    @XmlElement(name = "executionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    private KapuaId executionId;

    @XmlElement(name = "targetId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @XmlElementWrapper(name = "jobTargetIdSubset")
    private Set<KapuaId> jobTargetIdSubset;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected JobAlreadyRunningExceptionInfo() {
        super();
    }

    /**
     * Constructor.
     *
     * @param jobAlreadyRunningException The root exception.
     * @since 1.0.0
     */
    public JobAlreadyRunningExceptionInfo(JobAlreadyRunningException jobAlreadyRunningException) {
        super(Status.INTERNAL_SERVER_ERROR, jobAlreadyRunningException);

        this.executionId = jobAlreadyRunningException.getJobExecutionId();
        this.jobTargetIdSubset = jobAlreadyRunningException.getJobTargetIdSubset();
    }

    /**
     * Gets the {@link JobAlreadyRunningException#getJobExecutionId()}.
     *
     * @return The {@link JobAlreadyRunningException#getJobExecutionId()}.
     * @since 1.0.0
     */
    public KapuaId getExecutionId() {
        return executionId;
    }

    /**
     * Gets the {@link JobAlreadyRunningException#getJobTargetIdSubset()}.
     *
     * @return The {@link JobAlreadyRunningException#getJobTargetIdSubset()}.
     * @since 1.0.0
     */
    public Set<KapuaId> getJobTargetIdSubset() {
        return jobTargetIdSubset;
    }

}
