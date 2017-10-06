/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.listener;

import com.codahale.metrics.Counter;
import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaNotifyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Device messages listener (device package management notification messages).<br>
 * Manage:<br>
 * - NOTIFY device messages<br>
 *
 * @since 1.0
 */
@UriEndpoint(title = "device notification message processor", syntax = "bean:devicePackageManagementNotifyMessageListener", scheme = "bean")
public class DevicePackageManagementNotifyMessageListener extends AbstractListener {

    private static final Logger logger = LoggerFactory.getLogger(DevicePackageManagementNotifyMessageListener.class);

    // metrics
    private Counter metricDeviceNotifyMessage;

    public DevicePackageManagementNotifyMessageListener() {
        super("deviceManagementPackageNotification");
        metricDeviceNotifyMessage = registerCounter("messages", "notify", "packages", "count");
    }

    /**
     * Process a notify message.
     *
     * @param notifyMessage
     */
    public void processNotifyMessage(CamelKapuaMessage<KapuaNotifyMessage> notifyMessage) {
        logger.info("Received notify message from device channel: {}",
                new Object[] { notifyMessage.getMessage().getChannel().toString() });
        metricDeviceNotifyMessage.inc();
    }

}
