/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
 * {@link KapuaErrorCode} implementation for {@link JobEngineException}
 *
 * @since 1.0.0
 */
public enum JobEngineErrorCodes implements KapuaErrorCode {

    /**
     * See {@link JobMissingStepException}.
     *
     * @since 1.0.0
     */
    JOB_STEP_MISSING,

    /**
     * See {@link JobMissingTargetException}
     *
     * @since 1.0.0
     */
    JOB_TARGET_MISSING,

    /**
     * See {@link JobInvalidTargetException}.
     *
     * @since 1.0.0
     */
    JOB_TARGET_INVALID,

    /**
     * See {@link JobAlreadyRunningException}.
     *
     * @since 1.0.0
     */
    JOB_ALREADY_RUNNING,

    /**
     * See {@link JobStartingException}.
     *
     * @since 1.0.0
     */
    JOB_STARTING,

    /**
     * See {@link JobResumingException}.
     *
     * @since 1.0.0
     */
    JOB_RESUMING,

    /**
     * See {@link JobRunningException}.
     *
     * @since 1.0.0
     */
    JOB_RUNNING,

    /**
     * See {@link JobCheckRunningException}.
     *
     * @since 1.0.0
     */
    JOB_CHECK_RUNNING,

    /**
     * See {@link JobStoppingException}.
     *
     * @since 1.0.0
     */
    JOB_STOPPING,

    /**
     * See {@link JobStoppingException}.
     *
     * @since 1.0.0
     */
    JOB_EXECUTION_STOPPING,

    /**
     * See {@link JobNotRunningException}.
     *
     * @since 1.0.0
     */
    JOB_NOT_RUNNING,

    /**
     * See {@link CleanJobDataException}.
     *
     * @since 1.0.0
     */
    CANNOT_CLEANUP_JOB_DATA,
}
