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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.kura.app.AssetMetrics;
import org.eclipse.kapua.service.device.call.kura.model.asset.KuraAssetChannelMode;
import org.eclipse.kapua.service.device.call.kura.model.asset.KuraAssets;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannelMode;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.asset.internal.DeviceAssetAppProperties;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseChannel;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseMessage;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponsePayload;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

import java.util.Date;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link KuraResponseMessage} to {@link AssetResponseMessage}
 *
 * @since 1.0.0
 */
public class TranslatorAppAssetKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<AssetResponseChannel, AssetResponsePayload, AssetResponseMessage> {

    public TranslatorAppAssetKuraKapua() {
        super(AssetResponseMessage.class);
    }

    @Override
    protected AssetResponseChannel translateChannel(KuraResponseChannel kuraChannel) throws InvalidChannelException {
        try {
            if (!getControlMessageClassifier().equals(kuraChannel.getMessageClassification())) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER, null, kuraChannel.getMessageClassification());
        }

        String[] appIdTokens = kuraChannel.getAppId().split("-");

        if (!AssetMetrics.APP_ID.getValue().equals(appIdTokens[0])) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_NAME, null, appIdTokens[0]);
        }

        if (!AssetMetrics.APP_VERSION.getValue().equals(appIdTokens[1])) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_VERSION, null, appIdTokens[1]);
        }

            AssetResponseChannel assetResponseChannel = new AssetResponseChannel();
        assetResponseChannel.setAppName(DeviceAssetAppProperties.APP_NAME);
        assetResponseChannel.setVersion(DeviceAssetAppProperties.APP_VERSION);

        // Return Kapua Channel
        return assetResponseChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraChannel);
    }
    }

    @Override
    protected AssetResponsePayload translatePayload(KuraResponsePayload kuraPayload) throws InvalidPayloadException {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceAssetFactory deviceAssetFactory = locator.getFactory(DeviceAssetFactory.class);

        try {
        AssetResponsePayload assetResponsePayload = new AssetResponsePayload();

        assetResponsePayload.setExceptionMessage((String) kuraPayload.getMetrics().get(KuraResponseMetrics.EXCEPTION_MESSAGE.getName()));
        assetResponsePayload.setExceptionStack((String) kuraPayload.getMetrics().get(KuraResponseMetrics.EXCEPTION_STACK.getName()));

        if (kuraPayload.hasBody()) {

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(kuraPayload.getBody());
                DeviceAssets deviceAssets = deviceAssetFactory.newAssetListResult();
                KuraAssets kuraAssets = KuraAssets.readJsonNode(jsonNode);

                kuraAssets.getAssets().forEach(kuraAsset -> {
                    DeviceAsset deviceAsset = deviceAssetFactory.newDeviceAsset();
                    deviceAsset.setName(kuraAsset.getName());

                    kuraAsset.getChannels().forEach(kuraAssetChannel -> {
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
                    });

                    deviceAssets.getAssets().add(deviceAsset);
                });

                assetResponsePayload.setBody(XmlUtil.marshal(deviceAssets).getBytes());
            }

        // Return Kapua Payload
        return assetResponsePayload;
        } catch (InvalidPayloadException ipe) {
            throw ipe;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kuraPayload);
        }
    }
}
