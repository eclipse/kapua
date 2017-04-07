/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *
 *******************************************************************************/
package org.eclipse.kapua.service;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;

/**
 * Running broker in separate thread.
 */
public class MQBrokerRunner implements Runnable {

    /**
     * Second in milliseconcs.
     */
    public static final int SECOND_MILLIS = 1_000;

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(MQBrokerRunner.class);

    /**
     * Max time to wait for broker to be started, but not necessary ready
     * to accept messages.
     */
    private long maxWaitTime;

    /**
     * Broker configuration URI. Usually xml configuration file.
     */
    private URI brokerUri;

    /**
     * Instance of running broker.
     */
    private BrokerService brokerInstance;

    /**
     * Creation of broker startup thread.
     *
     * @param maxWaitTime max time to wait for broker to start
     * @param brokerUri   location of broker configuration file
     * @throws URISyntaxException
     */
    public MQBrokerRunner(long maxWaitTime, String brokerUri) throws URISyntaxException {
        this.maxWaitTime = maxWaitTime;
        this.brokerUri = new URI(brokerUri);
    }

    /**
     * Stop current instance of broker that was started in this thread.
     *
     * @throws Exception
     */
    public synchronized void stopBroker() throws Exception {
        if (brokerInstance != null) {
            if (brokerInstance.isStarted()) {
                brokerInstance.stop();
            }
        }
    }

    /**
     * Implemetation of broker startup in separate thread.
     */
    @Override
    public void run() {
        final Instant endOfWait = Instant.now().plusMillis(maxWaitTime);

        try {
            brokerInstance = BrokerFactory.createBroker(brokerUri);
            brokerInstance.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            if (brokerInstance.isStarted()) {
                logger.debug("Broker started.");
                break;
            }
            if (Instant.now().isAfter(endOfWait)) {
                throw new IllegalStateException(String.format("Broker didn't start in %s seconds.",
                        Duration.ofMillis(maxWaitTime).getSeconds()));
            }
            logger.debug("Waiting Broker.");
            try {
                Thread.sleep(SECOND_MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
