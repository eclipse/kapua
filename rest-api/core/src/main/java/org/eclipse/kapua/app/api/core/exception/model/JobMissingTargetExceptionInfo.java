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

import org.eclipse.kapua.job.engine.exception.JobEngineErrorCodes;
import org.eclipse.kapua.job.engine.exception.JobMissingTargetException;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "jobMissingTargetExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobMissingTargetExceptionInfo extends JobScopedEngineExceptionInfo {

    public JobMissingTargetExceptionInfo() {
        this(null);
    }

    public JobMissingTargetExceptionInfo(JobMissingTargetException jobMissingTargetException) {
        super(Status.INTERNAL_SERVER_ERROR, JobEngineErrorCodes.JOB_TARGET_MISSING, jobMissingTargetException);
    }

}
