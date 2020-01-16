/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
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
import com.google.common.base.MoreObjects;
import org.apache.camel.Exchange;
import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.job.manager.JobDeviceManagementOperationManagerService;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyChannel;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyMessage;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyPayload;
import org.eclipse.kapua.service.device.management.registry.manager.DeviceManagementRegistryManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link DeviceManagementNotificationMessageProcessor}
 *
 * @since 1.0.0
 */
@UriEndpoint(title = "Device management notification storage message processor", syntax = "bean:deviceManagementNotificationMessageProcessor", scheme = "bean")
public class DeviceManagementNotificationMessageProcessor extends AbstractProcessor<CamelKapuaMessage<?>> {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceManagementNotificationMessageProcessor.class);

    private static final DeviceManagementRegistryManagerService DEVICE_MANAGEMENT_REGISTRY_MANAGER_SERVICE = KapuaLocator.getInstance().getService(DeviceManagementRegistryManagerService.class);
    private static final JobDeviceManagementOperationManagerService JOB_DEVICE_MANAGEMENT_OPERATION_MANAGER_SERVICE = KapuaLocator.getInstance().getService(JobDeviceManagementOperationManagerService.class);

    private static final String METRIC_MODULE_NAME = "device_management_registry";
    private static final String METRIC_COMPONENT_NAME = "notification";
    private static final String METRIC_PROCESS_QUEUE = "process_queue";

    // queues counters
    private final Counter metricQueueCommunicationErrorCount;
    private final Counter metricQueueConfigurationErrorCount;
    private final Counter metricQueueGenericErrorCount;

    public DeviceManagementNotificationMessageProcessor() {
        super("Device Management Notify Processor");
        MetricsService metricService = MetricServiceFactory.getInstance();

        metricQueueCommunicationErrorCount = metricService.getCounter(METRIC_MODULE_NAME, METRIC_COMPONENT_NAME, METRIC_PROCESS_QUEUE, "communication", "error", "count");
        metricQueueConfigurationErrorCount = metricService.getCounter(METRIC_MODULE_NAME, METRIC_COMPONENT_NAME, METRIC_PROCESS_QUEUE, "configuration", "error", "count");
        metricQueueGenericErrorCount = metricService.getCounter(METRIC_MODULE_NAME, METRIC_COMPONENT_NAME, METRIC_PROCESS_QUEUE, "generic", "error", "count");
    }

    /**
     * Process a device management {@link KapuaNotifyMessage}.
     *
     * @throws KapuaException
     */
    @Override
    public void processMessage(CamelKapuaMessage<?> message) throws KapuaException {
        LOG.debug("Received notification message from device channel: client id '{}' - {}", message.getMessage().getClientId(), message.getMessage().getChannel());

        KapuaNotifyMessage notifyMessage = (KapuaNotifyMessage) message.getMessage();
        KapuaNotifyPayload notifyPayload = notifyMessage.getPayload();
        KapuaNotifyChannel notifyChannel = notifyMessage.getChannel();

        try {
            DEVICE_MANAGEMENT_REGISTRY_MANAGER_SERVICE.processOperationNotification(
                    notifyMessage.getScopeId(),
                    notifyPayload.getOperationId(),
                    MoreObjects.firstNonNull(notifyMessage.getSentOn(), notifyMessage.getReceivedOn()),
                    notifyPayload.getResource() != null ? notifyPayload.getResource() : notifyChannel.getResources()[0],
                    notifyPayload.getStatus(),
                    notifyPayload.getProgress(),
                    notifyPayload.getMessage());

            JOB_DEVICE_MANAGEMENT_OPERATION_MANAGER_SERVICE.processJobTargetOnNotification(
                    notifyMessage.getScopeId(),
                    notifyPayload.getOperationId(),
                    MoreObjects.firstNonNull(notifyMessage.getSentOn(), notifyMessage.getReceivedOn()),
                    notifyPayload.getResource() != null ? notifyPayload.getResource() : notifyChannel.getResources()[0],
                    notifyPayload.getStatus());
        } catch (Exception e) {
            LOG.error("Error while processing Device Management Operation Notification message!", e);
            throw e;
        }
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
