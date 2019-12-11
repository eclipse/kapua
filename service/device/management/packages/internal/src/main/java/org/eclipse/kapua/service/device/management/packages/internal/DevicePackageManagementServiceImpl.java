/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.internal;

import com.google.common.base.MoreObjects;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementErrorCodes;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementException;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.eclipse.kapua.service.device.management.packages.DevicePackageFactory;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.internal.exception.PackageDownloadStatusManagementException;
import org.eclipse.kapua.service.device.management.packages.internal.exception.PackageDownloadStopManagementException;
import org.eclipse.kapua.service.device.management.packages.internal.exception.PackageGetManagementException;
import org.eclipse.kapua.service.device.management.packages.internal.exception.PackageInstallExecuteManagementException;
import org.eclipse.kapua.service.device.management.packages.internal.exception.PackageInstallStatusManagementException;
import org.eclipse.kapua.service.device.management.packages.internal.exception.PackageUninstallExecuteManagementException;
import org.eclipse.kapua.service.device.management.packages.internal.exception.PackageUninstallStatusManagementException;
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
import org.eclipse.kapua.service.device.management.packages.model.install.internal.DevicePackageInstallOperationImpl;
import org.eclipse.kapua.service.device.management.packages.model.internal.DevicePackagesImpl;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.internal.DevicePackageUninstallOperationImpl;

import java.util.Date;

/**
 * {@link DevicePackageManagementService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DevicePackageManagementServiceImpl extends AbstractDeviceManagementServiceImpl implements DevicePackageManagementService {

    private static final DevicePackageFactory DEVICE_PACKAGE_FACTORY = LOCATOR.getFactory(DevicePackageFactory.class);

    private static final PackageManagementServiceSetting PACKAGE_MANAGEMENT_SERVICE_SETTING = PackageManagementServiceSetting.getInstance();

    //
    // Installed
    //

    @Override
    public DevicePackages getInstalled(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");

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
        // Do get
        DeviceCallExecutor<?, ?, ?, PackageResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(packageRequestMessage, timeout);
        PackageResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        if (responseMessage.getResponseCode().isAccepted()) {
            PackageResponsePayload responsePayload = responseMessage.getPayload();

            DevicePackages devicePackages;
            if (responsePayload.getBody() != null) {
                DeviceManagementSetting config = DeviceManagementSetting.getInstance();
                String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

                String body;
                try {
                    body = new String(responsePayload.getBody(), charEncoding);
                } catch (Exception e) {
                    throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, (Object) responsePayload.getBody());
                }

                try {
                    devicePackages = XmlUtil.unmarshal(body, DevicePackagesImpl.class);
                } catch (Exception e) {
                    throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, body);

                }
            } else {
                devicePackages = new DevicePackagesImpl();
            }

            return devicePackages;
        } else {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new PackageGetManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }

    //
    // Download
    //

    @Override
    public KapuaId downloadExec(KapuaId scopeId, KapuaId deviceId, DevicePackageDownloadRequest packageDownloadRequest, Long timeout) throws KapuaException {
        DevicePackageDownloadOptions packageDownloadOptions = DEVICE_PACKAGE_FACTORY.newDevicePackageDownloadOptions();
        packageDownloadOptions.setTimeout(timeout);

        return downloadExec(scopeId, deviceId, packageDownloadRequest, packageDownloadOptions);
    }

    @Override
    public KapuaId downloadExec(KapuaId scopeId, KapuaId deviceId, DevicePackageDownloadRequest packageDownloadRequest, DevicePackageDownloadOptions packageDownloadOptions) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notNull(packageDownloadRequest, "packageDownloadRequest");
        ArgumentValidator.notNull(packageDownloadRequest.getUri(), "packageDownloadRequest.uri");
        ArgumentValidator.notNull(packageDownloadRequest.getName(), "packageDownloadRequest.name");
        ArgumentValidator.notNull(packageDownloadRequest.getVersion(), "packageDownloadRequest.version");
        ArgumentValidator.notNull(packageDownloadOptions, "packageDownloadOptions");

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
        KapuaId deviceManagementOperationId = createManagementOperation(scopeId, deviceId, operationId, packageDownloadRequest.getInstall() ? 2 : 1, packageRequestMessage);

        //
        // Do exec
        DeviceCallExecutor<?, ?, ?, PackageResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(packageRequestMessage, packageDownloadOptions.getTimeout());
        PackageResponseMessage responseMessage;
        try {
            responseMessage = deviceApplicationCall.send();
        } catch (Exception e) {
            closeManagementOperation(scopeId, deviceId, operationId);
            throw e;
        }

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        if (!responseMessage.getResponseCode().isAccepted()) {
            closeManagementOperation(scopeId, deviceId, operationId, responseMessage);

            throw new KapuaException(KapuaErrorCodes.DOWNLOAD_PACKAGE_EXCEPTION);
        }

        return deviceManagementOperationId;
    }

    @Override
    public DevicePackageDownloadOperation downloadStatus(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");

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
        // Do get
        DeviceCallExecutor<?, ?, ?, PackageResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(packageRequestMessage, timeout);
        PackageResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        if (responseMessage.getResponseCode().isAccepted()) {
            PackageResponsePayload responsePayload = responseMessage.getPayload();

            DevicePackageDownloadOperation downloadOperation = new DevicePackageDownloadOperationImpl();
            downloadOperation.setId(responsePayload.getPackageDownloadOperationId());
            downloadOperation.setStatus(responsePayload.getPackageDownloadOperationStatus());
            downloadOperation.setSize(responsePayload.getPackageDownloadOperationSize());
            downloadOperation.setProgress(responsePayload.getPackageDownloadOperationProgress());

            return downloadOperation;
        } else {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new PackageDownloadStatusManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }

    @Override
    public void downloadStop(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");

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
        // Do del
        DeviceCallExecutor<?, ?, ?, PackageResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(packageRequestMessage, timeout);
        PackageResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        if (!responseMessage.getResponseCode().isAccepted()) {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new PackageDownloadStopManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }

    //
    // Install
    //

    @Override
    public KapuaId installExec(KapuaId scopeId, KapuaId deviceId, DevicePackageInstallRequest packageInstallRequest, Long timeout) throws KapuaException {
        DevicePackageInstallOptions packageInstallOptions = DEVICE_PACKAGE_FACTORY.newDevicePackageInstallOptions();
        packageInstallOptions.setTimeout(timeout);

        return installExec(scopeId, deviceId, packageInstallRequest, packageInstallOptions);
    }

    @Override
    public KapuaId installExec(KapuaId scopeId, KapuaId deviceId, DevicePackageInstallRequest deployInstallRequest, DevicePackageInstallOptions packageInstallOptions) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
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
        KapuaId deviceManagementOperationId = createManagementOperation(scopeId, deviceId, operationId, 1, packageRequestMessage);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, PackageResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(packageRequestMessage, packageInstallOptions.getTimeout());
        PackageResponseMessage responseMessage;
        try {
            responseMessage = deviceApplicationCall.send();
        } catch (Exception e) {
            closeManagementOperation(scopeId, deviceId, operationId);
            throw e;
        }

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        if (!responseMessage.getResponseCode().isAccepted()) {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            closeManagementOperation(scopeId, deviceId, operationId, responseMessage);

            throw new PackageInstallExecuteManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }

        return deviceManagementOperationId;
    }

    @Override
    public DevicePackageInstallOperation installStatus(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");

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
        // Do get
        DeviceCallExecutor<?, ?, ?, PackageResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(packageRequestMessage, timeout);
        PackageResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        if (responseMessage.getResponseCode().isAccepted()) {
            PackageResponsePayload responsePayload = responseMessage.getPayload();

            DeviceManagementSetting config = DeviceManagementSetting.getInstance();
            String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

            String body;
            try {
                body = new String(responsePayload.getBody(), charEncoding);
            } catch (Exception e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, (Object) responsePayload.getBody());

            }

            DevicePackageInstallOperation installOperation;
            try {
                installOperation = XmlUtil.unmarshal(body, DevicePackageInstallOperationImpl.class);
            } catch (Exception e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, body);

            }

            return installOperation;
        } else {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new PackageInstallStatusManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }

    //
    // Uninstall
    //

    @Override
    public KapuaId uninstallExec(KapuaId scopeId, KapuaId deviceId, DevicePackageUninstallRequest packageUninstallRequest, Long timeout) throws KapuaException {
        DevicePackageUninstallOptions packageUninstallOptions = DEVICE_PACKAGE_FACTORY.newDevicePackageUninstallOptions();
        packageUninstallOptions.setTimeout(timeout);

        return uninstallExec(scopeId, deviceId, packageUninstallRequest, packageUninstallOptions);
    }

    @Override
    public KapuaId uninstallExec(KapuaId scopeId, KapuaId deviceId, DevicePackageUninstallRequest packageUninstallRequest, DevicePackageUninstallOptions packageUninstallOptions) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
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
        KapuaId deviceManagementOperationId = createManagementOperation(scopeId, deviceId, operationId, 1, packageRequestMessage);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, PackageResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(packageRequestMessage, packageUninstallOptions.getTimeout());
        PackageResponseMessage responseMessage;
        try {
            responseMessage = deviceApplicationCall.send();
        } catch (Exception e) {
            closeManagementOperation(scopeId, deviceId, operationId);
            throw e;
        }

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        if (!responseMessage.getResponseCode().isAccepted()) {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            closeManagementOperation(scopeId, deviceId, operationId, responseMessage);

            throw new PackageUninstallExecuteManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }

        return deviceManagementOperationId;
    }

    @Override
    public DevicePackageUninstallOperation uninstallStatus(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");

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
        // Do get
        DeviceCallExecutor<?, ?, ?, PackageResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(packageRequestMessage, timeout);
        PackageResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, packageRequestMessage, responseMessage);

        //
        // Check response
        if (responseMessage.getResponseCode().isAccepted()) {
            PackageResponsePayload responsePayload = responseMessage.getPayload();

            DeviceManagementSetting config = DeviceManagementSetting.getInstance();
            String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

            String body;
            try {
                body = new String(responsePayload.getBody(), charEncoding);
            } catch (Exception e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, (Object) responsePayload.getBody());

            }

            DevicePackageUninstallOperation uninstallOperation;
            try {
                uninstallOperation = XmlUtil.unmarshal(body, DevicePackageUninstallOperationImpl.class);
            } catch (Exception e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, body);

            }

            return uninstallOperation;
        } else {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new PackageUninstallStatusManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }
}
