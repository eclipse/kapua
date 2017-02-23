/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *
 *******************************************************************************/
package org.eclipse.kapua.translator.jms.kura;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraUnmatchedChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraUnmatchedMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraUnmatchedPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Messages translator implementation from {@link org.eclipse.kapua.transport.message.jms.JmsMessage} to {@link org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraUnmatchedMessage}
 * 
 * @since 1.0
 * 
 */
public class TranslatorLifeUnmatchedJmsKura extends Translator<JmsMessage, KuraUnmatchedMessage>
{
    private static final Logger logger = LoggerFactory.getLogger(TranslatorLifeUnmatchedJmsKura.class);
    
    @Override
    public KuraUnmatchedMessage translate(JmsMessage jmsMessage)
        throws KapuaException
    {
        return new KuraUnmatchedMessage(translate(jmsMessage.getTopic()),
                                      jmsMessage.getReceivedOn(),
                                      translate(jmsMessage.getPayload().getBody()));
    }

    private KuraUnmatchedChannel translate(JmsTopic jmsTopic)
        throws KapuaException
    {
        String[] topicTokens = jmsTopic.getSplittedTopic();
        // we shouldn't never get a shorter topic here (because that means we have issues on camel routing)
        // TODO check exception type
        if (topicTokens == null || topicTokens.length < 3) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR);
        }
        return new KuraUnmatchedChannel(topicTokens[1],
                                      topicTokens[2]);
    }

    private KuraUnmatchedPayload translate(byte[] jmsBody)
        throws KapuaException
    {
        KuraUnmatchedPayload kuraUnmatchedPayload = new KuraUnmatchedPayload();
        // unmatched message may be a text message so if the message protobuf conversion fails, try to set the kura message body as jms message raw payload
        try {
            kuraUnmatchedPayload.readFromByteArray(jmsBody);
        }
        catch (Throwable t) {
            logger.debug("Invalid protobuf message - set Kura payload as jms message raw body");
            kuraUnmatchedPayload.setBody(jmsBody);
        }
        return kuraUnmatchedPayload;
    }

    @Override
    public Class<JmsMessage> getClassFrom()
    {
        return JmsMessage.class;
    }

    @Override
    public Class<KuraUnmatchedMessage> getClassTo()
    {
        return KuraUnmatchedMessage.class;
    }
}
