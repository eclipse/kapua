/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
			if (this.state != JobState.RUNNING) {
				return;
			}

			this.currentBytes += DownloadSimulator.this.bytesPerSecond;
			if (this.currentBytes >= this.totalBytes) {
				this.currentBytes = this.totalBytes;
				this.future.cancel(false);
				this.state = JobState.COMPLETED;
				if (this.whenCompleted != null) {
					// run outside of sync lock
					DownloadSimulator.this.executor.execute(this.whenCompleted);
				}
			}
		}

		public void cancel() {
			this.future.cancel(false);
			this.state = JobState.CANCELED;
		}

		public DownloadState getState() {
			switch (this.state) {
			case RUNNING:
				return new DownloadState((int) this.totalBytes, (int) (getCompletion() * 100.0), "IN_PROGRESS",
						this.jobId);
			case CANCELED:
				return new DownloadState(0, 0, "CANCELLED", this.jobId);
			case COMPLETED:
				// Kura actually sends a size of zero in this case
				return new DownloadState(0, 100, "COMPLETED", this.jobId);
			case FAILED:
				return new DownloadState(0, 0, "FAILED", this.jobId);
			}
			return DownloadState.DONE;
		}

		private double getCompletion() {
			return (double) this.currentBytes / (double) this.totalBytes;
		}
	}

	public DownloadSimulator(final ScheduledExecutorService executor, final long bytesPerSecond) {
		this.executor = executor;
		this.bytesPerSecond = bytesPerSecond;
	}

	public synchronized boolean startDownload(final long jobId, final long totalBytes,
			final Consumer<DownloadState> consumer, final Runnable whenCompleted) {

		if (this.job != null) {
			// only one job can run at a time
			return false;
		}

		this.job = new Job(this.executor.scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS), totalBytes, jobId,
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
			if (this.job == null) {
				// this should never happen ;)
				return;
			}

			this.job.tick();

			state = this.job.getState();
		}

		if (this.job.consumer != null) {
			this.job.consumer.accept(state);
		}
	}

	@Override
	public synchronized void close() {
		// we don't stop the executor ... it was not created by us

		if (this.job != null) {
			this.job.cancel();
			this.job = null;
		}
	}

	public synchronized DownloadState getState() {
		if (this.job == null) {
			return DownloadState.DONE;
		}

		return this.job.getState();
	}
}
