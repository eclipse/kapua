/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.localevent;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorWrapper {

    private static Logger logger = LoggerFactory.getLogger(ExecutorWrapper.class);

    private String name;
    private Runnable runnable;
    private long delay;
    private long period;
    private TimeUnit timeUnit;
    private Future<?> future;
    private ScheduledExecutorService executorService;

    public ExecutorWrapper(String name, Runnable runnable) {
        this(name, runnable, 0, -1, null);
   }

    public ExecutorWrapper(String name, Runnable runnable, long delay, TimeUnit timeUnit) {
        this(name, runnable, delay, -1, timeUnit);
    }

    public ExecutorWrapper(String name, Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        this.name = name;
        this.runnable = runnable;
        this.delay = delay;
        this.period = period;
        this.timeUnit = timeUnit;
    }

    public synchronized void start() {
        logger.info("Starting ExecutorWrapper...");
        if (executorService==null) {
            executorService = Executors.newScheduledThreadPool(1);
            if (period<0) {
                if (delay>0) {
                    logger.info("Submitting executor {} with delay {}...", name, delay);
                    future = executorService.schedule(runnable, delay, TimeUnit.SECONDS);
                    logger.info("Submitting executor {} with delay {}... DONE", name, delay);
                }
                else {
                    logger.info("Submitting executor {}...", name);
                    future = executorService.submit(runnable);
                    logger.info("Submitting executor {}... DONE", name);
                }
            }
            else {
                logger.info("Submiting scheduled executor {} (delay {} - period {} - time unit {})...", name, delay, period, timeUnit);
                future = executorService.scheduleAtFixedRate(runnable, delay, period, timeUnit);
                logger.info("Submiting scheduled executor {} (delay {} - period {} - time unit {})... DONE", name, delay, period, timeUnit);
            }
        }
        else {
            logger.info("Starting ExecutorWrapper... Executor already running. Please stop it before starting again.");
        }
        logger.info("Starting ExecutorWrapper... DONE");
    }

    public synchronized void stop() {
        logger.info("Stopping ExecutorWrapper...");
        if (future!=null) {
            future.cancel(true);
            executorService.shutdown();
            executorService = null;
        }
        else {
            logger.info("Stopping executor wrapper {}... Service was already stopped", runnable.getClass().getName());
        }
        logger.info("Stopping ExecutorWrapper... DONE");
    }
}
