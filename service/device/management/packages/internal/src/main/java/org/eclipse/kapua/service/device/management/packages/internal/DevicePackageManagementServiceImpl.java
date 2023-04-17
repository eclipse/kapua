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
package org.eclipse.kapua.service.device.management.packages.internal;

import com.google.common.base.MoreObjects;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallBuilder;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.packages.DevicePackageFactory;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.internal.setting.PackageManagementServiceSetting;
import org.eclipse.kapua.service.device.management.packages.internal.setting.PackageManagementServiceSettingKeys;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageAppProperties;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestChannel;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestMessage;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestPayload;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageResource;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageResponseMessage;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageResponsePayload;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.AdvancedPackageDownloadOptions;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOperation;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOptions;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.download.internal.DevicePackageDownloadOperationImpl;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.Date;

/**
 * {@link DevicePackageManagementService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DevicePackageManagementServiceImpl extends AbstractDeviceManagementServiceImpl implements DevicePackageManagementService {

    private static final Logger LOG = LoggerFactory.getLogger(DevicePackageManagementServiceImpl.class);

    private static final PackageManagementServiceSetting PACKAGE_MANAGEMENT_SERVICE_SETTING = PackageManagementServiceSetting.getInstance();

    private static final DevicePackageFactory DEVICE_PACKAGE_FACTORY = LOCATOR.getFactory(DevicePackageFactory.class);

    private static final String SCOPE_ID = "scopeId";
    private static final String DEVICE_ID = "deviceId";

    //
    // Installed
    //

    @Override
    public DevicePackages getInstalled(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        PackageRequestChannel packageRequestChannel = new PackageRequestChannel();
        packageRequestChannel.setAppName(PackageAppProperties.APP_NAME);
        packageRequestChannel.setVersion(PackageAppProperties.APP_VERSION);
        packageRequestChannel.setMethod(KapuaMethod.READ);
        packageRequestChannel.setPackageResource(null);

        PackageRequestPayload packageRequestPayload = new PackageRequestPayload();

        PackageRequestMessage packageRequestMessage = new PackageRequestMessage();
        packageRequestMessage.setScopeId(scopeId);
        packageRequestMessage.setDeviceId(deviceId);
        packageRequestMessage.setCapturedOn(new Date());
        packageRequestMessage.setPayload(packageRequestPayload);
        packageRequestMessage.setChannel(packageRequestChannel);

        //
        // Build request
        DeviceCallBuilder<PackageRequestChannel, PackageRequestPayload, PackageRequestMessage, PackageResponseMessage> packageDeviceCallBuilder =
                DeviceCallBuilder
                        .newBuilder()
                        .withRequestMessage(packageRequestMessage)
                        .withTimeoutOrDefault(timeout);

        //
        // Do get
        PackageResponseMessage responseMessage;
        try {
            responseMessage = packageDeviceCallBuilder.send();
        } catch (Exception e) {
            LOG.error("Error while getting DevicePackages {} for Device {}. Error: {}", deviceId, e.getMessage(), e);
            throw e;
        }

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDevicePackages());
    }

    //
    // Download
    //

    @Override
    public KapuaId downloadExec(KapuaId scopeId, KapuaId deviceId, DevicePackageDownloadRequest packageDownloadRequest, Long timeout) throws KapuaException {
        DevicePackageDownloadOptions packageDownloadOptions = DEVICE_PACKAGE_FACTORY.newPackageDownloadOptions();
        packageDownloadOptions.setTimeout(timeout);

        return downloadExec(scopeId, deviceId, packageDownloadRequest, packageDownloadOptions);
    }

    @Override
    public KapuaId downloadExec(KapuaId scopeId, KapuaId deviceId, DevicePackageDownloadRequest packageDownloadRequest, DevicePackageDownloadOptions packageDownloadOptions) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(packageDownloadRequest, "packageDownloadRequest");
        ArgumentValidator.notNull(packageDownloadRequest.getUri(), "packageDownloadRequest.uri");
        ArgumentValidator.notNull(packageDownloadRequest.getName(), "packageDownloadRequest.name");
        ArgumentValidator.notNull(packageDownloadRequest.getVersion(), "packageDownloadRequest.version");
        ArgumentValidator.notNull(packageDownloadOptions, "packageDownloadOptions");

        try {
            packageDownloadRequest.getUri().toURL();
        } catch (MalformedURLException | IllegalArgumentException ignored) {
            throw new KapuaIllegalArgumentException("packageDownloadRequest.uri", packageDownloadRequest.getUri().toString());
        }
        verifyOverflowPackageFields(packageDownloadRequest);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));

        //
        // Generate requestId
        KapuaId operationId = new KapuaEid(IdGenerator.generate());

        //
        // Prepare the request
        PackageRequestChannel packageRequestChannel = new PackageRequestChannel();
        packageRequestChannel.setAppName(PackageAppProperties.APP_NAME);
        packageRequestChannel.setVersion(PackageAppProperties.APP_VERSION);
        packageRequestChannel.setMethod(KapuaMethod.EXECUTE);
        packageRequestChannel.setPackageResource(PackageResource.DOWNLOAD);

        PackageRequestPayload packageRequestPayload = new PackageRequestPayload();

        // Basic options
        packageRequestPayload.setOperationId(operationId);
        packageRequestPayload.setPackageDownloadURI(packageDownloadRequest.getUri());
        packageRequestPayload.setPackageDownloadName(packageDownloadRequest.getName());
        packageRequestPayload.setPackageDownloadVersion(packageDownloadRequest.getVersion());
        packageRequestPayload.setPackageDownloadUsername(packageDownloadRequest.getUsername());
        packageRequestPayload.setPackageDownloadPassword(packageDownloadRequest.getPassword());
        packageRequestPayload.setPackageDownloadFileHash(packageDownloadRequest.getFileHash());
        packageRequestPayload.setPackageDownloadFileType(packageDownloadRequest.getFileType());
        packageRequestPayload.setPackageDownloadnstall(packageDownloadRequest.getInstall());
        packageRequestPayload.setReboot(packageDownloadRequest.getReboot());
        packageRequestPayload.setRebootDelay(packageDownloadRequest.getRebootDelay());

        // Advanced ones
        AdvancedPackageDownloadOptions advancedOptions = packageDownloadRequest.getAdvancedOptions();

        packageRequestPayload.setPackageDownloadRestart(advancedOptions.getRestart());
        packageRequestPayload.setPackageDownloadBlockSize(MoreObjects.firstNonNull(advancedOptions.getBlockSize(), PACKAGE_MANAGEMENT_SERVICE_SETTING.getInt(PackageManagementServiceSettingKeys.PACKAGE_MANAGEMENT_SERVICE_SETTING_DOWDLOAD_DEFAULT_BLOCK_SIZE)));
        packageRequestPayload.setPackageDownloadBlockDelay(MoreObjects.firstNonNull(advancedOptions.getBlockDelay(), PACKAGE_MANAGEMENT_SERVICE_SETTING.getInt(PackageManagementServiceSettingKeys.PACKAGE_MANAGEMENT_SERVICE_SETTING_DOWDLOAD_DEFAULT_BLOCK_DELAY)));
        packageRequestPayload.setPackageDownloadBlockTimeout(MoreObjects.firstNonNull(advancedOptions.getBlockTimeout(), PACKAGE_MANAGEMENT_SERVICE_SETTING.getInt(PackageManagementServiceSettingKeys.PACKAGE_MANAGEMENT_SERVICE_SETTING_DOWDLOAD_DEFAULT_BLOCK_TIMEOUT)));
        packageRequestPayload.setPackageDownloadBlockSize(MoreObjects.firstNonNull(advancedOptions.getNotifyBlockSize(), PACKAGE_MANAGEMENT_SERVICE_SETTING.getInt(PackageManagementServiceSettingKeys.PACKAGE_MANAGEMENT_SERVICE_SETTING_DOWDLOAD_DEFAULT_NOTIFY_BLOCK_SIZE)));
        packageRequestPayload.setPackageDownloadInstallVerifierURI(advancedOptions.getInstallVerifierURI());

        // Message
        PackageRequestMessage packageRequestMessage = new PackageRequestMessage();
        packageRequestMessage.setScopeId(scopeId);
        packageRequestMessage.setDeviceId(deviceId);
        packageRequestMessage.setCapturedOn(new Date());
        packageRequestMessage.setPayload(packageRequestPayload);
        packageRequestMessage.setChannel(packageRequestChannel);

        //
        // Create device management operation
        KapuaId deviceManagementOperationId = createManagementOperation(scopeId, deviceId, operationId, packageRequestMessage);

        //
        // Build request
        DeviceCallBuilder<PackageRequestChannel, PackageRequestPayload, PackageRequestMessage, PackageResponseMessage> packageDeviceCallBuilder =
                DeviceCallBuilder
                        .newBuilder()
                        .withRequestMessage(packageRequestMessage)
                        .withTimeoutOrDefault(packageDownloadOptions.getTimeout());

        //
        // Do exec
        PackageResponseMessage responseMessage;
        try {
            responseMessage = packageDeviceCallBuilder.send();
        } catch (Exception e) {
            closeManagementOperation(scopeId, deviceId, operationId);
            LOG.error("Error while executing DevicePackageDownloadRequest {} for Device {}. Error: {}", packageDownloadRequest.getUri(), deviceId, e.getMessage(), e);
            throw e;
        }

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        try {
            checkResponseAcceptedOrThrowError(responseMessage);
        } catch (Exception e) {
            closeManagementOperation(scopeId, deviceId, operationId, responseMessage);
            throw e;
        }

        //
        // Return operation id
        return deviceManagementOperationId;
    }

    @Override
    public DevicePackageDownloadOperation downloadStatus(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        PackageRequestChannel packageRequestChannel = new PackageRequestChannel();
        packageRequestChannel.setAppName(PackageAppProperties.APP_NAME);
        packageRequestChannel.setVersion(PackageAppProperties.APP_VERSION);
        packageRequestChannel.setMethod(KapuaMethod.READ);
        packageRequestChannel.setPackageResource(PackageResource.DOWNLOAD);

        PackageRequestPayload packageRequestPayload = new PackageRequestPayload();

        PackageRequestMessage packageRequestMessage = new PackageRequestMessage();
        packageRequestMessage.setScopeId(scopeId);
        packageRequestMessage.setDeviceId(deviceId);
        packageRequestMessage.setCapturedOn(new Date());
        packageRequestMessage.setPayload(packageRequestPayload);
        packageRequestMessage.setChannel(packageRequestChannel);

        //
        // Build request
        DeviceCallBuilder<PackageRequestChannel, PackageRequestPayload, PackageRequestMessage, PackageResponseMessage> packageDeviceCallBuilder =
                DeviceCallBuilder
                        .newBuilder()
                        .withRequestMessage(packageRequestMessage)
                        .withTimeoutOrDefault(timeout);

        //
        // Do get
        PackageResponseMessage responseMessage;
        try {
            responseMessage = packageDeviceCallBuilder.send();
        } catch (Exception e) {
            LOG.error("Error while getting DevicePackageDownloadOperation for Device {}. Error: {}", deviceId, e.getMessage(), e);
            throw e;
        }

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> {
            PackageResponsePayload responsePayload = responseMessage.getPayload();

            DevicePackageDownloadOperation downloadOperation = new DevicePackageDownloadOperationImpl();
            downloadOperation.setId(responsePayload.getPackageDownloadOperationId());
            downloadOperation.setStatus(responsePayload.getPackageDownloadOperationStatus());
            downloadOperation.setSize(responsePayload.getPackageDownloadOperationSize());
            downloadOperation.setProgress(responsePayload.getPackageDownloadOperationProgress());

            return downloadOperation;
        });
    }

    @Override
    public void downloadStop(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));

        //
        // Prepare the request
        PackageRequestChannel packageRequestChannel = new PackageRequestChannel();
        packageRequestChannel.setAppName(PackageAppProperties.APP_NAME);
        packageRequestChannel.setVersion(PackageAppProperties.APP_VERSION);
        packageRequestChannel.setMethod(KapuaMethod.DELETE);
        packageRequestChannel.setPackageResource(PackageResource.DOWNLOAD);

        PackageRequestPayload packageRequestPayload = new PackageRequestPayload();

        PackageRequestMessage packageRequestMessage = new PackageRequestMessage();
        packageRequestMessage.setScopeId(scopeId);
        packageRequestMessage.setDeviceId(deviceId);
        packageRequestMessage.setCapturedOn(new Date());
        packageRequestMessage.setPayload(packageRequestPayload);
        packageRequestMessage.setChannel(packageRequestChannel);

        //
        // Build request
        DeviceCallBuilder<PackageRequestChannel, PackageRequestPayload, PackageRequestMessage, PackageResponseMessage> packageDeviceCallBuilder =
                DeviceCallBuilder
                        .newBuilder()
                        .withRequestMessage(packageRequestMessage)
                        .withTimeoutOrDefault(timeout);

        //
        // Do del
        PackageResponseMessage responseMessage;
        try {
            responseMessage = packageDeviceCallBuilder.send();
        } catch (Exception e) {
            LOG.error("Error while stopping DevicePackageDownloadOperation for Device {}. Error: {}", deviceId, e.getMessage(), e);
            throw e;
        }

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        checkResponseAcceptedOrThrowError(responseMessage);
    }

    //
    // Install
    //

    @Override
    public KapuaId installExec(KapuaId scopeId, KapuaId deviceId, DevicePackageInstallRequest packageInstallRequest, Long timeout) throws KapuaException {
        DevicePackageInstallOptions packageInstallOptions = DEVICE_PACKAGE_FACTORY.newPackageInstallOptions();
        packageInstallOptions.setTimeout(timeout);

        return installExec(scopeId, deviceId, packageInstallRequest, packageInstallOptions);
    }

    @Override
    public KapuaId installExec(KapuaId scopeId, KapuaId deviceId, DevicePackageInstallRequest deployInstallRequest, DevicePackageInstallOptions packageInstallOptions) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(deployInstallRequest, "deployInstallRequest");
        ArgumentValidator.notNull(packageInstallOptions, "packageInstallOptions");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));

        //
        // Generate requestId
        KapuaId operationId = new KapuaEid(IdGenerator.generate());

        //
        // Prepare the request
        PackageRequestChannel packageRequestChannel = new PackageRequestChannel();
        packageRequestChannel.setAppName(PackageAppProperties.APP_NAME);
        packageRequestChannel.setVersion(PackageAppProperties.APP_VERSION);
        packageRequestChannel.setMethod(KapuaMethod.EXECUTE);
        packageRequestChannel.setPackageResource(PackageResource.INSTALL);

        PackageRequestPayload packageRequestPayload = new PackageRequestPayload();
        packageRequestPayload.setOperationId(operationId);
        packageRequestPayload.setPackageDownloadName(deployInstallRequest.getName());
        packageRequestPayload.setPackageDownloadVersion(deployInstallRequest.getVersion());
        packageRequestPayload.setReboot(deployInstallRequest.isReboot());
        packageRequestPayload.setRebootDelay(deployInstallRequest.getRebootDelay());

        PackageRequestMessage packageRequestMessage = new PackageRequestMessage();
        packageRequestMessage.setScopeId(scopeId);
        packageRequestMessage.setDeviceId(deviceId);
        packageRequestMessage.setCapturedOn(new Date());
        packageRequestMessage.setPayload(packageRequestPayload);
        packageRequestMessage.setChannel(packageRequestChannel);

        //
        // Create device management operation
        KapuaId deviceManagementOperationId = createManagementOperation(scopeId, deviceId, operationId, packageRequestMessage);

        //
        // Build request
        DeviceCallBuilder<PackageRequestChannel, PackageRequestPayload, PackageRequestMessage, PackageResponseMessage> packageDeviceCallBuilder =
                DeviceCallBuilder
                        .newBuilder()
                        .withRequestMessage(packageRequestMessage)
                        .withTimeoutOrDefault(packageInstallOptions.getTimeout());

        //
        // Do get
        PackageResponseMessage responseMessage;
        try {
            responseMessage = packageDeviceCallBuilder.send();
        } catch (Exception e) {
            closeManagementOperation(scopeId, deviceId, operationId);
            LOG.error("Error while executing DevicePackageInstallRequest {} for Device {}. Error: {}", deployInstallRequest.getName(), deviceId, e.getMessage(), e);
            throw e;
        }

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        try {
            checkResponseAcceptedOrThrowError(responseMessage);
        } catch (Exception e) {
            closeManagementOperation(scopeId, deviceId, operationId, responseMessage);
            throw e;
        }

        //
        // Return operation id
        return deviceManagementOperationId;
    }

    @Override
    public DevicePackageInstallOperation installStatus(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        PackageRequestChannel packageRequestChannel = new PackageRequestChannel();
        packageRequestChannel.setAppName(PackageAppProperties.APP_NAME);
        packageRequestChannel.setVersion(PackageAppProperties.APP_VERSION);
        packageRequestChannel.setMethod(KapuaMethod.READ);
        packageRequestChannel.setPackageResource(PackageResource.INSTALL);

        PackageRequestPayload packageRequestPayload = new PackageRequestPayload();

        PackageRequestMessage packageRequestMessage = new PackageRequestMessage();
        packageRequestMessage.setScopeId(scopeId);
        packageRequestMessage.setDeviceId(deviceId);
        packageRequestMessage.setCapturedOn(new Date());
        packageRequestMessage.setPayload(packageRequestPayload);
        packageRequestMessage.setChannel(packageRequestChannel);

        //
        // Build request
        DeviceCallBuilder<PackageRequestChannel, PackageRequestPayload, PackageRequestMessage, PackageResponseMessage> packageDeviceCallBuilder =
                DeviceCallBuilder
                        .newBuilder()
                        .withRequestMessage(packageRequestMessage)
                        .withTimeoutOrDefault(timeout);

        //
        // Do get
        PackageResponseMessage responseMessage;
        try {
            responseMessage = packageDeviceCallBuilder.send();
        } catch (Exception e) {
            LOG.error("Error while getting DevicePackageInstallOperation for Device {}. Error: {}", deviceId, e.getMessage(), e);
            throw e;
        }

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDevicePackageInstallOperation());
    }

    //
    // Uninstall
    //

    @Override
    public KapuaId uninstallExec(KapuaId scopeId, KapuaId deviceId, DevicePackageUninstallRequest packageUninstallRequest, Long timeout) throws KapuaException {
        DevicePackageUninstallOptions packageUninstallOptions = DEVICE_PACKAGE_FACTORY.newPackageUninstallOptions();
        packageUninstallOptions.setTimeout(timeout);

        return uninstallExec(scopeId, deviceId, packageUninstallRequest, packageUninstallOptions);
    }

    @Override
    public KapuaId uninstallExec(KapuaId scopeId, KapuaId deviceId, DevicePackageUninstallRequest packageUninstallRequest, DevicePackageUninstallOptions packageUninstallOptions) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(packageUninstallRequest, "packageUninstallRequest");
        ArgumentValidator.notNull(packageUninstallOptions, "packageUninstallOptions");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));

        //
        // Generate requestId
        KapuaId operationId = new KapuaEid(IdGenerator.generate());

        //
        // Prepare the request
        PackageRequestChannel packageRequestChannel = new PackageRequestChannel();
        packageRequestChannel.setAppName(PackageAppProperties.APP_NAME);
        packageRequestChannel.setVersion(PackageAppProperties.APP_VERSION);
        packageRequestChannel.setMethod(KapuaMethod.EXECUTE);
        packageRequestChannel.setPackageResource(PackageResource.UNINSTALL);

        PackageRequestPayload packageRequestPayload = new PackageRequestPayload();
        packageRequestPayload.setOperationId(operationId);
        packageRequestPayload.setPackageUninstallName(packageUninstallRequest.getName());
        packageRequestPayload.setPackageUninstallVersion(packageUninstallRequest.getVersion());
        packageRequestPayload.setReboot(packageUninstallRequest.isReboot());
        packageRequestPayload.setRebootDelay(packageUninstallRequest.getRebootDelay());

        PackageRequestMessage packageRequestMessage = new PackageRequestMessage();
        packageRequestMessage.setScopeId(scopeId);
        packageRequestMessage.setDeviceId(deviceId);
        packageRequestMessage.setCapturedOn(new Date());
        packageRequestMessage.setPayload(packageRequestPayload);
        packageRequestMessage.setChannel(packageRequestChannel);

        //
        // Create device management operation
        KapuaId deviceManagementOperationId = createManagementOperation(scopeId, deviceId, operationId, packageRequestMessage);

        //
        // Build request
        DeviceCallBuilder<PackageRequestChannel, PackageRequestPayload, PackageRequestMessage, PackageResponseMessage> packageDeviceCallBuilder =
                DeviceCallBuilder
                        .newBuilder()
                        .withRequestMessage(packageRequestMessage)
                        .withTimeoutOrDefault(packageUninstallOptions.getTimeout());

        //
        // Do uninstall
        PackageResponseMessage responseMessage;
        try {
            responseMessage = packageDeviceCallBuilder.send();
        } catch (Exception e) {
            closeManagementOperation(scopeId, deviceId, operationId);
            LOG.error("Error while executing DevicePackageUninstallRequest {} for Device {}. Error: {}", packageUninstallRequest.getName(), deviceId, e.getMessage(), e);
            throw e;
        }

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        try {
            checkResponseAcceptedOrThrowError(responseMessage);
        } catch (Exception e) {
            closeManagementOperation(scopeId, deviceId, operationId, responseMessage);
            throw e;
        }

        //
        // Return operation id
        return deviceManagementOperationId;
    }

    @Override
    public DevicePackageUninstallOperation uninstallStatus(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        PackageRequestChannel packageRequestChannel = new PackageRequestChannel();
        packageRequestChannel.setAppName(PackageAppProperties.APP_NAME);
        packageRequestChannel.setVersion(PackageAppProperties.APP_VERSION);
        packageRequestChannel.setMethod(KapuaMethod.READ);
        packageRequestChannel.setPackageResource(PackageResource.UNINSTALL);

        PackageRequestPayload packageRequestPayload = new PackageRequestPayload();

        PackageRequestMessage packageRequestMessage = new PackageRequestMessage();
        packageRequestMessage.setScopeId(scopeId);
        packageRequestMessage.setDeviceId(deviceId);
        packageRequestMessage.setCapturedOn(new Date());
        packageRequestMessage.setPayload(packageRequestPayload);
        packageRequestMessage.setChannel(packageRequestChannel);

        //
        // Build request
        DeviceCallBuilder<PackageRequestChannel, PackageRequestPayload, PackageRequestMessage, PackageResponseMessage> packageDeviceCallBuilder =
                DeviceCallBuilder
                        .newBuilder()
                        .withRequestMessage(packageRequestMessage)
                        .withTimeoutOrDefault(timeout);

        //
        // Do get
        PackageResponseMessage responseMessage;
        try {
            responseMessage = packageDeviceCallBuilder.send();
        } catch (Exception e) {
            LOG.error("Error while getting DevicePackageUninstallOperation for Device {}. Error: {}", deviceId, e.getMessage(), e);
            throw e;
        }

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDevicePackageUninstallOperation());
    }

    private void verifyOverflowPackageFields(DevicePackageDownloadRequest packageDownloadRequest) throws KapuaIllegalArgumentException {
        ArgumentValidator.lengthRange(packageDownloadRequest.getUri().toString(), null, 2048, "packageDownloadRequest.uri");
        ArgumentValidator.lengthRange(packageDownloadRequest.getName(), null, 256, "packageDownloadRequest.name");
        ArgumentValidator.lengthRange(packageDownloadRequest.getVersion(), null, 256, "packageDownloadRequest.version");
    }


}
