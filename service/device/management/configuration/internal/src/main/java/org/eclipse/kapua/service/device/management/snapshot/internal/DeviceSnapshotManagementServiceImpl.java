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
package org.eclipse.kapua.service.device.management.snapshot.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationAppProperties;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotManagementService;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestChannel;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestMessage;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestPayload;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotResponseMessage;

import java.util.Date;

/**
 * {@link DeviceSnapshotManagementService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DeviceSnapshotManagementServiceImpl extends AbstractDeviceManagementServiceImpl implements DeviceSnapshotManagementService {

    @Override
    public DeviceSnapshots get(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        SnapshotRequestChannel snapshotRequestChannel = new SnapshotRequestChannel();
        snapshotRequestChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        snapshotRequestChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);
        snapshotRequestChannel.setMethod(KapuaMethod.READ);

        SnapshotRequestPayload snapshotRequestPayload = new SnapshotRequestPayload();

        SnapshotRequestMessage snapshotRequestMessage = new SnapshotRequestMessage();
        snapshotRequestMessage.setScopeId(scopeId);
        snapshotRequestMessage.setDeviceId(deviceId);
        snapshotRequestMessage.setCapturedOn(new Date());
        snapshotRequestMessage.setPayload(snapshotRequestPayload);
        snapshotRequestMessage.setChannel(snapshotRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, SnapshotResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(snapshotRequestMessage, timeout);
        SnapshotResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, snapshotRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDeviceSnapshots());
    }

    @Override
    public void rollback(KapuaId scopeId, KapuaId deviceId, String snapshotId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notEmptyOrNull(snapshotId, "snapshotId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.execute, scopeId));

        //
        // Prepare the request
        SnapshotRequestChannel snapshotRequestChannel = new SnapshotRequestChannel();
        snapshotRequestChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        snapshotRequestChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);
        snapshotRequestChannel.setMethod(KapuaMethod.EXECUTE);
        snapshotRequestChannel.setSnapshotId(snapshotId);

        SnapshotRequestPayload snapshotRequestPayload = new SnapshotRequestPayload();

        SnapshotRequestMessage snapshotRequestMessage = new SnapshotRequestMessage();
        snapshotRequestMessage.setScopeId(scopeId);
        snapshotRequestMessage.setDeviceId(deviceId);
        snapshotRequestMessage.setCapturedOn(new Date());
        snapshotRequestMessage.setPayload(snapshotRequestPayload);
        snapshotRequestMessage.setChannel(snapshotRequestChannel);

        //
        // Do exec
        DeviceCallExecutor<?, ?, ?, SnapshotResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(snapshotRequestMessage, timeout);
        SnapshotResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, snapshotRequestMessage, responseMessage);

        //
        // Check response
        checkResponseAcceptedOrThrowError(responseMessage);
    }
}
