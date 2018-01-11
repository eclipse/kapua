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

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.kura.app.AssetMetrics;
import org.eclipse.kapua.service.device.call.kura.app.ResponseMetrics;
import org.eclipse.kapua.service.device.call.kura.model.asset.KuraAsset;
import org.eclipse.kapua.service.device.call.kura.model.asset.KuraAssetChannel;
import org.eclipse.kapua.service.device.call.kura.model.asset.KuraAssetChannelMode;
import org.eclipse.kapua.service.device.call.kura.model.asset.KuraAssets;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannelMode;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.asset.internal.DeviceAssetAppProperties;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseChannel;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseMessage;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponsePayload;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Messages translator implementation from {@link KuraResponseMessage} to {@link AssetResponseMessage}
 *
 * @since 1.0
 *
 */
public class TranslatorAppAssetKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<AssetResponseChannel, AssetResponsePayload, AssetResponseMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();
    private static final Map<AssetMetrics, DeviceAssetAppProperties> METRICS_DICTIONARY;

    static {
        METRICS_DICTIONARY = new HashMap<>();

        METRICS_DICTIONARY.put(AssetMetrics.APP_ID, DeviceAssetAppProperties.APP_NAME);
        METRICS_DICTIONARY.put(AssetMetrics.APP_VERSION, DeviceAssetAppProperties.APP_VERSION);
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

        if (kuraPayload.getBody() != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();

                JsonNode jsonNode = mapper.readTree(kuraPayload.getBody());

                KapuaLocator locator = KapuaLocator.getInstance();
                DeviceAssetFactory deviceAssetFactory = locator.getFactory(DeviceAssetFactory.class);
                DeviceAssets deviceAssets = deviceAssetFactory.newAssetListResult();
                KuraAssets kuraAssets = KuraAssets.readJsonNode(jsonNode);
                for (KuraAsset kuraAsset : kuraAssets.getAssets()) {
                    DeviceAsset deviceAsset = deviceAssetFactory.newDeviceAsset();
                    deviceAsset.setName(kuraAsset.getName());

                    for (KuraAssetChannel kuraAssetChannel : kuraAsset.getChannels()) {
                        DeviceAssetChannel deviceAssetChannel = deviceAssetFactory.newDeviceAssetChannel();

                        deviceAssetChannel.setName(kuraAssetChannel.getName());
                        KuraAssetChannelMode kuraChannelMode = kuraAssetChannel.getMode();
                        if (kuraChannelMode != null) {
                            deviceAssetChannel.setMode(DeviceAssetChannelMode.valueOf(kuraChannelMode.name()));
                        }
                        deviceAssetChannel.setType(kuraAssetChannel.getType());
                        deviceAssetChannel.setValue(kuraAssetChannel.getValue());
                        Long kuraTimestamp = kuraAssetChannel.getTimestamp();
                        if (kuraTimestamp != null && kuraTimestamp > 0) {
                            deviceAssetChannel.setTimestamp(new Date(kuraAssetChannel.getTimestamp()));
                        }
                        deviceAssetChannel.setError(kuraAssetChannel.getError());

                        deviceAsset.getChannels().add(deviceAssetChannel);
                    }

                    deviceAssets.getAssets().add(deviceAsset);
                }

                assetResponsePayload.setBody(XmlUtil.marshal(deviceAssets).getBytes());

            } catch (KapuaException | JAXBException e) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, kuraPayload);
            } catch (IOException e) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_BODY, e, kuraPayload.getBody());
            }
        }
        //
        // Return Kapua Payload
        return assetResponsePayload;
    }
}
