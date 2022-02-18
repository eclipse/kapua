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

import org.eclipse.kapua.message.internal.MessageException;
import org.eclipse.kapua.service.device.call.kura.Kura;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
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
 * {@link Translator} implementation from {@link JmsMessage} to {@link KuraDataMessage}
 *
 * @since 1.0.0
 */
public class TranslatorDataJmsKura extends Translator<JmsMessage, KuraDataMessage> {

    @Override
    public KuraDataMessage translate(JmsMessage jmsMessage) throws TranslateException {
        try {
            KuraDataChannel kuraChannel = translate(jmsMessage.getTopic());
            KuraDataPayload kuraPayload = translate(jmsMessage.getPayload());
            return new KuraDataMessage(kuraChannel, jmsMessage.getReceivedOn(), kuraPayload);
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, jmsMessage);
        }
    }

    /**
     * Translates the given {@link JmsTopic} to the {@link KuraDataChannel} equivalent.
     *
     * @param jmsTopic The {@link JmsTopic} to translate.
     * @return The translated {@link KuraDataChannel}
     * @throws InvalidChannelException if translation encounters any error (i.e.: not enough {@link JmsTopic#getSplittedTopic()} tokens.
     * @since 1.0.0
     */
    public KuraDataChannel translate(JmsTopic jmsTopic) throws InvalidChannelException {
        try {
            String[] topicTokens = jmsTopic.getSplittedTopic();

            if (topicTokens.length < 2) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL, null, (Object) topicTokens);
            }

            KuraDataChannel kuraDataChannel = new KuraDataChannel();
            kuraDataChannel.setScope(topicTokens[0]);
            kuraDataChannel.setClientId(topicTokens[1]);
            for (int i = 2; i < topicTokens.length; i++) {
                kuraDataChannel.getSemanticParts().add(topicTokens[i]);
            }

            return kuraDataChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, jmsTopic);
        }
    }

    /**
     * Translates the given {@link JmsPayload} to the {@link KuraDataPayload} equivalent.
     * <p>
     * If {@link JmsPayload#getBody()} is not {@link Kura} protobuf encoded the raw data will be put into {@link KuraDataPayload#getBody()}
     *
     * @param jmsPayload The {@link JmsPayload} to translate.
     * @return The translated {@link KuraDataPayload}
     * @throws InvalidPayloadException if translation encounters any error.
     * @since 1.0.0
     */
    public KuraDataPayload translate(JmsPayload jmsPayload) throws InvalidPayloadException {
        try {
            KuraDataPayload kuraDataPayload = new KuraDataPayload();

            if (jmsPayload.hasBody()) {
                byte[] mqttBody = jmsPayload.getBody();

                try {
                    kuraDataPayload.readFromByteArray(mqttBody);
                } catch (MessageException ex) {
                    kuraDataPayload.setBody(mqttBody);
                }
            }
            return kuraDataPayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, jmsPayload);
        }
    }

    @Override
    public Class<JmsMessage> getClassFrom() {
        return JmsMessage.class;
    }

    @Override
    public Class<KuraDataMessage> getClassTo() {
        return KuraDataMessage.class;
    }

}
