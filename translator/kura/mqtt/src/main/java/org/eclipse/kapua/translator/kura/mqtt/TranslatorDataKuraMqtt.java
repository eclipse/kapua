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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Messages translator implementation from {@link org.eclipse.kapua.service.device.call.message.kura.KuraMessage} to {@link org.eclipse.kapua.transport.message.mqtt.MqttMessage}
 * 
 * @since 1.0
 *
 */
@SuppressWarnings("rawtypes")
public class TranslatorDataKuraMqtt extends Translator<KuraDataMessage, MqttMessage> {

    @Override
    public MqttMessage translate(KuraDataMessage kuraMessage)
            throws KapuaException {
        //
        // Mqtt request topic
        MqttTopic mqttRequestTopic = translate(kuraMessage.getChannel());

        //
        // Mqtt payload
        MqttPayload mqttPayload = translate(kuraMessage.getPayload());

        //
        // Return Mqtt message
        return new MqttMessage(mqttRequestTopic,
                new Date(),
                mqttPayload);
    }

    private MqttTopic translate(KuraDataChannel kuraChannel)
            throws KapuaException {
        List<String> topicTokens = new ArrayList<>();

        topicTokens.add(kuraChannel.getScope());
        topicTokens.add(kuraChannel.getClientId());

        if (kuraChannel.getSemanticChannelParts() != null &&
                !kuraChannel.getSemanticChannelParts().isEmpty()) {
            topicTokens.addAll(kuraChannel.getSemanticChannelParts());
        }

        return new MqttTopic(topicTokens.toArray(new String[0]));
    }

    private MqttPayload translate(KuraDataPayload kuraPayload)
            throws KapuaException {
        return new MqttPayload(kuraPayload.toByteArray());
    }

    @Override
    public Class<KuraDataMessage> getClassFrom() {
        return KuraDataMessage.class;
    }

    @Override
    public Class<MqttMessage> getClassTo() {
        return MqttMessage.class;
    }

}
