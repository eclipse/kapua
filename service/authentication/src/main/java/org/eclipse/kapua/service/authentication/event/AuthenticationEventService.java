/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.event;

import java.util.Date;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.event.ListenServiceEvent;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

@KapuaProvider
public class AuthenticationEventService implements KapuaService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEventService.class);

    protected String metricComponentName = "listener";
    protected MetricsService metricsService = MetricServiceFactory.getInstance();

    private DeviceEventService deviceEventService;
    private DeviceEventFactory deviceEventFactory;
    private DeviceConnectionService deviceConnectionService;
    private DeviceRegistryService deviceRegistryService;

    private final Counter metricLifecycleOkMessage;
    private final Counter metricLifecycleErrorMessage;

    public AuthenticationEventService() {
        deviceEventService = KapuaLocator.getInstance().getService(DeviceEventService.class);
        deviceEventFactory = KapuaLocator.getInstance().getFactory(DeviceEventFactory.class);
        deviceConnectionService = KapuaLocator.getInstance().getService(DeviceConnectionService.class);
        deviceRegistryService = KapuaLocator.getInstance().getService(DeviceRegistryService.class);
        metricLifecycleOkMessage = metricsService.getCounter(metricComponentName, "ec", "kapua_message", "messages", "lifecycle", "ok", "count");
        metricLifecycleErrorMessage = metricsService.getCounter(metricComponentName, "ec", "kapua_message", "messages", "lifecycle", "error", "count");
    }

    @ListenServiceEvent(fromAddress = "lifecycleEvent")//TODO get from configuration
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        logger.info("Received lifecycle event for client {} - event type: {} - scopeId: {} - userId: {}",
            kapuaEvent.getInputs(), kapuaEvent.getOperation(), kapuaEvent.getScopeId(), kapuaEvent.getUserId());
        try {
            // Add event
            Date queuedOn = kapuaEvent.getTimestamp();
            KapuaId scopeId = kapuaEvent.getScopeId();
            String clientId = kapuaEvent.getInputs();
            DeviceConnectionStatus deviceConnectionStatus = DeviceConnectionStatus.fromString(kapuaEvent.getOperation());
            appendEvent(queuedOn, scopeId, clientId, deviceConnectionStatus);
            metricLifecycleOkMessage.inc();
        } catch (Exception e) {
            metricLifecycleErrorMessage.inc();
            logger.error("Exception converting message {}", e.getMessage(), e);
        }
    }

    private void appendEvent(Date queuedOn, KapuaId scopeId, String clientId, DeviceConnectionStatus deviceConnectionStatus) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            Device device = deviceRegistryService.findByClientId(scopeId, clientId);
            if (device == null) {
                DeviceConnection connection = deviceConnectionService.findByClientId(scopeId, clientId);
                if (connection != null) {
                    connection.setStatus(deviceConnectionStatus);
                    deviceConnectionService.update(connection);
                } else {
                    logger.warn("Processed {} event for clientId {} but nor the Device nor the DeviceConnection have been found. " +
                            "Likely the DeviceConnection has been deleted before the Device actually disconnected (i.e. during the DeviceProvisioning).", deviceConnectionStatus, clientId);
                }
            }
            else {
                DeviceEventCreator eventCreator = deviceEventFactory.newCreator(scopeId);
                eventCreator.setDeviceId(device.getId());
                eventCreator.setSentOn(queuedOn);
                eventCreator.setReceivedOn(Date.from(KapuaDateUtils.getKapuaSysDate()));
                eventCreator.setResource(deviceConnectionStatus.name());
                eventCreator.setAction(KapuaMethod.CREATE);
                eventCreator.setResponseCode(KapuaResponseCode.ACCEPTED);
                deviceEventService.create(eventCreator);
            }
        });
    }

}
