/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.queue;

/**
 * The status of the {@link QueuedJobExecution}
 *
 * @since 1.1.0
 */
public enum QueuedJobExecutionStatus {

    /**
     * The {@link QueuedJobExecution} has been enqueued and it is waiting to be resumed.
     * <p>
     * This is the initial status in which a newly created {@link QueuedJobExecution} should be in.
     *
     * @since 1.1.0
     */
    QUEUED,

    /**
     * The {@link QueuedJobExecution} has been resumed and fired.
     *
     * @since 1.1.0
     */
    PROCESSED,

    /**
     * The {@link QueuedJobExecution} has failed to resume.
     *
     * @since 1.2.0
     */
    FAILED_TO_RESUME
}
