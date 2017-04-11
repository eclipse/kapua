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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.kura.app.AssetMetrics;
import org.eclipse.kapua.service.device.call.kura.app.ResponseMetrics;
import org.eclipse.kapua.service.device.call.kura.model.asset.KuraAsset;
import org.eclipse.kapua.service.device.call.kura.model.asset.KuraAssets;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSetting;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettingKeys;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.asset.internal.DeviceAssetAppProperties;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseChannel;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseMessage;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponsePayload;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

/**
 * Messages translator implementation from {@link KuraResponseMessage} to {@link AssetResponseMessage}
 *
 * @since 1.0
 *
 */
public class TranslatorAppAssetKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<AssetResponseChannel, AssetResponsePayload, AssetResponseMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = DeviceCallSetting.getInstance().getString(DeviceCallSettingKeys.DESTINATION_MESSAGE_CLASSIFIER);
    private static final Map<AssetMetrics, DeviceAssetAppProperties> metricsDictionary;
    
    static {
        metricsDictionary = new HashMap<>();

        metricsDictionary.put(AssetMetrics.APP_ID, DeviceAssetAppProperties.APP_NAME);
        metricsDictionary.put(AssetMetrics.APP_VERSION, DeviceAssetAppProperties.APP_VERSION);
    }
    
    
    public TranslatorAppAssetKuraKapua() {
        super(AssetResponseMessage.class);
    }
    
    protected AssetResponseChannel translateChannel(KuraResponseChannel kuraChannel) throws KapuaException {

        if (!CONTROL_MESSAGE_CLASSIFIER.equals(kuraChannel.getMessageClassification())) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER,
                    null,
                    kuraChannel.getMessageClassification());
        }

        AssetResponseChannel assetResponseChannel = new AssetResponseChannel();

        String[] appIdTokens = kuraChannel.getAppId().split("-");

        if (!AssetMetrics.APP_ID.getValue().equals(appIdTokens[0])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_NAME,
                    null,
                    appIdTokens[0]);
        }

        if (!AssetMetrics.APP_VERSION.getValue().equals(appIdTokens[1])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_VERSION,
                    null,
                    appIdTokens[1]);
        }

        assetResponseChannel.setAppName(DeviceAssetAppProperties.APP_NAME);
        assetResponseChannel.setVersion(DeviceAssetAppProperties.APP_VERSION);

        //
        // Return Kapua Channel
        return assetResponseChannel;
    }

    protected AssetResponsePayload translatePayload(KuraResponsePayload kuraPayload) throws KapuaException {
        AssetResponsePayload assetResponsePayload = new AssetResponsePayload();

        assetResponsePayload.setExceptionMessage((String) kuraPayload.getMetrics().get(ResponseMetrics.RESP_METRIC_EXCEPTION_MESSAGE.getValue()));
        assetResponsePayload.setExceptionStack((String) kuraPayload.getMetrics().get(ResponseMetrics.RESP_METRIC_EXCEPTION_STACK.getValue()));

        if (kuraPayload.getMetrics() != null && !kuraPayload.getMetrics().isEmpty()) {
            Map<String, Object> payloadProperties = assetResponsePayload.getProperties();
            
            for (Entry<String, Object> kuraMetric : kuraPayload.getMetrics().entrySet()) {
                if (!kuraMetric.getKey().equals(ResponseMetrics.RESP_METRIC_EXIT_CODE.getValue())) {
                    payloadProperties.put(kuraMetric.getKey(), kuraMetric.getValue());                    
                }
            }
        }

        //
        // Return Kapua Payload
        return assetResponsePayload;
    }
}
