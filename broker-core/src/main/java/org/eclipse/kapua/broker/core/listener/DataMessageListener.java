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

import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

/**
 * Data messages listener.
 *
 * @since 1.0
 */
@UriEndpoint(title = "data message processor", syntax = "bean:dataMessageListener", scheme = "bean")
public class DataMessageListener extends AbstractListener {

    private static final Logger logger = LoggerFactory.getLogger(DataMessageListener.class);

    // metrics
    private Counter metricDataMessage;

    public DataMessageListener() {
        super("deviceLifeCycle");
        metricDataMessage = registerCounter("messages", "data", "count");
    }

    /**
     * Process a data message.
     * 
     * @param dataMessage
     */
    public void processDataMessage(CamelKapuaMessage<KapuaDataMessage> dataMessage) {
        // TODO to be implemented
        logger.info("Received data message from device channel: client id '{}' - {}", dataMessage.getMessage().getClientId(), dataMessage.getMessage().getChannel());

        metricDataMessage.inc();
    }

}
