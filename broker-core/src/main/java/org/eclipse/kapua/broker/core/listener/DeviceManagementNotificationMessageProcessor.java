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

import com.codahale.metrics.Counter;
import com.google.common.base.Objects;
import org.apache.camel.Exchange;
import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyMessage;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyPayload;
import org.eclipse.kapua.service.device.management.registry.manager.DeviceManagementRegistryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Device management notification listener
 *
 * @since 1.0
 */
@UriEndpoint(title = "Device management notification storage message processor", syntax = "bean:deviceManagementNotificationMessageProcessor", scheme = "bean")
public class DeviceManagementNotificationMessageProcessor extends AbstractProcessor<CamelKapuaMessage<?>> {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceManagementNotificationMessageProcessor.class);
    private static final String METRIC_COMPONENT_NAME = "deviceManagementRegistry";

    // queues counters
    private final Counter metricQueueCommunicationErrorCount;
    private final Counter metricQueueConfigurationErrorCount;
    private final Counter metricQueueGenericErrorCount;

    public DeviceManagementNotificationMessageProcessor() {
        super("Device Management Notify Processor");
        MetricsService metricService = MetricServiceFactory.getInstance();

        metricQueueCommunicationErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "deviceManagementNotification", "process", "queue", "communication", "error", "count");
        metricQueueConfigurationErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "deviceManagementNotification", "process", "queue", "configuration", "error", "count");
        metricQueueGenericErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "deviceManagementNotification", "process", "queue", "generic", "error", "count");
    }

    /**
     * Process a device management notification message.
     *
     * @throws KapuaException
     */
    @Override
    public void processMessage(CamelKapuaMessage<?> message) throws KapuaException {
        LOG.debug("Received notification message from device channel: client id '{}' - {}", message.getMessage().getClientId(), message.getMessage().getChannel());

        KapuaNotifyMessage notifyMessage = (KapuaNotifyMessage) message.getMessage();

        KapuaNotifyPayload notifyPayload = notifyMessage.getPayload();

        DeviceManagementRegistryManager.processOperationNotification(
                notifyMessage.getScopeId(),
                notifyPayload.getOperationId(),
                Objects.firstNonNull(notifyMessage.getSentOn(), notifyMessage.getReceivedOn()),
                notifyPayload.getStatus(),
                notifyPayload.getProgress());
    }

    public void processCommunicationErrorMessage(Exchange exchange, CamelKapuaMessage<?> message) throws KapuaException {
        LOG.info("Message datastoreId: '{}' - Message Id: '{}'", message.getDatastoreId(), message.getMessage().getId());
        processMessage(message);
        metricQueueCommunicationErrorCount.dec();
    }

    public void processConfigurationErrorMessage(Exchange exchange, CamelKapuaMessage<?> message) throws KapuaException {
        LOG.info("Message datastoreId: '{}' - Message Id '{}'", message.getDatastoreId(), message.getMessage().getId());
        metricQueueConfigurationErrorCount.dec();
    }

    public void processGenericErrorMessage(Exchange exchange, CamelKapuaMessage<?> message) throws KapuaException {
        LOG.info("Message datastoreId: '{}' - Message Id '{}'", message.getDatastoreId(), message.getMessage().getId());
        metricQueueGenericErrorCount.dec();
    }

}
