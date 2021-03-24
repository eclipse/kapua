/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.consumer.telemetry.listener;

import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.message.CamelKapuaMessage;
import org.eclipse.kapua.consumer.commons.listener.AbstractProcessor;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreCommunicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data storage message listener
 *
 * @since 1.0
 */
@UriEndpoint(title = "Data storage message processor", syntax = "bean:dataMessageProcessor", scheme = "bean")
public class DataStorageMessageProcessor extends AbstractProcessor<CamelKapuaMessage<?>> {

    private static final Logger LOG = LoggerFactory.getLogger(DataStorageMessageProcessor.class);

    private final MessageStoreService messageStoreService = KapuaLocator.getInstance().getService(MessageStoreService.class);

    public DataStorageMessageProcessor() {
        super("DataStorage");
    }

    /**
     * Process a data message.
     *
     * @throws KapuaException
     */
    @Override
    public void processMessage(CamelKapuaMessage<?> message) throws KapuaException {
        // data messages
        LOG.debug("Received data message from device channel: client id '{}' - {}", message.getMessage().getClientId(), message.getMessage().getChannel());
        try {
            messageStoreService.store(message.getMessage(), message.getDatastoreId());
        } catch (DatastoreCommunicationException e) {
            message.setDatastoreId(e.getUuid());
            throw e;
        }
    }

}
