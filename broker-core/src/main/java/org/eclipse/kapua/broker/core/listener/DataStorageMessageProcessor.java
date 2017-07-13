/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.broker.core.listener;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;

/**
 * Data storage message listener
 *
 * @since 1.0
 */
@UriEndpoint(title = "Data storage message processor", syntax = "bean:dataStorageMessageProcessor", scheme = "bean")
public class DataStorageMessageProcessor extends AbstractProcessor<CamelKapuaMessage<?>> {

    private static final Logger logger = LoggerFactory.getLogger(DataStorageMessageProcessor.class);

    // metrics
    private final Counter metricStorageMessage;
    // data message
    private final Counter metricStorageDataErrorMessage;
    // store timers
    private final Timer metricStorageDataSaveTime;

    private final static AtomicInteger ERROR_COUNT = new AtomicInteger();
    private final static AtomicInteger COUNT = new AtomicInteger();
    private final static AtomicInteger PROCESSED_COUNT = new AtomicInteger();

    private final MessageStoreService messageStoreService = KapuaLocator.getInstance().getService(MessageStoreService.class);

    public DataStorageMessageProcessor() {
        super("DataStorage");

        // data message
        metricStorageMessage = registerCounter("listener", "storage", "messages", "count");
        metricStorageDataErrorMessage = registerCounter("listener", "storage", "messages", "data", "error", "count");
        // store timers
        metricStorageDataSaveTime = registerTimer("listener", "storage", "store", "data", "time", "s");
    }

    /**
     * Process a data message.
     * 
     * @throws KapuaException
     */
    @Override
    public void processMessage(CamelKapuaMessage<?> message) throws KapuaException {

        // TODO filter alert topic???
        //
        PROCESSED_COUNT.incrementAndGet();
        // data messages
        try {
            Context metricStorageDataSaveTimeContext = metricStorageDataSaveTime.time();
            logger.debug("Received data message from device channel: client id '{}' - {}", message.getMessage().getClientId(), message.getMessage().getChannel());
            messageStoreService.store(message.getMessage());
            COUNT.incrementAndGet();
            metricStorageMessage.inc();
            metricStorageDataSaveTimeContext.stop();
        } catch (KapuaException e) {
            ERROR_COUNT.incrementAndGet();
            metricStorageDataErrorMessage.inc();
            logger.error("An error occurred while storing message", e);
            throw e;
        }
    }

}
