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
package org.eclipse.kapua.translator.mqtt.kura;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;

/**
 * Messages translator implementation from {@link org.eclipse.kapua.transport.message.mqtt.MqttMessage} to {@link org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorResponseMqttKura extends Translator<MqttMessage, KuraResponseMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();

    @Override
    public KuraResponseMessage translate(MqttMessage mqttMessage)
            throws KapuaException {
        //
        // Kura topic
        KuraResponseChannel kuraChannel = translate(mqttMessage.getRequestTopic());

        //
        // Kura payload
        KuraResponsePayload kuraPayload = translate(mqttMessage.getPayload());

        //
        // Kura message
        return new KuraResponseMessage(kuraChannel,
                mqttMessage.getTimestamp(),
                kuraPayload);
    }

    private KuraResponseChannel translate(MqttTopic mqttTopic)
            throws KapuaException {
        String[] mqttTopicTokens = mqttTopic.getSplittedTopic();

        if (!CONTROL_MESSAGE_CLASSIFIER.equals(mqttTopicTokens[0])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER,
                    null,
                    mqttTopicTokens[0]);
        }

        KuraResponseChannel kuraResponseChannel = new KuraResponseChannel(mqttTopicTokens[0],
                mqttTopicTokens[1],
                mqttTopicTokens[2]);

        kuraResponseChannel.setAppId(mqttTopicTokens[3]);
        kuraResponseChannel.setReplyPart(mqttTopicTokens[4]);
        kuraResponseChannel.setRequestId(mqttTopicTokens[5]);

        //
        // Return Kura Channel
        return kuraResponseChannel;
    }

    private KuraResponsePayload translate(MqttPayload mqttPayload)
            throws KapuaException {
        byte[] mqttBody = mqttPayload.getBody();

        KuraResponsePayload kuraResponsePayload = new KuraResponsePayload();
        kuraResponsePayload.readFromByteArray(mqttBody);

        //
        // Return Kura Payload
        return kuraResponsePayload;
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
