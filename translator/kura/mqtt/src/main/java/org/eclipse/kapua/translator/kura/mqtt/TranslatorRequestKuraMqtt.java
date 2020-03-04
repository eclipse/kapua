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
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.mqtt;

import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettingKeys;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettings;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link Translator} implementation from {@link KuraRequestMessage} to {@link MqttMessage}
 *
 * @since 1.0.0
 */
public class TranslatorRequestKuraMqtt extends Translator<KuraRequestMessage, MqttMessage> {

    private static final String REPLY_PART = DeviceCallSettings.getInstance().getString(DeviceCallSettingKeys.DESTINATION_REPLY_PART);

    @Override
    public MqttMessage translate(KuraRequestMessage kuraMessage) throws TranslateException {
        try {
            // Mqtt request topic
            MqttTopic mqttRequestTopic = translate(kuraMessage.getChannel());

            // Mqtt response topic
            MqttTopic mqttResponseTopic = generateResponseTopic(kuraMessage.getChannel());

            // Mqtt payload
            MqttPayload mqttPayload = translate(kuraMessage.getPayload());

            // Return Mqtt message
            return new MqttMessage(mqttRequestTopic, mqttResponseTopic, mqttPayload);
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, kuraMessage);
        }
    }

    public MqttTopic translate(KuraRequestChannel kuraChannel) throws InvalidChannelException {
        try {
            List<String> topicTokens = new ArrayList<>();

            if (kuraChannel.getMessageClassification() != null) {
                topicTokens.add(kuraChannel.getMessageClassification());
            }

            topicTokens.add(kuraChannel.getScope());
            topicTokens.add(kuraChannel.getClientId());
            topicTokens.add(kuraChannel.getAppId());
            topicTokens.add(kuraChannel.getMethod().name());

            Collections.addAll(topicTokens, kuraChannel.getResources());

            // Return Mqtt Topic
            return new MqttTopic(topicTokens.toArray(new String[0]));
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraChannel);
        }
    }

    private MqttTopic generateResponseTopic(KuraRequestChannel kuraChannel) throws InvalidChannelException {
        try {
            List<String> topicTokens = new ArrayList<>();

            if (kuraChannel.getMessageClassification() != null) {
                topicTokens.add(kuraChannel.getMessageClassification());
            }

            topicTokens.add(kuraChannel.getScope());
            topicTokens.add(kuraChannel.getRequesterClientId());
            topicTokens.add(kuraChannel.getAppId());
            topicTokens.add(REPLY_PART);
            topicTokens.add(kuraChannel.getRequestId());

            return new MqttTopic(topicTokens.toArray(new String[0]));
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraChannel);
        }
    }

    private MqttPayload translate(KuraPayload kuraPayload) throws InvalidPayloadException {
        try {
            return new MqttPayload(kuraPayload.toByteArray());
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kuraPayload);
        }
    }

    @Override
    public Class<KuraRequestMessage> getClassFrom() {
        return KuraRequestMessage.class;
    }

    @Override
    public Class<MqttMessage> getClassTo() {
        return MqttMessage.class;
    }
}
