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
package org.eclipse.kapua.translator.amqp.kura;

import org.eclipse.kapua.message.internal.MessageException;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.transport.amqp.message.AmqpMessage;
import org.eclipse.kapua.transport.amqp.message.AmqpPayload;
import org.eclipse.kapua.transport.amqp.message.AmqpTopic;

/**
 * Messages translator implementation from {@link AmqpMessage} to {@link org.eclipse.kapua.service.device.call.message.kura.KuraMessage}
 */
public class TranslatorDataAmqpKura extends Translator<AmqpMessage, KuraDataMessage> {

    @Override
    public KuraDataMessage translate(AmqpMessage aqmpMessage)
            throws TranslateException {
        // Kura topic
        KuraDataChannel kuraChannel = translate(aqmpMessage.getRequestTopic());

        // Kura payload
        KuraDataPayload kuraPayload = translate(aqmpMessage.getPayload());

        // Return Kura message
        return new KuraDataMessage(kuraChannel,
                aqmpMessage.getTimestamp(),
                kuraPayload);
    }

    private KuraDataChannel translate(AmqpTopic aqmpTopic)
            throws TranslateException {
        String[] aqmpTopicTokens = aqmpTopic.getSplittedTopic();
        return new KuraDataChannel(aqmpTopicTokens[0],
                aqmpTopicTokens[1]);
    }

    private KuraDataPayload translate(AmqpPayload aqmpPayload)
            throws TranslateException {
        byte[] jmsBody = aqmpPayload.getBody();

        KuraDataPayload kuraPayload = new KuraDataPayload();
        try {
            kuraPayload.readFromByteArray(jmsBody);
        } catch (MessageException e) {
            throw new InvalidPayloadException(e, aqmpPayload);
        }
        return kuraPayload;
    }

    @Override
    public Class<AmqpMessage> getClassFrom() {
        return AmqpMessage.class;
    }

    @Override
    public Class<KuraDataMessage> getClassTo() {
        return KuraDataMessage.class;
    }

}
