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
package org.eclipse.kapua.translator.jms.kura;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.internal.MessageException;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

/**
 * {@link Translator} implementation from {@link JmsMessage} to {@link KuraMissingMessage}
 *
 * @since 1.0.0
 */
public class TranslatorLifeMissingJmsKura extends Translator<JmsMessage, KuraMissingMessage> {


    @Override
    public KuraMissingMessage translate(JmsMessage jmsMessage) throws TranslateException {
        try {
            return new KuraMissingMessage(translate(jmsMessage.getTopic()),
                    jmsMessage.getReceivedOn(),
                    translate(jmsMessage.getPayload()));
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, jmsMessage);
        }
    }

    private KuraMissingChannel translate(JmsTopic jmsTopic) throws InvalidChannelException {
        try {
            String[] topicTokens = jmsTopic.getSplittedTopic();
            // we shouldn't never get a shorter topic here (because that means we have issues on camel routing)
            if (topicTokens == null || topicTokens.length < 3) {
                throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR);
            }
            return new KuraMissingChannel(topicTokens[1], topicTokens[2]);
        } catch (Exception e) {
            throw new InvalidChannelException(e, jmsTopic);
        }
    }

    private KuraMissingPayload translate(JmsPayload jmsPayload) throws InvalidPayloadException {
        try {
            KuraMissingPayload kuraMissingPayload = new KuraMissingPayload();
            // missing message may be a text message so if the message protobuf conversion fails, try to set the kura message body as jms message raw payload
            try {
                kuraMissingPayload.readFromByteArray(jmsPayload.getBody());
            } catch (MessageException me) {
                // When reading a payload which is not Kura-protobuf encoded we use that payload as a raw KapuaPayload.body
                kuraMissingPayload.setBody(jmsPayload.getBody());
            }
            return kuraMissingPayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, jmsPayload);
        }
    }

    @Override
    public Class<JmsMessage> getClassFrom() {
        return JmsMessage.class;
    }

    @Override
    public Class<KuraMissingMessage> getClassTo() {
        return KuraMissingMessage.class;
    }
}
