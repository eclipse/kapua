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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Message translator implementation from {@link org.eclipse.kapua.service.device.call.message.kura.KuraMessage} to {@link org.eclipse.kapua.transport.message.jms.JmsMessage}
 *
 * @since 1.0
 */
public class TranslatorDataKuraJms extends Translator<KuraDataMessage, JmsMessage> {

    @Override
    public JmsMessage translate(KuraDataMessage kuraMessage)
            throws KapuaException {
        JmsTopic jmsRequestTopic = translate(kuraMessage.getChannel());
        JmsPayload jmsPayload = translate(kuraMessage.getPayload());
        return new JmsMessage(jmsRequestTopic,
                new Date(),
                jmsPayload);
    }

    private JmsTopic translate(KuraDataChannel kuraChannel)
            throws KapuaException {
        List<String> topicTokens = new ArrayList<>();
        topicTokens.add(kuraChannel.getScope());
        topicTokens.add(kuraChannel.getClientId());
        if (kuraChannel.getSemanticParts() != null &&
                !kuraChannel.getSemanticParts().isEmpty()) {
            topicTokens.addAll(kuraChannel.getSemanticParts());
        }
        return new JmsTopic(topicTokens.toArray(new String[0]));
    }

    private JmsPayload translate(KuraPayload kuraPayload)
            throws KapuaException {
        return new JmsPayload(kuraPayload.toByteArray());
    }

    @Override
    public Class<KuraDataMessage> getClassFrom() {
        return KuraDataMessage.class;
    }

    @Override
    public Class<JmsMessage> getClassTo() {
        return JmsMessage.class;
    }

}
