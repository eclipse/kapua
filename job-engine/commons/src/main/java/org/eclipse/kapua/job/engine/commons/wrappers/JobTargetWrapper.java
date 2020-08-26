/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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
