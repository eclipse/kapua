/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.consumer.lifecycle.listener;

import com.codahale.metrics.Counter;
import org.apache.camel.spi.UriEndpoint;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage;
import org.eclipse.kapua.service.camel.listener.AbstractListener;
import org.eclipse.kapua.service.camel.message.CamelKapuaMessage;
import org.eclipse.kapua.service.device.management.job.scheduler.manager.JobDeviceManagementTriggerManagerService;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifeCycleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Device messages listener (device life cycle).
 * <p>
 * Manage:<br>
 * - BIRTH/DC/LWT/APPS device messages<br>
 * Republish of the lifecycle messages (once processed by the broker) isn't supported yet (see #136).
 *
 * @since 1.0.0
 */
@UriEndpoint(title = "device message processor", syntax = "bean:deviceMessageListener", scheme = "bean")
public class DeviceMessageListener extends AbstractListener {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceMessageListener.class);

    private DeviceLifeCycleService deviceLifeCycleService;
    private JobDeviceManagementTriggerManagerService jobDeviceManagementTriggerManagerService;

    // metrics
    private Counter metricDeviceBirthMessage;
    private Counter metricDeviceDisconnectMessage;
    private Counter metricDeviceMissingMessage;
    private Counter metricDeviceAppsMessage;
    private Counter metricDeviceErrorMessage;

    public DeviceMessageListener() {
        super(DeviceManagementRegistryNotificationMetrics.METRIC_COMPONENT_DEVICE_LIFE_CYCLE);
        deviceLifeCycleService = KapuaLocator.getInstance().getService(DeviceLifeCycleService.class);
        jobDeviceManagementTriggerManagerService = KapuaLocator.getInstance().getService(JobDeviceManagementTriggerManagerService.class);
        metricDeviceBirthMessage = registerCounter(MetricsLabel.MESSAGES, MetricsLabel.MESSAGE_BIRTH, MetricsLabel.COUNT);
        metricDeviceDisconnectMessage = registerCounter(MetricsLabel.MESSAGES, MetricsLabel.MESSAGE_DC, MetricsLabel.COUNT);
        metricDeviceMissingMessage = registerCounter(MetricsLabel.MESSAGES, MetricsLabel.MESSAGE_MISSING, MetricsLabel.COUNT);
        metricDeviceAppsMessage = registerCounter(MetricsLabel.MESSAGES, MetricsLabel.MESSAGE_APPS, MetricsLabel.COUNT);
        metricDeviceErrorMessage = registerCounter(MetricsLabel.MESSAGES, MetricsLabel.ERROR, MetricsLabel.COUNT);
    }

    /**
     * Process a birth message.
     *
     * @param birthMessage The birth message to process.
     * @since 1.0.0
     */
    public void processBirthMessage(CamelKapuaMessage<KapuaBirthMessage> birthMessage) {
        try {
            deviceLifeCycleService.birth(birthMessage.getConnectionId(), birthMessage.getMessage());
            metricDeviceBirthMessage.inc();
        } catch (KapuaException e) {
            metricDeviceErrorMessage.inc();
            LOG.error("Error while processing device birth life-cycle event", e);
        }

        //TODO EXT-CAMEL only for test remove when jobs will be defined in their own container
        try {
            KapuaBirthMessage kapuaBirthMessage = birthMessage.getMessage();

            jobDeviceManagementTriggerManagerService.processOnConnect(kapuaBirthMessage.getScopeId(), kapuaBirthMessage.getDeviceId());
        } catch (Exception e) {
            LOG.error("Error while processing device birth to trigger jobs", e);
        }
    }

    /**
     * Process a disconnect message.
     *
     * @param disconnectMessage The disconnect message to process.
     * @since 1.0.0
     */
    public void processDisconnectMessage(CamelKapuaMessage<KapuaDisconnectMessage> disconnectMessage) {
        try {
            deviceLifeCycleService.death(disconnectMessage.getConnectionId(), disconnectMessage.getMessage());
            metricDeviceDisconnectMessage.inc();
        } catch (KapuaException e) {
            metricDeviceErrorMessage.inc();
            LOG.error("Error while processing device disconnect life-cycle event", e);
        }
    }

    /**
     * Process an application message.
     *
     * @param appsMessage The apps message to process.
     * @since 1.0.0
     */
    public void processAppsMessage(CamelKapuaMessage<KapuaAppsMessage> appsMessage) {
        try {
            deviceLifeCycleService.applications(appsMessage.getConnectionId(), appsMessage.getMessage());
            metricDeviceAppsMessage.inc();
        } catch (KapuaException e) {
            metricDeviceErrorMessage.inc();
            LOG.error("Error while processing device apps life-cycle event", e);
        }
    }

    /**
     * Process a missing message.
     *
     * @param missingMessage The missing message to process.
     * @since 1.0.0
     */
    public void processMissingMessage(CamelKapuaMessage<KapuaMissingMessage> missingMessage) {
        try {
            deviceLifeCycleService.missing(missingMessage.getConnectionId(), missingMessage.getMessage());
            metricDeviceMissingMessage.inc();
        } catch (KapuaException e) {
            metricDeviceErrorMessage.inc();
            LOG.error("Error while processing device missing life-cycle event", e);
        }
    }
}
