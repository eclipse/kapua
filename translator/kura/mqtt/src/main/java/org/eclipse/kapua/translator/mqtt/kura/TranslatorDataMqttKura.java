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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.translator.mqtt.kura;

import org.eclipse.kapua.message.internal.MessageException;
import org.eclipse.kapua.service.device.call.kura.Kura;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
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
 * {@link Translator} implementation from {@link MqttMessage} to {@link KuraDataMessage}
 *
 * @since 1.0.0
 */
public class TranslatorDataMqttKura extends Translator<MqttMessage, KuraDataMessage> {

    @Override
    public KuraDataMessage translate(MqttMessage mqttMessage) throws TranslateException {
        try {
            // Kura topic
            KuraDataChannel kuraChannel = translate(mqttMessage.getRequestTopic());

            // Kura payload
            KuraDataPayload kuraPayload = translate(mqttMessage.getPayload());

            // Return Kura message
            return new KuraDataMessage(kuraChannel, mqttMessage.getTimestamp(), kuraPayload);
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, mqttMessage);
        }
    }

    /**
     * Translates the given {@link MqttTopic} to the {@link KuraDataChannel} equivalent.
     *
     * @param mqttTopic The {@link MqttTopic} to translate.
     * @return The translated {@link KuraDataChannel}
     * @throws InvalidChannelException if translation encounters any error (i.e.: not enough {@link MqttTopic#getSplittedTopic()} tokens.
     * @since 1.0.0
     */
    public KuraDataChannel translate(MqttTopic mqttTopic) throws InvalidChannelException {
        try {
            String[] topicTokens = mqttTopic.getSplittedTopic();

            if (topicTokens.length < 2) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL, null, (Object) topicTokens);
            }

            KuraDataChannel kuraDataChannel = new KuraDataChannel();
            kuraDataChannel.setScope(topicTokens[0]);
            kuraDataChannel.setClientId(topicTokens[1]);
            for (int i = 2; i < topicTokens.length; i++) {
                kuraDataChannel.getSemanticParts().add(topicTokens[i]);
            }

            // Return Kura Channel
            return kuraDataChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, mqttTopic);
        }
    }

    /**
     * Translates the given {@link MqttPayload} to the {@link KuraDataPayload} equivalent.
     * <p>
     * If {@link MqttPayload#getBody()} is not {@link Kura} protobuf encoded the raw data will be put into {@link KuraDataPayload#getBody()}
     *
     * @param mqttPayload The {@link MqttPayload} to translate.
     * @return The translated {@link KuraDataPayload}
     * @throws InvalidPayloadException if translation encounters any error.
     * @since 1.0.0
     */
    public KuraDataPayload translate(MqttPayload mqttPayload) throws InvalidPayloadException {
        try {
            KuraDataPayload kuraDataPayload = new KuraDataPayload();

            if (mqttPayload.hasBody()) {
                byte[] mqttBody = mqttPayload.getBody();

                try {
                    kuraDataPayload.readFromByteArray(mqttBody);
                } catch (MessageException ex) {
                    kuraDataPayload.setBody(mqttBody);
                }
            }

            // Return Kura Payload
            return kuraDataPayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, mqttPayload);
        }
    }

    @Override
    public Class<MqttMessage> getClassFrom() {
        return MqttMessage.class;
    }

    @Override
    public Class<KuraDataMessage> getClassTo() {
        return KuraDataMessage.class;
    }
}
