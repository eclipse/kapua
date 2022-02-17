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
package org.eclipse.kapua.job.engine.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Job Engine error codes
 * <p>
 *
 * @since 1.0.0
 */
public enum KapuaJobEngineErrorCodes implements KapuaErrorCode {

    /**
     * {@link org.eclipse.kapua.service.job.step.JobStep} missing
     *
     * @since 1.0.0
     */
    JOB_STEP_MISSING,

    /**
     * {@link org.eclipse.kapua.service.job.targets.JobTarget} missing
     *
     * @since 1.0.0
     */
    JOB_TARGET_MISSING,

    /**
     * The provided {@link java.util.List} of {@link org.eclipse.kapua.service.job.targets.JobTarget} ids contains one or more id that is not present in the list of available
     * {@link org.eclipse.kapua.service.job.targets.JobTarget}s fot this {@link org.eclipse.kapua.service.job.Job}
     *
     * @since 1.0.0
     */
    JOB_TARGET_INVALID,

    /**
     * The {@link org.eclipse.kapua.service.job.Job} requested to start has another execution running
     *
     * @since 1.0.0
     */
    JOB_ALREADY_RUNNING,

    /**
     * The {@link org.eclipse.kapua.service.job.execution.JobExecution} requested to start cannot be started now but ahs been enqueued.
     *
     * @since 1.1.0
     */
    JOB_EXECUTION_ENQUEUED,

    /**
     * The {@link org.eclipse.kapua.service.job.Job} start has thrown an error.
     *
     * @since 1.0.0
     */
    JOB_STARTING,

    /**
     * The {@link org.eclipse.kapua.service.job.Job} resume has thrown an error.
     *
     * @since 1.0.0
     */
    JOB_RESUMING,

    /**
     * The {@link org.eclipse.kapua.service.job.Job} is currently running
     *
     * @since 1.0.0
     */
    JOB_RUNNING,

    /**
     * Checking the status of the {@link org.eclipse.kapua.service.job.Job} has thrown an error
     *
     * @since 1.0.0
     */
    JOB_CHECK_RUNNING,

    /**
     * The {@link org.eclipse.kapua.service.job.Job} stop has thrown an error.
     *
     * @since 1.0.0
     */
    JOB_STOPPING,

    /**
     * The {@link org.eclipse.kapua.service.job.Job} is not currently running
     *
     * @since 1.0.0
     */
    JOB_NOT_RUNNING,

    /**
     * Data related to the {@link org.eclipse.kapua.service.job.Job} could not be cleaned
     *
     * @since 1.0.0
     */
    CANNOT_CLEANUP_JOB_DATA,
}
