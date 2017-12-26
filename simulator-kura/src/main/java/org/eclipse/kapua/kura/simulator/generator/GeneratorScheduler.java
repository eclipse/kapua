/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.generator;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.eclipse.scada.utils.concurrent.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A scheduler calling all tasks with a fixed timestamp per tick
 * <p>
 * All tasks added to this scheduler will get called at the same period. For each period all tasks will receive the same timestamp as input.
 * </p>
 */
public class GeneratorScheduler implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(GeneratorScheduler.class);

    public interface Handle {

        public void remove();
    }

    private static class Entry {

        private final Consumer<Instant> task;

        public Entry(final Consumer<Instant> task) {
            this.task = task;
        }

        public void accept(final Instant timestamp) {
            this.task.accept(timestamp);
        }
    }

    private final ScheduledFuture<?> job;

    private final Set<Entry> tasks = new CopyOnWriteArraySet<>();

    private final ScheduledExecutorService executorService;

    public GeneratorScheduler(final ScheduledExecutorService executorService, final Duration period) {
        this.executorService = null;
        this.job = executorService.scheduleAtFixedRate(this::tick, 0, period.toMillis(), TimeUnit.MILLISECONDS);
    }

    public GeneratorScheduler(final Duration period) {
        this.executorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("GeneratorScheduler"));
        this.job = this.executorService.scheduleAtFixedRate(this::tick, 0, period.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() throws Exception {
        if (this.job != null) {
            this.job.cancel(false);
        }
        if (this.executorService != null) {
            this.executorService.shutdown();
        }
    }

    protected void tick() {
        // Run all tasks with the same timestamp
        final Instant timestamp = Instant.now();

        for (final Entry task : this.tasks) {
            // If a task fails here, we still process others
            task.accept(timestamp);
        }
    }

    /**
     * Add a new task to the scheduler
     * <p>
     * Adding the same task multiple time will result in the task being processed multiple times per tick.
     * </p>
     *
     * @param task
     *            the task to add
     * @return a handle to remove the task
     */
    public Handle add(final Consumer<Instant> task) {

        // create unique entry
        final Entry entry = new Entry(task);

        // add it
        this.tasks.add(entry);

        // return remove operation
        return new Handle() {

            private final AtomicBoolean removed = new AtomicBoolean(false);

            @Override
            public void remove() {
                if (!this.removed.getAndSet(true)) {
                    // only remove once
                    GeneratorScheduler.this.tasks.remove(entry);
                }
            }
        };
    }

}
