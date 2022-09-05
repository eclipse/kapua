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
package org.eclipse.kapua.translator.jms.kura.data;

import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;
import org.eclipse.kapua.translator.jms.kura.AbstractTranslatorJmsKura;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

import java.util.Date;

/**
 * {@link Translator} implementation from {@link JmsMessage} to {@link KuraDataMessage}
 *
 * @since 1.0.0
 */
public class TranslatorDataJmsKura extends AbstractTranslatorJmsKura<KuraDataChannel, KuraDataPayload, KuraDataMessage> {

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    public TranslatorDataJmsKura() {
        super(KuraDataMessage.class);
    }

    /**
     * Translates the given {@link JmsTopic} to the {@link KuraDataChannel} equivalent.
     *
     * @param jmsTopic The {@link JmsTopic} to translate.
     * @return The translated {@link KuraDataChannel}
     * @throws InvalidChannelException if translation encounters any error (i.e.: not enough {@link JmsTopic#getSplittedTopic()} tokens.
     * @since 1.0.0
     */
    @Override
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

    @Override
    public KuraDataMessage createMessage(KuraDataChannel deviceChannel, Date receivedOn, KuraDataPayload devicePayload) {
        return new KuraDataMessage(deviceChannel, receivedOn, devicePayload);
    }

    @Override
    public KuraDataChannel createChannel(String messageClassifier, String scopeName, String clientId) {
        return new KuraDataChannel(scopeName, clientId);
    }

    @Override
    public KuraDataPayload createPayload() {
        return new KuraDataPayload();
    }

    @Override
    public boolean isRawPayloadToBody() {
        return Boolean.TRUE;
    }

    @Override
    public Class<KuraDataMessage> getClassTo() {
        return KuraDataMessage.class;
    }

}
