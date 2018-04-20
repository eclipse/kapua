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
package org.eclipse.kapua.translator.kura.jms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.kura.KuraChannel;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

/**
 * Message translator implementation from {@link org.eclipse.kapua.service.device.call.message.kura.KuraMessage} to {@link org.eclipse.kapua.transport.message.jms.JmsMessage}
 * 
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class TranslatorDataKuraJms extends Translator<KuraMessage, JmsMessage> {

    @Override
    public JmsMessage translate(KuraMessage kuraMessage)
            throws KapuaException {
        JmsTopic jmsRequestTopic = translate(kuraMessage.getChannel());
        JmsPayload jmsPayload = translate(kuraMessage.getPayload());
        return new JmsMessage(jmsRequestTopic,
                new Date(),
                jmsPayload);
    }

    private JmsTopic translate(KuraChannel kuraChannel)
            throws KapuaException {
        List<String> topicTokens = new ArrayList<>();
        topicTokens.add(kuraChannel.getScope());
        topicTokens.add(kuraChannel.getClientId());
        if (kuraChannel.getSemanticChannelParts() != null &&
                !kuraChannel.getSemanticChannelParts().isEmpty()) {
            topicTokens.addAll(kuraChannel.getSemanticChannelParts());
        }
        return new JmsTopic(topicTokens.toArray(new String[0]));
    }

    private JmsPayload translate(KuraPayload kuraPayload)
            throws KapuaException {
        return new JmsPayload(kuraPayload.toByteArray());
    }

    @Override
    public Class<KuraMessage> getClassFrom() {
        return KuraMessage.class;
    }

    @Override
    public Class<JmsMessage> getClassTo() {
        return JmsMessage.class;
    }

}
