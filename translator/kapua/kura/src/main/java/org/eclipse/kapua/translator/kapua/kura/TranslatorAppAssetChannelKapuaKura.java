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
 *
 *******************************************************************************/
package org.eclipse.kapua.translator.kapua.kura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.kura.app.AssetMetrics;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestPayload;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSetting;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettingKeys;
import org.eclipse.kapua.service.device.management.asset.internal.DeviceAssetAppProperties;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestChannel;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestMessage;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestPayload;
import org.eclipse.kapua.service.device.management.channel.message.internal.ChannelRequestChannel;
import org.eclipse.kapua.service.device.management.channel.message.internal.ChannelRequestMessage;
import org.eclipse.kapua.service.device.management.channel.message.internal.ChannelRequestPayload;

import com.google.common.base.Strings;

/**
 * Messages translator implementation from {@link AssetRequestMessage} to {@link KuraRequestMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorAppAssetChannelKapuaKura extends AbstractTranslatorKapuaKura<ChannelRequestChannel, ChannelRequestPayload, ChannelRequestMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = DeviceCallSetting.getInstance().getString(DeviceCallSettingKeys.DESTINATION_MESSAGE_CLASSIFIER);
    private static final Map<DeviceAssetAppProperties, AssetMetrics> propertiesDictionary= new HashMap<>();
    
    static {
        propertiesDictionary.put(DeviceAssetAppProperties.APP_NAME, AssetMetrics.APP_ID);
        propertiesDictionary.put(DeviceAssetAppProperties.APP_VERSION, AssetMetrics.APP_VERSION);
    }


    protected KuraRequestChannel translateChannel(ChannelRequestChannel kapuaChannel) throws KapuaException {
        KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
        kuraRequestChannel.setMessageClassification(CONTROL_MESSAGE_CLASSIFIER);

        // Build appId
        StringBuilder appIdSb = new StringBuilder();
        appIdSb.append(propertiesDictionary.get(DeviceAssetAppProperties.APP_NAME).getValue())
                .append("-")
                .append(propertiesDictionary.get(DeviceAssetAppProperties.APP_VERSION).getValue());

        kuraRequestChannel.setAppId(appIdSb.toString());
        kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.get(kapuaChannel.getMethod()));

        // Build resources
        kuraRequestChannel.setResources(new String[]{"assets", kapuaChannel.getAssetName()});

        //
        // Return Kura Channel
        return kuraRequestChannel;
    }

    protected KuraRequestPayload translatePayload(ChannelRequestPayload kapuaPayload) throws KapuaException {
        KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

        if (kapuaPayload.getBody() != null) {
            kuraRequestPayload.setBody(kapuaPayload.getBody());
        }

        return kuraRequestPayload;
    }

    @Override
    public Class<ChannelRequestMessage> getClassFrom() {
        return ChannelRequestMessage.class;
    }

    @Override
    public Class<KuraRequestMessage> getClassTo() {
        return KuraRequestMessage.class;
    }

}
