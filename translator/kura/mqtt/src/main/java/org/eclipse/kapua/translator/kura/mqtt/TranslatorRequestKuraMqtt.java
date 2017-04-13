/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.mqtt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSetting;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettingKeys;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;

/**
 * Messages translator implementation from {@link org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage} to {@link org.eclipse.kapua.transport.message.mqtt.MqttMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorRequestKuraMqtt extends Translator<KuraRequestMessage, MqttMessage>
{
    @Override
    public MqttMessage translate(KuraRequestMessage kuraMessage)
        throws KapuaException
    {
        //
        // Mqtt request topic
        MqttTopic mqttRequestTopic = translate(kuraMessage.getChannel());

        //
        // Mqtt response topic

        MqttTopic mqttResponseTopic = generateResponseTopic(kuraMessage.getChannel());

        //
        // Mqtt payload
        MqttPayload mqttPayload = translate(kuraMessage.getPayload());

        //
        // Return Mqtt message
        return new MqttMessage(mqttRequestTopic,
                               mqttResponseTopic,
                               mqttPayload);
    }

    public MqttTopic translate(KuraRequestChannel kuraChannel)
        throws KapuaException
    {
        List<String> topicTokens = new ArrayList<>();

        if (kuraChannel.getMessageClassification() != null) {
            topicTokens.add(kuraChannel.getMessageClassification());
        }

        topicTokens.add(kuraChannel.getScope());
        topicTokens.add(kuraChannel.getClientId());
        topicTokens.add(kuraChannel.getAppId());
        topicTokens.add(kuraChannel.getMethod().name());

        for (String s : kuraChannel.getResources()) {
            topicTokens.add(s);
        }

        //
        // Return Mqtt Topic
        return new MqttTopic(topicTokens.toArray(new String[0]));
    }

    private MqttTopic generateResponseTopic(KuraRequestChannel kuraChannel)
    {
        String replyPart = DeviceCallSetting.getInstance().getString(DeviceCallSettingKeys.DESTINATION_REPLY_PART);

        List<String> topicTokens = new ArrayList<>();

        if (kuraChannel.getMessageClassification() != null) {
            topicTokens.add(kuraChannel.getMessageClassification());
        }

        topicTokens.add(kuraChannel.getScope());
        topicTokens.add(kuraChannel.getRequesterClientId());
        topicTokens.add(kuraChannel.getAppId());
        topicTokens.add(replyPart);
        topicTokens.add(kuraChannel.getRequestId());

        return new MqttTopic(topicTokens.toArray(new String[0]));
    }

    private MqttPayload translate(KuraPayload kuraPayload)
        throws KapuaException
    {
        return new MqttPayload(kuraPayload.toByteArray());
    }

    @Override
    public Class<KuraRequestMessage> getClassFrom()
    {
        return KuraRequestMessage.class;
    }

    @Override
    public Class<MqttMessage> getClassTo()
    {
        return MqttMessage.class;
    }
}
