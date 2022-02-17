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
package org.eclipse.kapua.service.job.targets;

/**
 * The possible statues for the {@link JobTarget}
 * <p>
 * Initial status will be {@link #PROCESS_AWAITING} and then is up to the {@link org.eclipse.kapua.service.job.Job} processing
 * manage the {@link JobTarget}'s {@link JobTargetStatus} according to the processing results.
 *
 * @since 1.0.0
 */
public enum JobTargetStatus {
    /**
     * The processing for the current {@link JobTarget#getStepIndex()} has completed.
     *
     * @since 1.0.0
     */
    PROCESS_OK,

    /**
     * The processing for the current {@link JobTarget#getStepIndex()} has failed.
     *
     * @since 1.0.0
     */
    PROCESS_FAILED,

    /**
     * The processing for the current {@link JobTarget#getStepIndex()} is waiting the processing.
     *
     * @since 1.0.0
     */
    PROCESS_AWAITING,

    /**
     * The processing for the current {@link JobTarget#getStepIndex()} is waiting the {@link JobTarget} to complete the processing
     *
     * @since 1.1.0
     */
    AWAITING_COMPLETION,

    /**
     * The processing for the current {@link JobTarget#getStepIndex()} has been notified the completion by {@link JobTarget}.
     *
     * @since 1.1.0
     */
    NOTIFIED_COMPLETION,
}
