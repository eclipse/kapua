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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.kura.app.AssetMetrics;
import org.eclipse.kapua.service.device.call.kura.app.ResponseMetrics;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSetting;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettingKeys;
import org.eclipse.kapua.service.device.management.asset.internal.DeviceAssetAppProperties;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseChannel;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseMessage;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponsePayload;
import org.eclipse.kapua.service.device.management.channel.message.internal.ChannelResponseChannel;
import org.eclipse.kapua.service.device.management.channel.message.internal.ChannelResponseMessage;
import org.eclipse.kapua.service.device.management.channel.message.internal.ChannelResponsePayload;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

/**
 * Messages translator implementation from {@link KuraResponseMessage} to {@link ChannelResponseMessage}
 *
 * @since 1.0
 *
 */
public class TranslatorAppAssetChannelKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<ChannelResponseChannel, ChannelResponsePayload, ChannelResponseMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = DeviceCallSetting.getInstance().getString(DeviceCallSettingKeys.DESTINATION_MESSAGE_CLASSIFIER);
    private static final Map<AssetMetrics, DeviceAssetAppProperties> metricsDictionary;
    
    static {
        metricsDictionary = new HashMap<>();

        metricsDictionary.put(AssetMetrics.APP_ID, DeviceAssetAppProperties.APP_NAME);
        metricsDictionary.put(AssetMetrics.APP_VERSION, DeviceAssetAppProperties.APP_VERSION);
    }
    
    
    public TranslatorAppAssetChannelKuraKapua() {
        super(ChannelResponseMessage.class);
    }
    
    protected ChannelResponseChannel translateChannel(KuraResponseChannel kuraChannel) throws KapuaException {

        if (!CONTROL_MESSAGE_CLASSIFIER.equals(kuraChannel.getMessageClassification())) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER,
                    null,
                    kuraChannel.getMessageClassification());
        }

        ChannelResponseChannel channelResponseChannel = new ChannelResponseChannel();

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

        channelResponseChannel.setAppName(DeviceAssetAppProperties.APP_NAME);
        channelResponseChannel.setVersion(DeviceAssetAppProperties.APP_VERSION);

        //
        // Return Kapua Channel
        return channelResponseChannel;
    }

    protected ChannelResponsePayload translatePayload(KuraResponsePayload kuraPayload) throws KapuaException {
        ChannelResponsePayload assetResponsePayload = new ChannelResponsePayload();

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
