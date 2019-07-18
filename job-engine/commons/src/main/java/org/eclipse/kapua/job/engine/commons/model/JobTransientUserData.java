/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.commons.model;

import org.eclipse.kapua.job.engine.commons.logger.JobLogger;

import javax.batch.runtime.context.JobContext;

/**
 * {@link JobTransientUserData} offers utilities over the {@link JobContext#getTransientUserData()}.
 * <p>
 * Using this class in the {@link JobContext#getTransientUserData()} ease the access to the stored data.
 *
 * @since 1.1.0
 */
public class JobTransientUserData {

    private JobLogger jobLogger;

    /**
     * Gets the {@link JobLogger}.
     *
     * @return The {@link JobLogger}.
     * @since 1.1.0
     */
    public JobLogger getJobLogger() {
        return jobLogger;
    }

    /**
     * Sets the {@link JobLogger}.
     *
     * @param jobLogger The {@link JobLogger}.
     * @since 1.1.0
     */
    public void setJobLogger(JobLogger jobLogger) {
        this.jobLogger = jobLogger;
    }
}
