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
package org.eclipse.kapua.service.device.management.packages.broker;

import com.codahale.metrics.Counter;
import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.listener.AbstractListener;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.lifecycle.KapuaNotifyChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaNotifyMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaNotifyPayload;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageAppProperties;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationProperty;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.management.registry.operation.OperationStatus;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationPredicates;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationRegistryFactory;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationRegistryService;
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

    private static KapuaLocator kapuaLocator = KapuaLocator.getInstance();
    private static DeviceManagementOperationRegistryService deviceManagementOperationRegistryService = kapuaLocator.getService(DeviceManagementOperationRegistryService.class);
    private static DeviceManagementOperationRegistryFactory deviceManagementOperationRegistryFactory = kapuaLocator.getFactory(DeviceManagementOperationRegistryFactory.class);
    private static DeviceManagementOperationNotificationRegistryService deviceManagementOperationNotificationRegistryService = kapuaLocator
            .getService(DeviceManagementOperationNotificationRegistryService.class);
    private static DeviceManagementOperationNotificationRegistryFactory deviceManagementOperationNotificationRegistryFactory = kapuaLocator
            .getFactory(DeviceManagementOperationNotificationRegistryFactory.class);

    // metrics
    private Counter metricDeviceNotifyMessage;
    private Counter metricDeviceNotifyError;

    public DevicePackageManagementNotifyMessageListener() {
        super("deviceManagementPackageNotification");
        metricDeviceNotifyMessage = registerCounter("messages", "notify", "packages", "count");
        metricDeviceNotifyError = registerCounter("messages", "notify", "packages", "error", "count");
    }

    /**
     * Process a notify message.
     *
     * @param notifyMessage
     */
    public void processNotifyMessage(CamelKapuaMessage<KapuaNotifyMessage> notifyMessage) {
        logger.info("Received notify message from device channel: {}",
                new Object[] { notifyMessage.getMessage().getChannel().toString() });

        KapuaNotifyMessage kapuaNotifyMessage = notifyMessage.getMessage();
        KapuaNotifyPayload kapuaNotifyPayload = kapuaNotifyMessage.getPayload();
        KapuaNotifyChannel kapuaNotifyChannel = kapuaNotifyMessage.getChannel();

        try {
            KapuaSecurityUtils.doPrivileged(() -> {
                if (!OperationStatus.RUNNING.name().equals(kapuaNotifyPayload.getOperationStatus())) {
                    KapuaId operationId = kapuaNotifyMessage.getPayload().getOperationId();

                    DeviceManagementOperation deviceMngmtOperation = deviceManagementOperationRegistryService.find(kapuaNotifyMessage.getScopeId(), operationId);

                    boolean isInstall = false;
                    for (DeviceManagementOperationProperty ip : deviceMngmtOperation.getInputProperties()) {
                        if (ip.getName().equals(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL.getValue())) {
                            isInstall = Boolean.valueOf(ip.getPropertyValue());
                            break;
                        }
                    }

                    if (kapuaNotifyChannel.getResources().equals("download") && !isInstall) {
                        operationEnded(deviceMngmtOperation, kapuaNotifyMessage);
                    } else if (kapuaNotifyChannel.getResources().equals("install") && isInstall) {
                        operationEnded(deviceMngmtOperation, kapuaNotifyMessage);
                    } else {
                        operationRunning(kapuaNotifyMessage);
                    }
                } else {
                    operationRunning(kapuaNotifyMessage);
                }
            });
        } catch (Exception e) {
            metricDeviceNotifyError.inc();
            logger.error("Error while processing device management package notification", e);
            return;
        }

        metricDeviceNotifyMessage.inc();
    }

    protected void operationEnded(DeviceManagementOperation deviceManagementOperation, KapuaNotifyMessage kapuaNotifyMessage) throws KapuaException {
        KapuaNotifyPayload kapuaNotifyPayload = kapuaNotifyMessage.getPayload();

        deviceManagementOperation.setOperationStatus(OperationStatus.valueOf(kapuaNotifyPayload.getOperationStatus()));
        deviceManagementOperationRegistryService.update(deviceManagementOperation);

        DeviceManagementOperationNotificationQuery notificationQuery = deviceManagementOperationNotificationRegistryFactory.newQuery(deviceManagementOperation.getScopeId());
        notificationQuery.setPredicate(new AttributePredicate<>(DeviceManagementOperationNotificationPredicates.OPERATION_ID, deviceManagementOperation.getId()));

        DeviceManagementOperationNotificationListResult notificationListResult = deviceManagementOperationNotificationRegistryService.query(notificationQuery);

        for (DeviceManagementOperationNotification deviceManagementOperationNotification : notificationListResult.getItems()) {
            deviceManagementOperationNotificationRegistryService.delete(deviceManagementOperationNotification.getScopeId(), deviceManagementOperationNotification.getId());
        }
    }

    protected void operationRunning(KapuaNotifyMessage kapuaNotifyMessage) throws KapuaException {
        KapuaNotifyPayload kapuaNotifyPayload = kapuaNotifyMessage.getPayload();

        DeviceManagementOperationNotificationCreator deviceManagementOperationNotificationCreator = deviceManagementOperationNotificationRegistryFactory.newCreator(kapuaNotifyMessage.getScopeId());
        deviceManagementOperationNotificationCreator.setOperationId(kapuaNotifyPayload.getOperationId());
        deviceManagementOperationNotificationCreator.setOperationStatus(OperationStatus.valueOf(kapuaNotifyPayload.getOperationStatus()));
        deviceManagementOperationNotificationCreator.setProgress(kapuaNotifyPayload.getOperationProgress());
        deviceManagementOperationNotificationCreator.setSentOn(kapuaNotifyMessage.getSentOn());

        deviceManagementOperationNotificationRegistryService.create(deviceManagementOperationNotificationCreator);
    }

}
