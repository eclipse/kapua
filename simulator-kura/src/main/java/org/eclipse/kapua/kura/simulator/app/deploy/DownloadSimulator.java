/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.deploy;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DownloadSimulator implements AutoCloseable {

    private final ScheduledExecutorService executor;
    private Job job;
    private final long bytesPerSecond;

    public static enum JobState {
        RUNNING, COMPLETED, CANCELED, FAILED;
    }

    private class Job {

        private final long totalBytes;
        private long currentBytes;
        private final ScheduledFuture<?> future;
        private JobState state = JobState.RUNNING;
        private final long jobId;
        private final Consumer<DownloadState> consumer;
        private final Runnable whenCompleted;

        public Job(final ScheduledFuture<?> future, final long totalBytes, final long jobId,
                final Consumer<DownloadState> consumer, final Runnable whenCompleted) {
            this.future = future;
            this.totalBytes = totalBytes;
            this.jobId = jobId;
            this.consumer = consumer;
            this.whenCompleted = whenCompleted;
        }

        public void tick() {
            if (state != JobState.RUNNING) {
                return;
            }

            currentBytes += bytesPerSecond;
            if (currentBytes >= totalBytes) {
                currentBytes = totalBytes;
                future.cancel(false);
                state = JobState.COMPLETED;
                if (whenCompleted != null) {
                    // run outside of sync lock
                    executor.execute(whenCompleted);
                }
            }
        }

        public void cancel() {
            future.cancel(false);
            state = JobState.CANCELED;
        }

        public DownloadState getState() {
            switch (state) {
            case RUNNING:
                return new DownloadState((int) totalBytes, (int) (getCompletion() * 100.0), "IN_PROGRESS",
                        jobId);
            case CANCELED:
                return new DownloadState(0, 0, "CANCELLED", jobId);
            case COMPLETED:
                // Kura actually sends a size of zero in this case
                return new DownloadState(0, 100, "COMPLETED", jobId);
            case FAILED:
                return new DownloadState(0, 0, "FAILED", jobId);
            }
            return DownloadState.DONE;
        }

        private double getCompletion() {
            return (double) currentBytes / (double) totalBytes;
        }
    }

    public DownloadSimulator(final ScheduledExecutorService executor, final long bytesPerSecond) {
        this.executor = executor;
        this.bytesPerSecond = bytesPerSecond;
    }

    public synchronized boolean startDownload(final long jobId, final long totalBytes,
            final Consumer<DownloadState> consumer, final Runnable whenCompleted) {

        if (job != null) {
            // only one job can run at a time
            return false;
        }

        job = new Job(executor.scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS), totalBytes, jobId,
                consumer, whenCompleted);

        return true;
    }

    public boolean cancelDownload() {
        DownloadState state;
        Job job;

        synchronized (this) {
            if (this.job == null) {
                return false;
            }

            this.job.cancel();
            state = this.job.getState();
            job = this.job;
            this.job = null;
        }

        if (job.consumer != null) {
            job.consumer.accept(state);
        }

        return true;
    }

    protected void tick() {
        final DownloadState state;
        synchronized (this) {
            if (job == null) {
                // this should never happen ;)
                return;
            }

            job.tick();

            state = job.getState();
        }

        if (job.consumer != null) {
            job.consumer.accept(state);
        }
    }

    @Override
    public synchronized void close() {
        // we don't stop the executor ... it was not created by us

        if (job != null) {
            job.cancel();
            job = null;
        }
    }

    public synchronized DownloadState getState() {
        if (job == null) {
            return DownloadState.DONE;
        }

        return job.getState();
    }
}
