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
 *******************************************************************************/
package org.eclipse.kapua.translator.kapua.kura;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.device.call.kura.app.AssetMetrics;
import org.eclipse.kapua.service.device.call.kura.model.asset.KuraAsset;
import org.eclipse.kapua.service.device.call.kura.model.asset.KuraAssetChannel;
import org.eclipse.kapua.service.device.call.kura.model.asset.KuraAssets;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestPayload;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.asset.internal.DeviceAssetAppProperties;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestChannel;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestMessage;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestPayload;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Messages translator implementation from {@link AssetRequestMessage} to {@link KuraRequestMessage}
 * 
 * @since 1.0.0
 */
public class TranslatorAppAssetKapuaKura extends AbstractTranslatorKapuaKura<AssetRequestChannel, AssetRequestPayload, AssetRequestMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();
    private static final Map<DeviceAssetAppProperties, AssetMetrics> PROPERTIES_DICTIONARY = new HashMap<>();

    static {
        PROPERTIES_DICTIONARY.put(DeviceAssetAppProperties.APP_NAME, AssetMetrics.APP_ID);
        PROPERTIES_DICTIONARY.put(DeviceAssetAppProperties.APP_VERSION, AssetMetrics.APP_VERSION);
    }

    protected KuraRequestChannel translateChannel(AssetRequestChannel kapuaChannel) throws KapuaException {
        KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
        kuraRequestChannel.setMessageClassification(CONTROL_MESSAGE_CLASSIFIER);

        // Build appId
        StringBuilder appIdSb = new StringBuilder();
        appIdSb.append(PROPERTIES_DICTIONARY.get(DeviceAssetAppProperties.APP_NAME).getValue())
                .append("-")
                .append(PROPERTIES_DICTIONARY.get(DeviceAssetAppProperties.APP_VERSION).getValue());

        kuraRequestChannel.setAppId(appIdSb.toString());
        kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.get(kapuaChannel.getMethod()));

        // Build resources
        String[] resources;

        if (kapuaChannel.isRead()) {
            resources = new String[] { "read" };
        } else if (kapuaChannel.isWrite()) {
            resources = new String[] { "write" };
        } else {
            resources = new String[] { "assets" };
        }

        kuraRequestChannel.setResources(resources);

        //
        // Return Kura Channel
        return kuraRequestChannel;
    }

    protected KuraRequestPayload translatePayload(AssetRequestPayload kapuaPayload) throws KapuaException {

        DeviceAssets deviceAssets;
        try {
            deviceAssets = kapuaPayload.getDeviceAssets();
        } catch (UnsupportedEncodingException | JAXBException | XMLStreamException | FactoryConfigurationError | SAXException e) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, kapuaPayload);
        }

        if (deviceAssets.getAssets().isEmpty()) {
            return new KuraRequestPayload();
        }

        KuraAssets kuraAssets = new KuraAssets();
        for (DeviceAsset deviceAsset : deviceAssets.getAssets()) {
            KuraAsset kuraAsset = new KuraAsset();
            kuraAsset.setName(deviceAsset.getName());

            for (DeviceAssetChannel deviceAssetChannel : deviceAsset.getChannels()) {
                KuraAssetChannel kuraAssetChannel = new KuraAssetChannel();
                kuraAssetChannel.setName(deviceAssetChannel.getName());
                kuraAssetChannel.setType(deviceAssetChannel.getType());
                kuraAssetChannel.setValue(deviceAssetChannel.getValue());

                Date timestamp = deviceAssetChannel.getTimestamp();
                kuraAssetChannel.setTimestamp(timestamp != null ? timestamp.getTime() : null);

                kuraAsset.getChannels().add(kuraAssetChannel);
            }

            kuraAssets.getAssets().add(kuraAsset);
        }

        KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();
        try {
            StringWriter sw = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(sw);

            kuraAssets.writeJsonNode(jsonGenerator);

            jsonGenerator.close();
            kuraRequestPayload.setBody(sw.toString().getBytes());
        } catch (IOException e) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, kapuaPayload);
        }

        return kuraRequestPayload;
    }

    @Override
    public Class<AssetRequestMessage> getClassFrom() {
        return AssetRequestMessage.class;
    }

    @Override
    public Class<KuraRequestMessage> getClassTo() {
        return KuraRequestMessage.class;
    }

}
