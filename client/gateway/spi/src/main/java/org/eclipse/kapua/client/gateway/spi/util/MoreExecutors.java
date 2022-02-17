/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
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
package org.eclipse.kapua.client.gateway.spi.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class MoreExecutors {

    private MoreExecutors() {
    }

    public static ScheduledExecutorService preventShutdown(ScheduledExecutorService executor) {
        Objects.requireNonNull(executor);
        return new PreventShutdownScheduledExecutorService(executor);
    }

    private static final class PreventShutdownScheduledExecutorService implements ScheduledExecutorService {

        private final ScheduledExecutorService executor;

        private PreventShutdownScheduledExecutorService(final ScheduledExecutorService executor) {
            this.executor = executor;
        }

        @Override
        public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
            return executor.schedule(command, delay, unit);
        }

        @Override
        public void execute(Runnable command) {
            executor.execute(command);
        }

        @Override
        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
            return executor.schedule(callable, delay, unit);
        }

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
            return executor.scheduleAtFixedRate(command, initialDelay, period, unit);
        }

        @Override
        public void shutdown() {
            throw new UnsupportedOperationException("This executor service can not be shut down");
        }

        @Override
        public List<Runnable> shutdownNow() {
            throw new UnsupportedOperationException("This executor service can not be shut down");
        }

        @Override
        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
            return executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
        }

        @Override
        public boolean isShutdown() {
            return executor.isShutdown();
        }

        @Override
        public boolean isTerminated() {
            return executor.isTerminated();
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return executor.awaitTermination(timeout, unit);
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return executor.submit(task);
        }

        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            return executor.submit(task, result);
        }

        @Override
        public Future<?> submit(Runnable task) {
            return executor.submit(task);
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            return executor.invokeAll(tasks);
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            return executor.invokeAll(tasks, timeout, unit);
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            return executor.invokeAny(tasks);
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return executor.invokeAny(tasks, timeout, unit);
        }

    }

}
