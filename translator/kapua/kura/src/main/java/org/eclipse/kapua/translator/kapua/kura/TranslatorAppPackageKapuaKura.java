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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.translator.kapua.kura;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.call.kura.model.deploy.PackageMetrics;
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

    private final Map<PackageAppProperties, PackageMetrics> propertiesDictionary = new EnumMap<>(PackageAppProperties.class);

    public TranslatorAppPackageKapuaKura() {
        // Commons properties
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID, PackageMetrics.APP_METRIC_PACKAGE_OPERATION_ID);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT, PackageMetrics.APP_METRIC_PACKAGE_REBOOT);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT_DELAY, PackageMetrics.APP_METRIC_PACKAGE_REBOOT_DELAY);

        // Download properties
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_URI);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_NAME);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PACKAGE_VERSION);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_USERNAME, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_USERNAME);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PASSWORD, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PASSWORD);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_FILE_HASH, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_HASH);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_FILE_TYPE, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_INSTALL_SYSTEM_UPDATE);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_INSTALL);

        // Download advanced properties
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_RESTART, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_FORCE);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_SIZE, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_BLOCK_SIZE);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_DELAY, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_BLOCK_DELAY);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_TIMEOUT, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_TIMEOUT);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_NOTIFY_BLOCK_SIZE, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_NOTIFY_BLOCK_SIZE);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_INSTALL_VERIFIER_URI, PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_INSTALL_VERIFIER_URI);

        // Install properties
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_INSTALL_PACKAGE_NAME);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_INSTALL_PACKAGE_VERSION);

        // Uninstall properties
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME, PackageMetrics.APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_NAME);
        propertiesDictionary.put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION, PackageMetrics.APP_METRIC_PACKAGE_UNINSTALL_PACKAGE_VERSION);
    }

    @Override
    protected KuraRequestChannel translateChannel(PackageRequestChannel kapuaChannel) throws InvalidChannelException {
        try {
            KuraRequestChannel kuraRequestChannel = TranslatorKapuaKuraUtils.buildBaseRequestChannel(PackageMetrics.APP_ID, PackageMetrics.APP_VERSION, kapuaChannel.getMethod());

            // Build resources
            List<String> resources = new ArrayList<>();
            if (kapuaChannel.getPackageResource() == null) {
                resources.add("packages");
            } else {
                switch (kapuaChannel.getPackageResource()) {
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
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID).getName(), operationId.getId().longValue());
            }

            metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT).getName(), kapuaPayload.isReboot());
            metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT_DELAY).getName(), kapuaPayload.getRebootDelay());

            if (kapuaPayload.isDownloadRequest()) {
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI).getName(), kapuaPayload.getPackageDownloadURI().toString());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME).getName(), kapuaPayload.getPackageDownloadName());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION).getName(), kapuaPayload.getPackageDownloadVersion());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_USERNAME).getName(), kapuaPayload.getPackageDownloadUsername());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PASSWORD).getName(), kapuaPayload.getPackageDownloadPassword());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_FILE_HASH).getName(), kapuaPayload.getPackageDownloadFileHash());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_FILE_TYPE).getName(), FileType.EXECUTABLE_SCRIPT.equals(kapuaPayload.getPackageDownloadFileType()));
                metrics.put(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PROTOCOL.getName(), "HTTP");
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL).getName(), kapuaPayload.isPackageDownloadInstall());

                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_RESTART).getName(), kapuaPayload.getPackageDownloadRestart());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_SIZE).getName(), kapuaPayload.getPackageDownloadBlockSize());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_DELAY).getName(), kapuaPayload.getPackageDownloadBlockDelay());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_BLOCK_TIMEOUT).getName(), kapuaPayload.getPackageDownloadBlockTimeout());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_NOTIFY_BLOCK_SIZE).getName(), kapuaPayload.getPackageDownloadNotifyBlockSize());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_INSTALL_VERIFIER_URI).getName(), kapuaPayload.getPackageDownloadInstallVerifierURI());

            } else if (kapuaPayload.isInstallRequest()) {
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME).getName(), kapuaPayload.getPackageInstallName());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION).getName(), kapuaPayload.getPackageInstallVersion());
                metrics.put(PackageMetrics.APP_METRIC_PACKAGE_INSTALL_SYS_UPDATE.getName(), false);
            } else if (kapuaPayload.isUninstallRequest()) {
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME).getName(), kapuaPayload.getPackageUninstallName());
                metrics.put(propertiesDictionary.get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION).getName(), kapuaPayload.getPackageUninstallVersion());
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
