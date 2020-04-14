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
    public MqttMessage translate(KuraRequestMessage kuraRequestMessage) throws TranslateException {
        try {
            // Mqtt request topic
            MqttTopic mqttRequestTopic = translate(kuraRequestMessage.getChannel());

            // Mqtt response topic
            MqttTopic mqttResponseTopic = generateResponseTopic(kuraRequestMessage.getChannel());

            // Mqtt payload
            MqttPayload mqttPayload = translate(kuraRequestMessage.getPayload());

            // Return Mqtt message
            return new MqttMessage(mqttRequestTopic, mqttResponseTopic, mqttPayload);
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, kuraRequestMessage);
        }
    }

    public MqttTopic translate(KuraRequestChannel kuraRequestChannel) throws InvalidChannelException {
        try {
            List<String> topicTokens = new ArrayList<>();

            if (kuraRequestChannel.getMessageClassification() != null) {
                topicTokens.add(kuraRequestChannel.getMessageClassification());
            }

            topicTokens.add(kuraRequestChannel.getScope());
            topicTokens.add(kuraRequestChannel.getClientId());
            topicTokens.add(kuraRequestChannel.getAppId());
            topicTokens.add(kuraRequestChannel.getMethod().name());

            Collections.addAll(topicTokens, kuraRequestChannel.getResources());

            // Return Mqtt Topic
            return new MqttTopic(topicTokens.toArray(new String[0]));
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraRequestChannel);
        }
    }

    private MqttTopic generateResponseTopic(KuraRequestChannel kuraRequestChannel) throws InvalidChannelException {
        try {
            List<String> topicTokens = new ArrayList<>();

            if (kuraRequestChannel.getMessageClassification() != null) {
                topicTokens.add(kuraRequestChannel.getMessageClassification());
            }

            topicTokens.add(kuraRequestChannel.getScope());
            topicTokens.add(kuraRequestChannel.getRequesterClientId());
            topicTokens.add(kuraRequestChannel.getAppId());
            topicTokens.add(REPLY_PART);
            topicTokens.add(kuraRequestChannel.getRequestId());

            return new MqttTopic(topicTokens.toArray(new String[0]));
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraRequestChannel);
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
