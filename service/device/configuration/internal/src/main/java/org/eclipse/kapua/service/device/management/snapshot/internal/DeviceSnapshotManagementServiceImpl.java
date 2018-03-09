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
package org.eclipse.kapua.service.device.management.snapshot.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementErrorCodes;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementException;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationAppProperties;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotManagementService;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.device.management.snapshot.internal.exception.SnapshotGetManagementException;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestChannel;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestMessage;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotRequestPayload;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotResponseMessage;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotResponsePayload;

import java.util.Date;

/**
 * Device snapshot service implementation.
 *
 * @since 1.0
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
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

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
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(snapshotRequestMessage, timeout);
        SnapshotResponseMessage responseMessage = (SnapshotResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, snapshotRequestMessage, responseMessage);

        //
        // Check response
        if (responseMessage.getResponseCode().isAccepted()) {
            SnapshotResponsePayload responsePayload = responseMessage.getPayload();

            DeviceManagementSetting config = DeviceManagementSetting.getInstance();
            String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

            String body = null;
            try {
                body = new String(responsePayload.getBody(), charEncoding);
            } catch (Exception e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION,
                        e,
                        responsePayload.getBody());
            }

            DeviceSnapshots deviceSnapshots = null;
            try {
                deviceSnapshots = XmlUtil.unmarshal(body, DeviceSnapshotsImpl.class);
            } catch (Exception e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION,
                        e,
                        body);
            }

            return deviceSnapshots;
        } else {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new SnapshotGetManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
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
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.execute, scopeId));

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
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(snapshotRequestMessage, timeout);
        SnapshotResponseMessage responseMessage = (SnapshotResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, snapshotRequestMessage, responseMessage);

        if (!responseMessage.getResponseCode().isAccepted()) {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new SnapshotGetManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }
}
