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

import org.apache.camel.Exchange;
import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreCommunicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

/**
 * Data storage message listener
 *
 * @since 1.0
 */
@UriEndpoint(title = "Data storage message processor", syntax = "bean:dataMessageProcessor", scheme = "bean")
public class DataStorageMessageProcessor extends AbstractProcessor<CamelKapuaMessage<?>> {

    private static final Logger logger = LoggerFactory.getLogger(DataStorageMessageProcessor.class);
    private static final String METRIC_COMPONENT_NAME = "datastore";

    private final MessageStoreService messageStoreService = KapuaLocator.getInstance().getService(MessageStoreService.class);

    // queues counters
    private final Counter metricQueueCommunicationErrorCount;
    private final Counter metricQueueConfigurationErrorCount;
    private final Counter metricQueueGenericErrorCount;

    public DataStorageMessageProcessor() {
        super("DataStorage");
        MetricsService metricService = MetricServiceFactory.getInstance();
        metricQueueCommunicationErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "queue", "communication", "error", "count");
        metricQueueConfigurationErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "queue", "configuration", "error", "count");
        metricQueueGenericErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "queue", "generic", "error", "count");
    }

    /**
     * Process a data message.
     * 
     * @throws KapuaException
     */
    public void processMessage(CamelKapuaMessage<?> message) throws KapuaException {
        // data messages
        logger.debug("Received data message from device channel: client id '{}' - {}", message.getMessage().getClientId(), message.getMessage().getChannel());
        try {
            messageStoreService.store(message.getMessage(), message.getDatastoreId());
        } catch (DatastoreCommunicationException e) {
            message.setDatastoreId(e.getUuid());
            throw e;
        }
    }

    public void processCommunicationErrorMessage(Exchange exchange, CamelKapuaMessage<?> message) throws KapuaException {
        logger.info("Message datastoreId: '{}' - Message Id: '{}'", new Object[] { message.getDatastoreId(), message.getMessage().getId() });
        processMessage(message);
        metricQueueCommunicationErrorCount.dec();
    }

    public void processConfigurationErrorMessage(Exchange exchange, CamelKapuaMessage<?> message) throws KapuaException {
        logger.info("Message datastoreId: '{}' - Message Id '{}'", new Object[] { message.getDatastoreId(), message.getMessage().getId() });
        metricQueueConfigurationErrorCount.dec();
    }

    public void processGenericErrorMessage(Exchange exchange, CamelKapuaMessage<?> message) throws KapuaException {
        logger.info("Message datastoreId: '{}' - Message Id '{}'", new Object[] { message.getDatastoreId(), message.getMessage().getId() });
        metricQueueGenericErrorCount.dec();
    }

}
