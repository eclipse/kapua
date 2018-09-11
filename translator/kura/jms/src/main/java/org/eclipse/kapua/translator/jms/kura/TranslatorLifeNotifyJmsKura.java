/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.jms.kura;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

/**
 * Messages translator implementation from {@link org.eclipse.kapua.transport.message.jms.JmsMessage} to {@link KuraNotifyMessage}
 *
 * @since 1.0
 */
public class TranslatorLifeNotifyJmsKura extends Translator<JmsMessage, KuraNotifyMessage> {

    @Override
    public KuraNotifyMessage translate(JmsMessage jmsMessage)
            throws KapuaException {
        return new KuraNotifyMessage(translate(jmsMessage.getTopic()),
                jmsMessage.getReceivedOn(),
                translate(jmsMessage.getPayload().getBody()));
    }

    private KuraNotifyChannel translate(JmsTopic jmsTopic)
            throws KapuaException {
        String[] topicTokens = jmsTopic.getSplittedTopic();
        // we shouldn't never get a shorter topic here (because that means we have issues on camel routing)
        // TODO check exception type
        if (topicTokens == null || topicTokens.length < 3) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR);
        }
        KuraNotifyChannel kuraNotifyChannel = new KuraNotifyChannel(topicTokens[0], topicTokens[1], topicTokens[2]);

        kuraNotifyChannel.setAppId(topicTokens[3]);
        kuraNotifyChannel.setClientId(topicTokens[5]);

        return kuraNotifyChannel;
    }

    private KuraNotifyPayload translate(byte[] jmsBody)
            throws KapuaException {
        KuraNotifyPayload kuraNotifyPayload = new KuraNotifyPayload();
        kuraNotifyPayload.readFromByteArray(jmsBody);
        return kuraNotifyPayload;
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
