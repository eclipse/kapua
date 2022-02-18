/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.mqtt.kura;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;

/**
 * {@link Translator} implementation from {@link MqttMessage} to {@link KuraResponseMessage}
 *
 * @since 1.0.0
 */
public class TranslatorResponseMqttKura extends Translator<MqttMessage, KuraResponseMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();

    @Override
    public KuraResponseMessage translate(MqttMessage mqttMessage) throws TranslateException {
        try {
            // Kura topic
            KuraResponseChannel kuraChannel = translate(mqttMessage.getRequestTopic());

            // Kura payload
            KuraResponsePayload kuraPayload = translate(mqttMessage.getPayload());

            // Kura message
            return new KuraResponseMessage(kuraChannel, mqttMessage.getTimestamp(), kuraPayload);
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, mqttMessage);
        }
    }

    /**
     * Translates the given {@link MqttTopic} to the {@link KuraResponseChannel} equivalent.
     *
     * @param mqttTopic The {@link MqttTopic} to translate.
     * @return The translated {@link KuraResponseChannel}
     * @throws InvalidChannelException if translation encounters any error (i.e.: not enough {@link MqttTopic#getSplittedTopic()} tokens.
     * @since 1.0.0
     */
    public KuraResponseChannel translate(MqttTopic mqttTopic) throws InvalidChannelException {
        try {
            String[] mqttTopicTokens = mqttTopic.getSplittedTopic();

            if (mqttTopicTokens.length != 6) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL, null, (Object) mqttTopicTokens);
            }

            if (!CONTROL_MESSAGE_CLASSIFIER.equals(mqttTopicTokens[0])) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL, null, mqttTopicTokens[0]);
            }

            KuraResponseChannel kuraResponseChannel = new KuraResponseChannel(mqttTopicTokens[0], mqttTopicTokens[1], mqttTopicTokens[2]);
            kuraResponseChannel.setAppId(mqttTopicTokens[3]);
            kuraResponseChannel.setReplyPart(mqttTopicTokens[4]);
            kuraResponseChannel.setRequestId(mqttTopicTokens[5]);

            // Return Kura Channel
            return kuraResponseChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, mqttTopic);
        }
    }

    /**
     * Translates the given {@link MqttPayload} to the {@link KuraResponsePayload} equivalent.
     *
     * @param mqttPayload The {@link MqttPayload} to translate.
     * @return The translated {@link KuraResponsePayload}
     * @throws InvalidPayloadException if translation encounters any error.
     * @since 1.0.0
     */
    public KuraResponsePayload translate(MqttPayload mqttPayload) throws InvalidPayloadException {
        try {
            KuraResponsePayload kuraResponsePayload = new KuraResponsePayload();

            if (mqttPayload.hasBody()) {
                kuraResponsePayload.readFromByteArray(mqttPayload.getBody());
            }

            // Return Kura Payload
            return kuraResponsePayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, mqttPayload);
        }
    }

    /**
     * Returns the {@link SystemSetting#getMessageClassifier()}.
     *
     * @return The configured the {@link SystemSetting#getMessageClassifier()}.
     * @since 1.2.0
     */
    public static String getControlMessageClassifier() {
        return CONTROL_MESSAGE_CLASSIFIER;
    }

    @Override
    public Class<MqttMessage> getClassFrom() {
        return MqttMessage.class;
    }

    @Override
    public Class<KuraResponseMessage> getClassTo() {
        return KuraResponseMessage.class;
    }
}
