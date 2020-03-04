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
package org.eclipse.kapua.translator.mqtt.kura;

import org.eclipse.kapua.message.internal.MessageException;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;
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

    private KuraDataChannel translate(MqttTopic mqttTopic) throws InvalidChannelException {
        try {
            String[] mqttTopicTokens = mqttTopic.getSplittedTopic();

            // Return Kura Channel
            return new KuraDataChannel(mqttTopicTokens[0], mqttTopicTokens[1]);
        } catch (Exception e) {
            throw new InvalidChannelException(e, mqttTopic);
        }
    }

    private KuraDataPayload translate(MqttPayload mqttPayload) throws InvalidPayloadException {
        try {
            byte[] mqttBody = mqttPayload.getBody();

            KuraDataPayload kuraPayload = new KuraDataPayload();
            try {
                kuraPayload.readFromByteArray(mqttBody);
            } catch (MessageException ex) {
                kuraPayload.setBody(mqttBody);
            }

            //
            // Return Kura Payload
            return kuraPayload;
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
