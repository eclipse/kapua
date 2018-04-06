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
package org.eclipse.kapua.translator.amqp.kura;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.amqp.message.AmqpMessage;
import org.eclipse.kapua.transport.amqp.message.AmqpPayload;
import org.eclipse.kapua.transport.amqp.message.AmqpTopic;

/**
 * Messages translator implementation from {@link AmqpMessage} to {@link org.eclipse.kapua.service.device.call.message.kura.KuraMessage}
 */
public class TranslatorDataAmqpKura extends Translator<AmqpMessage, KuraDataMessage> {

    @Override
    public KuraDataMessage translate(AmqpMessage aqmpMessage)
            throws KapuaException {
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
            throws KapuaException {
        String[] aqmpTopicTokens = aqmpTopic.getSplittedTopic();
        return new KuraDataChannel(aqmpTopicTokens[0],
                aqmpTopicTokens[1]);
    }

    private KuraDataPayload translate(AmqpPayload aqmpPayload)
            throws KapuaException {
        byte[] jmsBody = aqmpPayload.getBody();

        KuraDataPayload kuraPayload = new KuraDataPayload();
        kuraPayload.readFromByteArray(jmsBody);
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
