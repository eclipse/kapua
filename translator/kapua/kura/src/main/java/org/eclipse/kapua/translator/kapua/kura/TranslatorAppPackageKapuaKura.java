/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.translator.kapua.kura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.call.kura.app.PackageMetrics;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestPayload;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageAppProperties;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestChannel;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestMessage;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestPayload;

/**
 * Messages translator implementation from {@link PackageRequestMessage} to {@link KuraRequestMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorAppPackageKapuaKura extends AbstractTranslatorKapuaKura<PackageRequestChannel, PackageRequestPayload, PackageRequestMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();
    private static final Map<PackageAppProperties, PackageMetrics> PROPERTIES_DICTIONARY = new HashMap<>();

    static {
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_NAME, PackageMetrics.APP_ID);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_VERSION, PackageMetrics.APP_VERSION);

        // Commons properties
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID, PackageMetrics.APP_METRIC_PACKAGE_OPERATION_ID);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT, PackageMetrics.APP_METRIC_PACKAGE_REBOOT);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT_DELAY, PackageMetrics.APP_METRIC_PACKAGE_REBOOT_DELAY);

        // Download properties
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_URI);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_NAME);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_VERSION);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_INSTALL);

        // Install properties
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_INSTALL_PACKAGE_NAME);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_INSTALL_PACKAGE_VERSION);

        // Uninstall properties
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_NAME);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_VERSION);

    }


    protected KuraRequestChannel translateChannel(PackageRequestChannel kapuaChannel) throws KapuaException {
        KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
        kuraRequestChannel.setMessageClassification(CONTROL_MESSAGE_CLASSIFIER);

        // Build appId
        StringBuilder appIdSb = new StringBuilder();
        appIdSb.append(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_NAME).getValue())
                .append("-")
                .append(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_VERSION).getValue());

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

    protected KuraRequestPayload translatePayload(PackageRequestPayload kapuaPayload) throws KapuaException {
        KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

        Map<String, Object> metrics = kuraRequestPayload.getMetrics();

        KapuaId operationId = kapuaPayload.getOperationId();
        if (operationId != null) {
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID).getValue(), operationId.getId().longValue());
        }

        metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT).getValue(), kapuaPayload.isReboot());
        metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT_DELAY).getValue(), kapuaPayload.getRebootDelay());

        if (kapuaPayload.isDownloadRequest()) {
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI).getValue(), kapuaPayload.getPackageDownloadURI().toString());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME).getValue(), kapuaPayload.getPackageDownloadName());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION).getValue(), kapuaPayload.getPackageDownloadVersion());
            metrics.put(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PROTOCOL.getValue(), "HTTP");
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL).getValue(), kapuaPayload.isPackageDownloadnstall());

            metrics.put(PackageMetrics.APP_METRIC_PACKAGE_INSTALL_SYS_UPDATE.getValue(), false);
        } else if (kapuaPayload.isInstallRequest()) {
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME).getValue(), kapuaPayload.getPackageInstallName());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION).getValue(), kapuaPayload.getPackageInstallVersion());
            metrics.put(PackageMetrics.APP_METRIC_PACKAGE_INSTALL_SYS_UPDATE.getValue(), false);
        } else if (kapuaPayload.isUninstallRequest()) {
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME).getValue(), kapuaPayload.getPackageUninstallName());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION).getValue(), kapuaPayload.getPackageUninstallVersion());
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
