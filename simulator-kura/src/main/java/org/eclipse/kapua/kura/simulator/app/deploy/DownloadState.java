/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.deploy;

public class DownloadState {

    public static final DownloadState DONE = new DownloadState(0, 100, "ALREADY_DONE", null);

    private final int transferSize;
    private final int transferProgress;
    private final String status;
    private final Long jobId;

    /**
     * Create a new download state instance
     *
     * @param transferSize
     *            the amount of bytes to transfer
     * @param transferProgress
     *            the state of the progress (0 to 100)
     * @param status
     *            the status value (IN_PROGRESS, COMPLETED, CANCELLED, ...)
     * @param jobId
     *            the job jd
     */
    public DownloadState(final int transferSize, final int transferProgress, final String status, final Long jobId) {
        this.transferSize = transferSize;
        this.transferProgress = transferProgress;
        this.status = status;
        this.jobId = jobId;
    }

    public int getTransferSize() {
        return transferSize;
    }

    public int getTransferProgress() {
        return transferProgress;
    }

    public String getStatus() {
        return status;
    }

    public Long getJobId() {
        return jobId;
    }

    @Override
    public String toString() {
        return String.format("[DownloadState - jobId: %s, transferSize: %s, transferProgress: %s%%, status: %s",
                jobId, transferSize, transferProgress, status);
    }
}