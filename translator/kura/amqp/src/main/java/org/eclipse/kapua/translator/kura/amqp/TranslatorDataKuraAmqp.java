/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.kura.amqp;

import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.transport.amqp.message.AmqpMessage;
import org.eclipse.kapua.transport.amqp.message.AmqpPayload;
import org.eclipse.kapua.transport.amqp.message.AmqpTopic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Messages translator implementation from {@link org.eclipse.kapua.service.device.call.message.kura.KuraMessage} to {@link AmqpMessage}
 */
public class TranslatorDataKuraAmqp extends Translator<KuraDataMessage, AmqpMessage> {

    @Override
    public AmqpMessage translate(KuraDataMessage kuraMessage)
            throws TranslateException {
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
            throws TranslateException {
        List<String> topicTokens = new ArrayList<>();

        topicTokens.add(kuraChannel.getScope());
        topicTokens.add(kuraChannel.getClientId());

        if (kuraChannel.getSemanticParts() != null &&
                !kuraChannel.getSemanticParts().isEmpty()) {
            topicTokens.addAll(kuraChannel.getSemanticParts());
        }

        return new AmqpTopic(topicTokens.toArray(new String[0]));
    }

    private AmqpPayload translate(KuraDataPayload kuraPayload)
            throws TranslateException {
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
