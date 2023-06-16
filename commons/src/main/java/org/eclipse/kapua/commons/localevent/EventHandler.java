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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventHandler<O> {

    private static Logger logger = LoggerFactory.getLogger(EventHandler.class);

    private boolean running;
    private ExecutorWrapper executorWrapper;
    private static final int MAX_ONGOING_OPERATION = 10;
    private static final int POLL_TIMEOUT = 10000;

    private BlockingQueue<O> eventQueue = new LinkedBlockingDeque<>(MAX_ONGOING_OPERATION);
    private EventProcessor<O> eventProcessor;

    protected EventHandler(String name, long initialDelay, long pollTimeout) {
        executorWrapper = new ExecutorWrapper(name, () -> {
            while (isRunning()) {
                try {
                    O eventBean = eventQueue.poll(POLL_TIMEOUT, TimeUnit.MILLISECONDS);
                    if (eventBean!=null) {
                        CommonsMetric.getInstance().getDequeuedEvent().inc();
                        eventProcessor.processEvent(eventBean);
                        CommonsMetric.getInstance().getProcessedEvent().inc();
                    }
                } catch (InterruptedException e) {
                    //do nothing...
                    Thread.currentThread().interrupt();
                }
                catch (Exception e) {
                    //do nothing
                    logger.error("Error while processing event: {}", e.getMessage(), e);
                    //TODO add metric?
                }
            }
        }, initialDelay, TimeUnit.SECONDS);
    }

    public void enqueueEvent(O eventBean) {
        eventQueue.add(eventBean);
        CommonsMetric.getInstance().getEnqueuedEvent().inc();
    }

    public void registerConsumer(EventProcessor<O> eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    public void start() {
        running = true;
        executorWrapper.start();
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
