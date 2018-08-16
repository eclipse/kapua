/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.amqp;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.amqpproton.message.AmqpMessage;
import org.eclipse.kapua.transport.amqpproton.message.AmqpPayload;
import org.eclipse.kapua.transport.amqpproton.message.AmqpTopic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Messages translator implementation from {@link org.eclipse.kapua.service.device.call.message.kura.KuraMessage} to {@link AmqpMessage}
 */
public class TranslatorDataKuraAmqp extends Translator<KuraDataMessage, AmqpMessage> {

    @Override
    public AmqpMessage translate(KuraDataMessage kuraMessage)
            throws KapuaException {
        // Amqp request topic
        AmqpTopic aqmpRequestTopic = translate(kuraMessage.getChannel());

        // Amqp payload
        AmqpPayload aqmpPayload = translate(kuraMessage.getPayload());

        // Return Amqp message
        return new AmqpMessage(aqmpRequestTopic,
                new Date(),
                aqmpPayload);
    }

    private AmqpTopic translate(KuraDataChannel kuraChannel)
            throws KapuaException {
        List<String> topicTokens = new ArrayList<>();

        topicTokens.add(kuraChannel.getScope());
        topicTokens.add(kuraChannel.getClientId());

        if (kuraChannel.getSemanticChannelParts() != null &&
                !kuraChannel.getSemanticChannelParts().isEmpty()) {
            topicTokens.addAll(kuraChannel.getSemanticChannelParts());
        }

        return new AmqpTopic(topicTokens.toArray(new String[0]));
    }

    private AmqpPayload translate(KuraDataPayload kuraPayload)
            throws KapuaException {
        return new AmqpPayload(kuraPayload.toByteArray());
    }

    @Override
    public Class<KuraDataMessage> getClassFrom() {
        return KuraDataMessage.class;
    }

    @Override
    public Class<AmqpMessage> getClassTo() {
        return AmqpMessage.class;
    }

}
