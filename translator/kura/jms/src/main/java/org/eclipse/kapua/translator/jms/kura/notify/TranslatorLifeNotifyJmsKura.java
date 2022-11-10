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
package org.eclipse.kapua.translator.jms.kura.notify;

import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;
import org.eclipse.kapua.translator.jms.kura.AbstractTranslatorJmsKura;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

import java.util.Arrays;
import java.util.Date;

/**
 * {@link Translator} implementation from {@link JmsMessage} to {@link KuraNotifyMessage}
 *
 * @since 1.0.0
 */
public class TranslatorLifeNotifyJmsKura extends AbstractTranslatorJmsKura<KuraNotifyChannel, KuraNotifyPayload, KuraNotifyMessage> {

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    public TranslatorLifeNotifyJmsKura() {
        super(KuraNotifyMessage.class);
    }

    @Override
    public KuraNotifyChannel translate(JmsTopic jmsTopic) throws InvalidChannelException {
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

    @Override
    public KuraNotifyMessage createMessage(KuraNotifyChannel deviceChannel, Date receivedOn, KuraNotifyPayload devicePayload) {
        return new KuraNotifyMessage(deviceChannel, receivedOn, devicePayload);
    }

    @Override
    public KuraNotifyChannel createChannel(String messageClassifier, String scopeName, String clientId) {
        return new KuraNotifyChannel(messageClassifier, scopeName, clientId);
    }

    @Override
    public KuraNotifyPayload createPayload() {
        return new KuraNotifyPayload();
    }

    @Override
    public Class<KuraNotifyMessage> getClassTo() {
        return KuraNotifyMessage.class;
    }
}
