/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Job Engine error codes
 * <p>
 * since 1.0
 */
public enum KapuaJobEngineErrorCodes implements KapuaErrorCode {

    /**
     * {@link org.eclipse.kapua.service.job.step.JobStep} missing
     */
    JOB_STEP_MISSING,
    /**
     * {@link org.eclipse.kapua.service.job.targets.JobTarget} missing
     */
    JOB_TARGET_MISSING,

    /**
     * The {@link org.eclipse.kapua.service.job.Job} requested to start has another execution running
     */
    JOB_ALREADY_RUNNING,

    /**
     * The {@link org.eclipse.kapua.service.job.Job} start has thrown an error.
     */
    JOB_STARTING,

    /**
     * Checking the status of the {@link org.eclipse.kapua.service.job.Job} has thrown an error
     */
    JOB_CHECK_RUNNING,

    /**
     * The {@link org.eclipse.kapua.service.job.Job} stop has thrown an error.
     */
    JOB_STOPPING,

    /**
     * The {@link org.eclipse.kapua.service.job.Job} is not currently running
     */
    JOB_NOT_RUNNING

}
