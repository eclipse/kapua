/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.connector.lifecycle;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

import java.util.Base64;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifeCycleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Device messages listener (device life cycle).<br>
 * Manage:<br>
 * - APPS/BIRTH/DC/LWT device messages<br>
 *
 * @since 1.0
 */
public class LifecycleListener {

    private static final Logger logger = LoggerFactory.getLogger(LifecycleListener.class);

    private static DeviceLifeCycleService deviceLifeCycleService = KapuaLocator.getInstance().getService(DeviceLifeCycleService.class);

    // metrics
    private String metricComponentName = "listener";
    private String name = "deviceLifeCycle";
    private static final MetricsService METRICS_SERVICE = MetricServiceFactory.getInstance();

    private Counter metricDeviceBirthMessage;
    private Counter metricDeviceDisconnectMessage;
    private Counter metricDeviceMissingMessage;
    private Counter metricDeviceAppsMessage;
    private Counter metricDeviceUnmatchedMessage;
    private Counter metricDeviceErrorMessage;

    protected LifecycleListener() {
        metricDeviceBirthMessage = registerCounter("messages", "birth", "count");
        metricDeviceDisconnectMessage = registerCounter("messages", "dc", "count");
        metricDeviceMissingMessage = registerCounter("messages", "missing", "count");
        metricDeviceAppsMessage = registerCounter("messages", "apps", "count");
        metricDeviceUnmatchedMessage = registerCounter("messages", "unmatched", "count");
        metricDeviceErrorMessage = registerCounter("messages", "error", "count");
    }

    protected Counter registerCounter(String... names) {
        return METRICS_SERVICE.getCounter(metricComponentName, name, names);
    }

    protected Timer registerTimer(String... names) {
        return METRICS_SERVICE.getTimer(metricComponentName, name, names);
    }

    /**
     * Process a birth message.
     *
     * @param message
     */
    public void processBirthMessage(MessageContext<TransportMessage> message) {
        try {
            KapuaSecurityUtils.doPrivileged(() -> {
                deviceLifeCycleService.birth(getConnectionId(message),
                    LifecycleConverter.getBirthMessage(message.getMessage()));
                metricDeviceBirthMessage.inc();
            });
        } catch (KapuaException e) {
            metricDeviceErrorMessage.inc();
            logger.error("Error while processing device birth life-cycle event", e);
        }
    }

    /**
     * Process a disconnect message.
     *
     * @param message
     */
    public void processDisconnectMessage(MessageContext<TransportMessage> message) {
        try {
            KapuaSecurityUtils.doPrivileged(() -> {
                deviceLifeCycleService.death(getConnectionId(message),
                    LifecycleConverter.getDisconnectMessage(message.getMessage()));
                metricDeviceDisconnectMessage.inc();
            });
        } catch (KapuaException e) {
            metricDeviceErrorMessage.inc();
            logger.error("Error while processing device disconnect life-cycle event", e);
        }
    }

    /**
     * Process an application message.
     *
     * @param message
     */
    public void processAppsMessage(MessageContext<TransportMessage> message) {
        try {
            KapuaSecurityUtils.doPrivileged(() -> {
                deviceLifeCycleService.applications(getConnectionId(message),
                    LifecycleConverter.getAppsMessage(message.getMessage()));
                metricDeviceAppsMessage.inc();
            });
        } catch (KapuaException e) {
            metricDeviceErrorMessage.inc();
            logger.error("Error while processing device apps life-cycle event", e);
        }
    }

    /**
     * Process a missing message.
     *
     * @param message
     */
    public void processMissingMessage(MessageContext<TransportMessage> message) {
        try {
            KapuaSecurityUtils.doPrivileged(() -> {
                deviceLifeCycleService.missing(getConnectionId(message),
                    LifecycleConverter.getMissingMessage(message.getMessage()));
                metricDeviceMissingMessage.inc();
            });
        } catch (KapuaException e) {
            metricDeviceErrorMessage.inc();
            logger.error("Error while processing device missing life-cycle event", e);
        }
    }

    /**
     * Process a unmatched message.
     *
     * @param message
     */
    public void processUnmatchedMessage(MessageContext<TransportMessage> message) {
        logger.info("Received unmatched message from device channel: {}", message.getMessage().getChannel());
        metricDeviceUnmatchedMessage.inc();
    }

    private KapuaId getConnectionId(MessageContext<TransportMessage> message) {
        return (KapuaId)SerializationUtils.deserialize(Base64.getDecoder().decode((String)message.getProperties().get(Properties.CONNECTION_ID)));
    }
}
