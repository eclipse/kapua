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
 *******************************************************************************/
package org.eclipse.kapua.translator.jms.kura;

import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyPayload;
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

import java.util.Arrays;

/**
 * {@link Translator} implementation from {@link JmsMessage} to {@link KuraNotifyMessage}
 *
 * @since 1.0.0
 */
public class TranslatorLifeNotifyJmsKura extends Translator<JmsMessage, KuraNotifyMessage> {

    @Override
    public KuraNotifyMessage translate(JmsMessage jmsMessage) throws TranslateException {
        try {
            return new KuraNotifyMessage(translate(jmsMessage.getTopic()),
                    jmsMessage.getReceivedOn(),
                    translate(jmsMessage.getPayload()));
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, jmsMessage);
        }
    }

    private KuraNotifyChannel translate(JmsTopic jmsTopic) throws InvalidChannelException {
        try {
            String[] topicTokens = jmsTopic.getSplittedTopic();

            if (topicTokens.length < 6) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL, null, (Object) topicTokens);
            }

            KuraNotifyChannel kuraNotifyChannel = new KuraNotifyChannel(topicTokens[0], topicTokens[1], topicTokens[2]);
            kuraNotifyChannel.setAppId(topicTokens[3]);
            kuraNotifyChannel.setClientId(topicTokens[5]);
            kuraNotifyChannel.setResources(Arrays.copyOfRange(topicTokens, 6, topicTokens.length));

            return kuraNotifyChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, jmsTopic);
        }
    }

    private KuraNotifyPayload translate(JmsPayload jmsPayload) throws InvalidPayloadException {
        try {
            KuraNotifyPayload kuraNotifyPayload = new KuraNotifyPayload();

            if (jmsPayload.hasBody()) {
                kuraNotifyPayload.readFromByteArray(jmsPayload.getBody());
            }

            return kuraNotifyPayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, jmsPayload);
        }
    }

    @Override
    public Class<JmsMessage> getClassFrom() {
        return JmsMessage.class;
    }

    @Override
    public Class<KuraNotifyMessage> getClassTo() {
        return KuraNotifyMessage.class;
    }
}
