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
 *
 *******************************************************************************/
package org.eclipse.kapua.translator.kapua.kura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.kura.app.PackageMetrics;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestPayload;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSetting;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettingKeys;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageAppProperties;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestChannel;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestMessage;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestPayload;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;

/**
 * Messages translator implementation from {@link PackageRequestMessage} to {@link KuraRequestMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorAppPackageKapuaKura extends Translator<PackageRequestMessage, KuraRequestMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = DeviceCallSetting.getInstance().getString(DeviceCallSettingKeys.DESTINATION_MESSAGE_CLASSIFIER);
    private static Map<PackageAppProperties, PackageMetrics> propertiesDictionary;

    /**
     * Constructor
     */
    public TranslatorAppPackageKapuaKura() {
        propertiesDictionary = new HashMap<>();

        propertiesDictionary.put(PackageAppProperties.APP_NAME, PackageMetrics.APP_ID);
        propertiesDictionary.put(PackageAppProperties.APP_VERSION, PackageMetrics.APP_VERSION);

        // Commons properties
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID, PackageMetrics.APP_METRIC_PACKAGE_OPERATION_ID);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT, PackageMetrics.APP_METRIC_PACKAGE_REBOOT);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT_DELAY, PackageMetrics.APP_METRIC_PACKAGE_REBOOT_DELAY);

        // Download properties
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_URI);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_NAME);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_VERSION);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_INSTALL);

        // Install properties
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_INSTALL_PACKAGE_NAME);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_INSTALL_PACKAGE_VERSION);

        // Uninstall properties
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_NAME);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_VERSION);

    }

    @Override
    public KuraRequestMessage translate(PackageRequestMessage kapuaMessage) throws KapuaException {
        //
        // Kura channel
        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.find(kapuaMessage.getScopeId());

        DeviceRegistryService deviceService = locator.getService(DeviceRegistryService.class);
        Device device = deviceService.find(kapuaMessage.getScopeId(),
                kapuaMessage.getDeviceId());

        KuraRequestChannel kuraRequestChannel = translate(kapuaMessage.getChannel());
        kuraRequestChannel.setScope(account.getName());
        kuraRequestChannel.setClientId(device.getClientId());

        //
        // Kura payload
        KuraRequestPayload kuraPayload = translate(kapuaMessage.getPayload());

        //
        // Return Kura Message
        return new KuraRequestMessage(kuraRequestChannel,
                kapuaMessage.getReceivedOn(),
                kuraPayload);
    }

    private KuraRequestChannel translate(PackageRequestChannel kapuaChannel) throws KapuaException {
        KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
        kuraRequestChannel.setMessageClassification(CONTROL_MESSAGE_CLASSIFIER);

        // Build appId
        StringBuilder appIdSb = new StringBuilder();
        appIdSb.append(propertiesDictionary.get(PackageAppProperties.APP_NAME).getValue())
                .append("-")
                .append(propertiesDictionary.get(PackageAppProperties.APP_VERSION).getValue());

        kuraRequestChannel.setAppId(appIdSb.toString());
        kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.get(kapuaChannel.getMethod()));

        // Build resources
        List<String> resources = new ArrayList<>();
        if (kapuaChannel.getResource() == null) {
            resources.add("packages");
        } else {
            switch (kapuaChannel.getResource()) {
            case DOWNLOAD:
                resources.add("download");
                break;
            case INSTALL:
                resources.add("install");
                break;
            case UNINSTALL:
                resources.add("uninstall");
                break;
            }
        }
        kuraRequestChannel.setResources(resources.toArray(new String[resources.size()]));

        //
        // Return Kura Channel
        return kuraRequestChannel;
    }

    private KuraRequestPayload translate(PackageRequestPayload kapuaPayload)
            throws KapuaException {
        KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

        Map<String, Object> metrics = kuraRequestPayload.getMetrics();

        KapuaId operationId = kapuaPayload.getOperationId();
        if (operationId != null) {
            metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID).getValue(), operationId.getId().longValue());
        }

        metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT).getValue(), kapuaPayload.isReboot());
        metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT_DELAY).getValue(), kapuaPayload.getRebootDelay());

        if (kapuaPayload.isDownloadRequest()) {
            metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI).getValue(), kapuaPayload.getPackageDownloadURI().toString());
            metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME).getValue(), kapuaPayload.getPackageDownloadName());
            metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION).getValue(), kapuaPayload.getPackageDownloadVersion());
            metrics.put(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PROTOCOL.getValue(), "HTTP");
            metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL).getValue(), kapuaPayload.isPackageDownloadnstall());

            metrics.put(PackageMetrics.APP_METRIC_PACKAGE_INSTALL_SYS_UPDATE.getValue(), false);
        } else if (kapuaPayload.isInstallRequest()) {
            metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME).getValue(), kapuaPayload.getPackageInstallName());
            metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION).getValue(), kapuaPayload.getPackageInstallVersion());
            metrics.put(PackageMetrics.APP_METRIC_PACKAGE_INSTALL_SYS_UPDATE.getValue(), false);
        } else if (kapuaPayload.isUninstallRequest()) {
            metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME).getValue(), kapuaPayload.getPackageUninstallName());
            metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION).getValue(), kapuaPayload.getPackageUninstallVersion());
        }

        //
        // Return Kura Payload
        return kuraRequestPayload;
    }

    @Override
    public Class<PackageRequestMessage> getClassFrom() {
        return PackageRequestMessage.class;
    }

    @Override
    public Class<KuraRequestMessage> getClassTo() {
        return KuraRequestMessage.class;
    }
}
