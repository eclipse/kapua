/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.commons.wrappers;

import org.eclipse.kapua.service.job.targets.JobTarget;

/**
 * {@link JobTargetWrapper} wraps the {@link JobTarget} and offers utility methods around it.
 *
 * @since 1.0.0
 */
public class JobTargetWrapper {

    private JobTarget jobTarget;

    private Exception processingException;

    public JobTargetWrapper(JobTarget jobTarget) {
        this.jobTarget = jobTarget;
    }

    public JobTarget getJobTarget() {
        return jobTarget;
    }

    public void setJobTarget(JobTarget jobTarget) {
        this.jobTarget = jobTarget;
    }

    public Exception getProcessingException() {
        return processingException;
    }

    public void setProcessingException(Exception processingException) {
        this.processingException = processingException;
    }
}
