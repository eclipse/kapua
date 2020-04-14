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

import org.eclipse.kapua.message.internal.MessageException;
import org.eclipse.kapua.service.device.call.message.kura.others.KuraUnmatchedChannel;
import org.eclipse.kapua.service.device.call.message.kura.others.KuraUnmatchedMessage;
import org.eclipse.kapua.service.device.call.message.kura.others.KuraUnmatchedPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

/**
 * {@link Translator} implementation from {@link JmsMessage} to {@link KuraUnmatchedMessage}
 *
 * @since 1.0.0
 */
public class TranslatorLifeUnmatchedJmsKura extends Translator<JmsMessage, KuraUnmatchedMessage> {

    @Override
    public KuraUnmatchedMessage translate(JmsMessage jmsMessage) throws TranslateException {
        try {
            return new KuraUnmatchedMessage(translate(jmsMessage.getTopic()),
                    jmsMessage.getReceivedOn(),
                    translate(jmsMessage.getPayload()));
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, jmsMessage);
        }
    }

    private KuraUnmatchedChannel translate(JmsTopic jmsTopic) throws InvalidChannelException {
        try {
            String[] topicTokens = jmsTopic.getSplittedTopic();
            if (topicTokens.length < 3) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL, null, (Object) topicTokens);
            }

            // TODO: we are loosing the sematinc parts of this topic
            return new KuraUnmatchedChannel(topicTokens[0], topicTokens[1], topicTokens[2]);
        } catch (Exception e) {
            throw new InvalidChannelException(e, jmsTopic);
        }
    }

    private KuraUnmatchedPayload translate(JmsPayload jmsPayload) throws InvalidPayloadException {
        try {
            KuraUnmatchedPayload kuraUnmatchedPayload = new KuraUnmatchedPayload();

            if (jmsPayload.hasBody()) {
                try {
                    kuraUnmatchedPayload.readFromByteArray(jmsPayload.getBody());
                } catch (MessageException me) {
                    // When reading a payload which is not Kura-protobuf encoded we use that payload as a raw KapuaPayload.body
                    kuraUnmatchedPayload.setBody(jmsPayload.getBody());
                }
            }

            return kuraUnmatchedPayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, jmsPayload);
        }
    }

    @Override
    public Class<JmsMessage> getClassFrom() {
        return JmsMessage.class;
    }

    @Override
    public Class<KuraUnmatchedMessage> getClassTo() {
        return KuraUnmatchedMessage.class;
    }
}
