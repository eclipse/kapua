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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.kura.app.PackageMetrics;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraBundleInfo;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraDeploymentPackage;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraDeploymentPackages;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseCode;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.packages.DevicePackageFactory;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageAppProperties;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageResponseChannel;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageResponseMessage;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageResponsePayload;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfo;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfos;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadStatus;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Map;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link KuraResponseMessage} to {@link PackageResponseMessage}
 *
 * @since 1.0.0
 */
public class TranslatorAppPackageKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<PackageResponseChannel, PackageResponsePayload, PackageResponseMessage> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    public TranslatorAppPackageKuraKapua() {
        super(PackageResponseMessage.class);
    }

    @Override
    protected PackageResponseChannel translateChannel(KuraResponseChannel kuraChannel) throws InvalidChannelException {
        try {
            if (!getControlMessageClassifier().equals(kuraChannel.getMessageClassification())) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER, null, kuraChannel.getMessageClassification());
        }

        String[] appIdTokens = kuraChannel.getAppId().split("-");

        if (!PackageMetrics.APP_ID.getValue().equals(appIdTokens[0])) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_NAME, null, appIdTokens[0]);
        }

        if (!PackageMetrics.APP_VERSION.getValue().equals(appIdTokens[1])) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_VERSION, null, appIdTokens[1]);
        }

            PackageResponseChannel responseChannel = new PackageResponseChannel();
        responseChannel.setAppName(PackageAppProperties.APP_NAME);
        responseChannel.setVersion(PackageAppProperties.APP_VERSION);

        // Return Kapua Channel
        return responseChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraChannel);
        }
    }

    @Override
    protected PackageResponsePayload translatePayload(KuraResponsePayload kuraPayload) throws InvalidPayloadException {
        try {
        PackageResponsePayload responsePayload = new PackageResponsePayload();

            Map<String, Object> metrics = kuraPayload.getMetrics();
        responsePayload.setExceptionMessage((String) metrics.get(KuraResponseMetrics.EXCEPTION_MESSAGE.getName()));
        responsePayload.setExceptionStack((String) metrics.get(KuraResponseMetrics.EXCEPTION_STACK.getName()));

        KuraResponseCode responseCode = KuraResponseCode.fromResponseCode((Integer) metrics.get(KuraResponseMetrics.EXIT_CODE.getName()));

        if (!KuraResponseCode.INTERNAL_ERROR.equals(responseCode)) {
            if (metrics.get(PackageMetrics.APP_METRIC_PACKAGE_OPERATION_ID.getValue()) != null) {
                responsePayload.setPackageDownloadOperationId(new KapuaEid(new BigInteger(metrics.get(PackageMetrics.APP_METRIC_PACKAGE_OPERATION_ID.getValue()).toString())));

                if (metrics.get(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_STATUS.getValue()) != null) {
                    DevicePackageDownloadStatus status;

                    String kuraStatus = (String) metrics.get(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_STATUS.getValue());
                    switch (kuraStatus) {
                        case "IN_PROGRESS":
                            status = DevicePackageDownloadStatus.IN_PROGRESS;
                            break;
                        case "FAILED":
                            status = DevicePackageDownloadStatus.FAILED;
                            break;
                        case "COMPLETED":
                        case "ALREADY DONE":
                            status = DevicePackageDownloadStatus.COMPLETED;
                            break;
                        default:
                            throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, null, kuraStatus);
                    }
                    responsePayload.setPackageDownloadOperationStatus(status);
                }

                responsePayload.setPackageDownloadOperationSize((Integer) metrics.get(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_SIZE.getValue()));
                responsePayload.setPackageDownloadOperationProgress((Integer) metrics.get(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PROGRESS.getValue()));
            } else {
                responsePayload.setPackageDownloadOperationStatus(DevicePackageDownloadStatus.NONE);
            }

            String body;
            if (kuraPayload.hasBody()) {
                DeviceManagementSetting config = DeviceManagementSetting.getInstance();
                String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

                try {
                        body = new String(kuraPayload.getBody(), charEncoding);
                } catch (Exception e) {
                        throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, (Object) kuraPayload.getBody());
                }

                KuraDeploymentPackages kuraDeploymentPackages = null;
                try {
                    kuraDeploymentPackages = XmlUtil.unmarshal(body, KuraDeploymentPackages.class);
                } catch (Exception e) {
                        throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, body);
                }
                translate(responsePayload, charEncoding, kuraDeploymentPackages);
            }
        } else {
            if (kuraPayload.hasBody()) {
                String errorMessage = new String(kuraPayload.getBody());

                responsePayload.setExceptionMessage(errorMessage);
            }
        }

        // Return Kapua Payload
        return responsePayload;
        } catch (InvalidPayloadException ipe) {
            throw ipe;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kuraPayload);
    }
    }

    private void translate(PackageResponsePayload packageResponsePayload, String charEncoding, KuraDeploymentPackages kuraDeploymentPackages) throws KapuaException {
        try {
            DevicePackageFactory devicePackageFactory = LOCATOR.getFactory(DevicePackageFactory.class);

            KuraDeploymentPackage[] deploymentPackageArray = kuraDeploymentPackages.getDeploymentPackages();
            if (deploymentPackageArray != null) {
                DevicePackages deviceDeploymentPackages = devicePackageFactory.newDeviceDeploymentPackages();

                for (KuraDeploymentPackage deploymentPackage : deploymentPackageArray) {
                    DevicePackage deviceDeploymentPackage = devicePackageFactory.newDeviceDeploymentPackage();
                    deviceDeploymentPackage.setName(deploymentPackage.getName());
                    deviceDeploymentPackage.setVersion(deploymentPackage.getVersion());

                    DevicePackageBundleInfos devicePackageBundleInfos = deviceDeploymentPackage.getBundleInfos();
                    KuraBundleInfo[] bundleInfoArray = deploymentPackage.getBundleInfos();
                    for (KuraBundleInfo bundleInfo : bundleInfoArray) {
                        DevicePackageBundleInfo devicePackageBundleInfo = devicePackageFactory.newDevicePackageBundleInfo();
                        devicePackageBundleInfo.setName(bundleInfo.getName());
                        devicePackageBundleInfo.setVersion(bundleInfo.getVersion());

                        // Add the new DevicePackageBundleInfo object to the corresponding list
                        devicePackageBundleInfos.getBundlesInfos().add(devicePackageBundleInfo);
                    }

                    // Add the new DeviceDeploymentPackage object to the corresponding list
                    deviceDeploymentPackages.getPackages().add(deviceDeploymentPackage);
                }

                StringWriter sw = new StringWriter();
                XmlUtil.marshal(deviceDeploymentPackages, sw);
                byte[] requestBody = sw.toString().getBytes(charEncoding);

                packageResponsePayload.setBody(requestBody);
            }
        } catch (Exception e) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_BODY, e, kuraDeploymentPackages);
        }
    }
}
