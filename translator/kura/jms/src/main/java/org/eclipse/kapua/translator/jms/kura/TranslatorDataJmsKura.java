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
 *******************************************************************************/
package org.eclipse.kapua.translator.jms.kura;

import org.eclipse.kapua.message.internal.MessageException;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    private KuraDataChannel translate(JmsTopic jmsTopic) throws InvalidChannelException {
        try {
            String[] mqttTopicTokens = jmsTopic.getSplittedTopic();
            KuraDataChannel kuraDataChannel = new KuraDataChannel();
            kuraDataChannel.setScope(mqttTopicTokens[0]);
            kuraDataChannel.setClientId(mqttTopicTokens[1]);

            List<String> channelPartsList = new LinkedList<>(Arrays.asList(mqttTopicTokens));
            // remove the first 2 items (do no use sublist since the returned object is not serializable then Camel will throws exception on error handling
            // channelPartsList.subList(2,mqttTopicTokens.length))
            channelPartsList.remove(0);
            channelPartsList.remove(0);
            kuraDataChannel.setSemanticParts(channelPartsList);
            return kuraDataChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, jmsTopic);
        }
    }

    private KuraDataPayload translate(JmsPayload jmsPayload) throws InvalidPayloadException {
        try {
            KuraDataPayload kuraPayload = new KuraDataPayload();
            if (jmsPayload.hasBody()) {
                try {
                    kuraPayload.readFromByteArray(jmsPayload.getBody());
                } catch (MessageException me) {
                    // When reading a payload which is not Kura-protobuf encoded we use that payload as a raw KapuaPayload.body
                    kuraPayload.setBody(jmsPayload.getBody());
                }
            }
            return kuraPayload;
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
