/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.call.kura.app.PackageMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestPayload;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageAppProperties;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestChannel;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestMessage;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageRequestPayload;
import org.eclipse.kapua.service.device.management.packages.model.FileType;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link PackageRequestMessage} to {@link KuraRequestMessage}
 *
 * @since 1.0.0
 */
public class TranslatorAppPackageKapuaKura extends AbstractTranslatorKapuaKura<PackageRequestChannel, PackageRequestPayload, PackageRequestMessage> {

    private static final Map<PackageAppProperties, PackageMetrics> PROPERTIES_DICTIONARY = new EnumMap<>(PackageAppProperties.class);

    static {
        // Commons properties
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID, PackageMetrics.APP_METRIC_PACKAGE_OPERATION_ID);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT, PackageMetrics.APP_METRIC_PACKAGE_REBOOT);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT_DELAY, PackageMetrics.APP_METRIC_PACKAGE_REBOOT_DELAY);

        // Download properties
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_URI);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_NAME);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_VERSION);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_USERNAME, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_USERNAME);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PASSWORD, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PASSWORD);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_FILE_HASH, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_HASH);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_FILE_TYPE, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_INSTALL_SYSTEM_UPDATE);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_INSTALL);

        // Download advanced properties
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_RESTART, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_FORCE);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_SIZE, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_BLOCK_SIZE);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_DELAY, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_BLOCK_DELAY);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_TIMEOUT, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_TIMEOUT);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_NOTIFY_BLOCK_SIZE, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_NOTIFY_BLOCK_SIZE);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_INSTALL_VERIFIER_URI, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_INSTALL_VERIFIER_URI);

        // Install properties
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_INSTALL_PACKAGE_NAME);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_INSTALL_PACKAGE_VERSION);

        // Uninstall properties
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_NAME);
        PROPERTIES_DICTIONARY.put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_VERSION);

    }

    @Override
    protected KuraRequestChannel translateChannel(PackageRequestChannel kapuaChannel) throws InvalidChannelException {
        try {
        KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
            kuraRequestChannel.setMessageClassification(getControlMessageClassifier());
            kuraRequestChannel.setAppId(PackageMetrics.APP_ID + "-" + PackageMetrics.APP_VERSION);
            kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.translate(kapuaChannel.getMethod()));

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
        kuraRequestChannel.setResources(resources.toArray(new String[0]));

        // Return Kura Channel
        return kuraRequestChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kapuaChannel);
        }
    }

    @Override
    protected KuraRequestPayload translatePayload(PackageRequestPayload kapuaPayload) throws InvalidPayloadException {
        try {
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
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_USERNAME).getValue(), kapuaPayload.getPackageDownloadUsername());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PASSWORD).getValue(), kapuaPayload.getPackageDownloadPassword());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_FILE_HASH).getValue(), kapuaPayload.getPackageDownloadFileHash());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_FILE_TYPE).getValue(), FileType.EXECUTABLE_SCRIPT.equals(kapuaPayload.getPackageDownloadFileType()));
            metrics.put(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PROTOCOL.getValue(), "HTTP");
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL).getValue(), kapuaPayload.isPackageDownloadInstall());

            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_RESTART).getValue(), kapuaPayload.getPackageDownloadRestart());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_SIZE).getValue(), kapuaPayload.getPackageDownloadBlockSize());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_DELAY).getValue(), kapuaPayload.getPackageDownloadBlockDelay());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_TIMEOUT).getValue(), kapuaPayload.getPackageDownloadBlockTimeout());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_NOTIFY_BLOCK_SIZE).getValue(), kapuaPayload.getPackageDownloadNotifyBlockSize());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_INSTALL_VERIFIER_URI).getValue(), kapuaPayload.getPackageDownloadInstallVerifierURI());

        } else if (kapuaPayload.isInstallRequest()) {
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME).getValue(), kapuaPayload.getPackageInstallName());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION).getValue(), kapuaPayload.getPackageInstallVersion());
            metrics.put(PackageMetrics.APP_METRIC_PACKAGE_INSTALL_SYS_UPDATE.getValue(), false);
        } else if (kapuaPayload.isUninstallRequest()) {
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME).getValue(), kapuaPayload.getPackageUninstallName());
            metrics.put(PROPERTIES_DICTIONARY.get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION).getValue(), kapuaPayload.getPackageUninstallVersion());
        }

        // Return Kura Payload
        return kuraRequestPayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kapuaPayload);
        }
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
