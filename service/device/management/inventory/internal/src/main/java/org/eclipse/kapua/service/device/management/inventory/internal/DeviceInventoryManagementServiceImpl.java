/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.inventory.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementRequestContentException;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementService;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryBundleExecRequestMessage;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryBundlesResponseMessage;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryEmptyRequestMessage;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryListResponseMessage;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryNoContentResponseMessage;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryPackagesResponseMessage;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryRequestChannel;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryRequestPayload;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventorySystemPackagesResponseMessage;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundle;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundleAction;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundles;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.packages.DeviceInventoryPackages;
import org.eclipse.kapua.service.device.management.inventory.model.system.DeviceInventorySystemPackages;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;

import java.util.Date;

/**
 * {@link DeviceInventoryManagementService} implementation.
 *
 * @since 1.5.0
 */
@KapuaProvider
public class DeviceInventoryManagementServiceImpl extends AbstractDeviceManagementServiceImpl implements DeviceInventoryManagementService {

    private static final String SCOPE_ID = "scopeId";
    private static final String DEVICE_ID = "deviceId";

    @Override
    public DeviceInventory getInventory(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        InventoryRequestChannel inventoryRequestChannel = new InventoryRequestChannel();
        inventoryRequestChannel.setAppName(DeviceInventoryAppProperties.APP_NAME);
        inventoryRequestChannel.setVersion(DeviceInventoryAppProperties.APP_VERSION);
        inventoryRequestChannel.setMethod(KapuaMethod.READ);
        inventoryRequestChannel.setResource("inventory");

        InventoryRequestPayload inventoryRequestPayload = new InventoryRequestPayload();

        InventoryEmptyRequestMessage inventoryRequestMessage = new InventoryEmptyRequestMessage() {
            @Override
            public Class<InventoryListResponseMessage> getResponseClass() {
                return InventoryListResponseMessage.class;
            }
        };

        inventoryRequestMessage.setScopeId(scopeId);
        inventoryRequestMessage.setDeviceId(deviceId);
        inventoryRequestMessage.setCapturedOn(new Date());
        inventoryRequestMessage.setPayload(inventoryRequestPayload);
        inventoryRequestMessage.setChannel(inventoryRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, InventoryListResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(inventoryRequestMessage, timeout);
        InventoryListResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, inventoryRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDeviceInventory());
    }

    @Override
    public DeviceInventoryBundles getBundles(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        InventoryRequestChannel inventoryRequestChannel = new InventoryRequestChannel();
        inventoryRequestChannel.setAppName(DeviceInventoryAppProperties.APP_NAME);
        inventoryRequestChannel.setVersion(DeviceInventoryAppProperties.APP_VERSION);
        inventoryRequestChannel.setMethod(KapuaMethod.READ);
        inventoryRequestChannel.setResource("bundles");

        InventoryRequestPayload inventoryRequestPayload = new InventoryRequestPayload();

        InventoryEmptyRequestMessage inventoryRequestMessage = new InventoryEmptyRequestMessage() {
            @Override
            public Class<InventoryBundlesResponseMessage> getResponseClass() {
                return InventoryBundlesResponseMessage.class;
            }
        };

        inventoryRequestMessage.setScopeId(scopeId);
        inventoryRequestMessage.setDeviceId(deviceId);
        inventoryRequestMessage.setCapturedOn(new Date());
        inventoryRequestMessage.setPayload(inventoryRequestPayload);
        inventoryRequestMessage.setChannel(inventoryRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, InventoryBundlesResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(inventoryRequestMessage, timeout);
        InventoryBundlesResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, inventoryRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDeviceInventoryBundles());
    }

    @Override
    public void execBundle(KapuaId scopeId, KapuaId deviceId, DeviceInventoryBundle deviceInventoryBundle, DeviceInventoryBundleAction deviceInventoryBundleAction, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(deviceInventoryBundle, "deviceInventoryBundle");
        ArgumentValidator.notNull(deviceInventoryBundle.getId(), "deviceInventoryBundle.id");
        ArgumentValidator.notNull(deviceInventoryBundle.getName(), "deviceInventoryBundle.name");
        ArgumentValidator.notNull(deviceInventoryBundleAction, "deviceInventoryBundleAction");

        performAdditionalValidationOnDeviceInventoryBundleId(deviceInventoryBundle.getId());

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));

        //
        // Prepare the request
        InventoryRequestChannel inventoryRequestChannel = new InventoryRequestChannel();
        inventoryRequestChannel.setAppName(DeviceInventoryAppProperties.APP_NAME);
        inventoryRequestChannel.setVersion(DeviceInventoryAppProperties.APP_VERSION);
        inventoryRequestChannel.setMethod(KapuaMethod.EXECUTE);
        inventoryRequestChannel.setResource("bundles");
        inventoryRequestChannel.setBundleAction(deviceInventoryBundleAction);

        InventoryRequestPayload inventoryRequestPayload = new InventoryRequestPayload();
        try {
            inventoryRequestPayload.setDeviceInventoryBundle(deviceInventoryBundle);
        } catch (Exception e) {
            throw new DeviceManagementRequestContentException(e, deviceInventoryBundle);
        }

        InventoryBundleExecRequestMessage inventoryRequestMessage = new InventoryBundleExecRequestMessage() {
            @Override
            public Class<InventoryNoContentResponseMessage> getResponseClass() {
                return InventoryNoContentResponseMessage.class;
            }
        };

        inventoryRequestMessage.setScopeId(scopeId);
        inventoryRequestMessage.setDeviceId(deviceId);
        inventoryRequestMessage.setCapturedOn(new Date());
        inventoryRequestMessage.setPayload(inventoryRequestPayload);
        inventoryRequestMessage.setChannel(inventoryRequestChannel);

        //
        // Do exec
        DeviceCallExecutor<?, ?, ?, InventoryNoContentResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(inventoryRequestMessage, timeout);
        InventoryNoContentResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, inventoryRequestMessage, responseMessage);

        //
        // Check response
        checkResponseAcceptedOrThrowError(responseMessage);
    }

    @Override
    public DeviceInventorySystemPackages getSystemPackages(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        InventoryRequestChannel inventoryRequestChannel = new InventoryRequestChannel();
        inventoryRequestChannel.setAppName(DeviceInventoryAppProperties.APP_NAME);
        inventoryRequestChannel.setVersion(DeviceInventoryAppProperties.APP_VERSION);
        inventoryRequestChannel.setMethod(KapuaMethod.READ);
        inventoryRequestChannel.setResource("systemPackages");

        InventoryRequestPayload inventoryRequestPayload = new InventoryRequestPayload();

        InventoryEmptyRequestMessage inventoryRequestMessage = new InventoryEmptyRequestMessage() {
            @Override
            public Class<InventorySystemPackagesResponseMessage> getResponseClass() {
                return InventorySystemPackagesResponseMessage.class;
            }
        };

        inventoryRequestMessage.setScopeId(scopeId);
        inventoryRequestMessage.setDeviceId(deviceId);
        inventoryRequestMessage.setCapturedOn(new Date());
        inventoryRequestMessage.setPayload(inventoryRequestPayload);
        inventoryRequestMessage.setChannel(inventoryRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, InventorySystemPackagesResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(inventoryRequestMessage, timeout);
        InventorySystemPackagesResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, inventoryRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDeviceInventorySystemPackages());
    }

    @Override
    public DeviceInventoryPackages getDeploymentPackages(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        InventoryRequestChannel inventoryRequestChannel = new InventoryRequestChannel();
        inventoryRequestChannel.setAppName(DeviceInventoryAppProperties.APP_NAME);
        inventoryRequestChannel.setVersion(DeviceInventoryAppProperties.APP_VERSION);
        inventoryRequestChannel.setMethod(KapuaMethod.READ);
        inventoryRequestChannel.setResource("deploymentPackages");

        InventoryRequestPayload inventoryRequestPayload = new InventoryRequestPayload();

        InventoryEmptyRequestMessage inventoryRequestMessage = new InventoryEmptyRequestMessage() {
            @Override
            public Class<InventoryPackagesResponseMessage> getResponseClass() {
                return InventoryPackagesResponseMessage.class;
            }
        };

        inventoryRequestMessage.setScopeId(scopeId);
        inventoryRequestMessage.setDeviceId(deviceId);
        inventoryRequestMessage.setCapturedOn(new Date());
        inventoryRequestMessage.setPayload(inventoryRequestPayload);
        inventoryRequestMessage.setChannel(inventoryRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, InventoryPackagesResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(inventoryRequestMessage, timeout);
        InventoryPackagesResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, inventoryRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDeviceInventoryPackages());
    }


    /**
     * Performs an additional check on {@link DeviceInventoryBundle#getId()} to verify that it can be converted to a {@link Integer}.
     * <p>
     * This check is required because initially the property was created as a {@link String} even if in Kura it is a {@link Integer}.
     * See Kura documentation on Device Inventory Bundle <a href="https://eclipse.github.io/kura/docs-release-5.4/administration/system-component-inventory/">here</a>
     * <p>
     * We cannot change the type of {@link DeviceInventoryBundle#getId()} from {@link String} to {@link Integer} because it would be an API breaking change.
     * We can add a validation to improve the error returned in case a non-integer value is provided, since the current error returned is {@link NumberFormatException} (at line
     * TranslatorAppInventoryBundleExecKapuaKura:74).
     *
     * @param deviceInventoryBundleId The {@link DeviceInventoryBundle#getId()} to check
     * @throws KapuaIllegalArgumentException If {@link DeviceInventoryBundle#getId()} cannot be converted to a {@link Integer}.
     * @since 2.0.0
     */
    private void performAdditionalValidationOnDeviceInventoryBundleId(String deviceInventoryBundleId) throws KapuaIllegalArgumentException {
        try {
            Integer.parseInt(deviceInventoryBundleId);
        } catch (NumberFormatException nfe) {
            throw new KapuaIllegalArgumentException("deviceInventoryBundle.id", deviceInventoryBundleId);
        }
    }

}
