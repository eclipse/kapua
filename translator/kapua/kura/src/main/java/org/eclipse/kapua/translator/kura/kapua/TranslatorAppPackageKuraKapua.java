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
package org.eclipse.kapua.translator.kura.kapua;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.kura.app.PackageMetrics;
import org.eclipse.kapua.service.device.call.kura.app.ResponseMetrics;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraBundleInfo;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraDeploymentPackage;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraDeploymentPackages;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
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
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

/**
 * Messages translator implementation from {@link KuraResponseMessage} to {@link PackageResponseMessage}
 *
 * @since 1.0
 *
 */
public class TranslatorAppPackageKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<PackageResponseChannel, PackageResponsePayload, PackageResponseMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();
    private static final Map<PackageMetrics, PackageAppProperties> METRICS_DICTIONARY;

    static {
        METRICS_DICTIONARY = new HashMap<>();

        METRICS_DICTIONARY.put(PackageMetrics.APP_ID, PackageAppProperties.APP_NAME);
        METRICS_DICTIONARY.put(PackageMetrics.APP_VERSION, PackageAppProperties.APP_VERSION);
    }

    /**
     * Constructor
     */
    public TranslatorAppPackageKuraKapua() {
        super(PackageResponseMessage.class);
    }

    protected PackageResponseChannel translateChannel(KuraResponseChannel kuraChannel) throws KapuaException {

        if (!CONTROL_MESSAGE_CLASSIFIER.equals(kuraChannel.getMessageClassification())) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER,
                    null,
                    kuraChannel.getMessageClassification());
        }

        PackageResponseChannel responseChannel = new PackageResponseChannel();

        String[] appIdTokens = kuraChannel.getAppId().split("-");

        if (!PackageMetrics.APP_ID.getValue().equals(appIdTokens[0])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_NAME,
                    null,
                    appIdTokens[0]);
        }

        if (!PackageMetrics.APP_VERSION.getValue().equals(appIdTokens[1])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_VERSION,
                    null,
                    appIdTokens[1]);
        }

        responseChannel.setAppName(PackageAppProperties.APP_NAME);
        responseChannel.setVersion(PackageAppProperties.APP_VERSION);

        //
        // Return Kapua Channel
        return responseChannel;
    }

    protected PackageResponsePayload translatePayload(KuraResponsePayload kuraResponsePayload) throws KapuaException {
        PackageResponsePayload responsePayload = new PackageResponsePayload();

        Map<String, Object> metrics = kuraResponsePayload.getMetrics();
        responsePayload.setExceptionMessage((String) metrics.get(ResponseMetrics.RESP_METRIC_EXCEPTION_MESSAGE.getValue()));
        responsePayload.setExceptionStack((String) metrics.get(ResponseMetrics.RESP_METRIC_EXCEPTION_STACK.getValue()));

        if (metrics.get(PackageMetrics.APP_METRIC_PACKAGE_OPERATION_ID.getValue()) != null) {
            responsePayload.setPackageDownloadOperationId(new KapuaEid(new BigInteger(metrics.get(PackageMetrics.APP_METRIC_PACKAGE_OPERATION_ID.getValue()).toString())));
        }

        if (metrics.get(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_STATUS.getValue()) != null) {
            responsePayload.setPackageDownloadOperationStatus(DevicePackageDownloadStatus.valueOf((String) metrics.get(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_STATUS.getValue())));
        }
        responsePayload.setPackageDownloadOperationSize((Integer) metrics.get(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_SIZE.getValue()));
        responsePayload.setPackageDownloadOperationProgress((Integer) metrics.get(PackageMetrics.APP_METRIC_PACKAGE_DOWNLOAD_PROGRESS.getValue()));

        String body;
        if (kuraResponsePayload.getBody() != null) {
            DeviceManagementSetting config = DeviceManagementSetting.getInstance();
            String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

            try {
                body = new String(kuraResponsePayload.getBody(), charEncoding);
            } catch (Exception e) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, //
                        e,//
                        kuraResponsePayload.getBody());//
            }

            KuraDeploymentPackages kuraDeploymentPackages = null;
            try {
                kuraDeploymentPackages = XmlUtil.unmarshal(body, KuraDeploymentPackages.class);
            } catch (Exception e) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD,
                        e,
                        body);
            }
            translate(responsePayload, charEncoding, kuraDeploymentPackages);
        }

        //
        // Return Kapua Payload
        return responsePayload;
    }

    private void translate(PackageResponsePayload packageResponsePayload,
            String charEncoding,
            KuraDeploymentPackages kuraDeploymentPackages)
            throws KapuaException {
        try {

            KuraDeploymentPackage[] deploymentPackageArray = kuraDeploymentPackages.getDeploymentPackages();
            if (deploymentPackageArray != null) {
                KapuaLocator locator = KapuaLocator.getInstance();
                DevicePackageFactory deviceDeploymentFactory = locator.getFactory(DevicePackageFactory.class);
                DevicePackages deviceDeploymentPackages = deviceDeploymentFactory.newDeviceDeploymentPackages();

                for (KuraDeploymentPackage deploymentPackage : deploymentPackageArray) {
                    DevicePackage deviceDeploymentPackage = deviceDeploymentFactory.newDeviceDeploymentPackage();
                    deviceDeploymentPackage.setName(deploymentPackage.getName());
                    deviceDeploymentPackage.setVersion(deploymentPackage.getVersion());

                    DevicePackageBundleInfos devicePackageBundleInfos = deviceDeploymentPackage.getBundleInfos();
                    KuraBundleInfo[] bundleInfoArray = deploymentPackage.getBundleInfos();
                    for (KuraBundleInfo bundleInfo : bundleInfoArray) {
                        DevicePackageBundleInfo devicePackageBundleInfo = deviceDeploymentFactory.newDevicePackageBundleInfo();
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
            throw new TranslatorException(TranslatorErrorCodes.INVALID_BODY,
                    e,
                    kuraDeploymentPackages);
        }
    }
}
