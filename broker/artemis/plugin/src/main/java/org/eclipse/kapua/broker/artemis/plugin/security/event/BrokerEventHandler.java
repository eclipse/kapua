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
package org.eclipse.kapua.broker.artemis.plugin.security.event;

import org.eclipse.kapua.commons.localevent.EventHandler;
import org.eclipse.kapua.commons.localevent.EventProcessor;
import org.eclipse.kapua.commons.localevent.ExecutorWrapper;
import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class BrokerEventHandler {
    private static Logger logger = LoggerFactory.getLogger(EventHandler.class);
    private final CommonsMetric commonsMetric;

    private boolean running;
    private ExecutorWrapper executorWrapper;
    private static final int MAX_ONGOING_OPERATION = 10;
    private static final int POLL_TIMEOUT = 10000;

    private BlockingQueue<BrokerEvent> eventQueue = new LinkedBlockingDeque<>(MAX_ONGOING_OPERATION);
    private EventProcessor<BrokerEvent> eventProcessor;

    @Inject
    public BrokerEventHandler(CommonsMetric commonsMetric) {
        this.commonsMetric = commonsMetric;
        executorWrapper = new ExecutorWrapper(BrokerEventHandler.class.getName(), () -> {
            while (isRunning()) {
                try {
                    final BrokerEvent eventBean = eventQueue.poll(POLL_TIMEOUT, TimeUnit.MILLISECONDS);
                    if (eventBean != null) {
                        commonsMetric.getDequeuedEvent().inc();
                        eventProcessor.processEvent(eventBean);
                        commonsMetric.getProcessedEvent().inc();
                    }
                } catch (InterruptedException e) {
                    //do nothing...
                    this.stop();
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    //do nothing
                    logger.error("Error while processing event: {}", e.getMessage(), e);
                    //TODO add metric?
                }
            }
        }, 10, TimeUnit.SECONDS);
    }

    public void enqueueEvent(BrokerEvent eventBean) {
        eventQueue.add(eventBean);
        commonsMetric.getEnqueuedEvent().inc();
    }

    public void registerConsumer(EventProcessor<BrokerEvent> eventProcessor) {
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
