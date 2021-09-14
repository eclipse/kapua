/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.kura.amqp;

import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettingKeys;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettings;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.transport.amqp.message.AmqpMessage;
import org.eclipse.kapua.transport.amqp.message.AmqpPayload;
import org.eclipse.kapua.transport.amqp.message.AmqpTopic;

import java.util.ArrayList;
import java.util.List;

/**
 * Messages translator implementation from {@link KuraRequestMessage} to {@link AmqpMessage}
 *
 * @since 1.0
 */
public class TranslatorRequestKuraAmqp extends Translator<KuraRequestMessage, AmqpMessage> {

    @Override
    public AmqpMessage translate(KuraRequestMessage kuraMessage)
            throws TranslateException {
        // Amqp request topic
        AmqpTopic aqmpRequestTopic = translate(kuraMessage.getChannel());

        // Amqp response topic
        AmqpTopic aqmpResponseTopic = generateResponseTopic(kuraMessage.getChannel());

        // Amqp payload
        AmqpPayload aqmpPayload = translate(kuraMessage.getPayload());
        return new AmqpMessage(aqmpRequestTopic,
                aqmpResponseTopic,
                aqmpPayload);
    }

    public AmqpTopic translate(KuraRequestChannel kuraChannel)
            throws TranslateException {
        List<String> topicTokens = new ArrayList<>();

        if (kuraChannel.getMessageClassification() != null) {
            topicTokens.add(kuraChannel.getMessageClassification());
        }

        topicTokens.add(kuraChannel.getScope());
        topicTokens.add(kuraChannel.getClientId());
        topicTokens.add(kuraChannel.getAppId());
        topicTokens.add(kuraChannel.getMethod().name());

        for (String s : kuraChannel.getResources()) {
            topicTokens.add(s);
        }
        return new AmqpTopic(topicTokens.toArray(new String[0]));
    }

    public AmqpTopic generateResponseTopic(KuraRequestChannel kuraChannel) {
        String replyPart = DeviceCallSettings.getInstance().getString(DeviceCallSettingKeys.DESTINATION_REPLY_PART);

        List<String> topicTokens = new ArrayList<>();

        if (kuraChannel.getMessageClassification() != null) {
            topicTokens.add(kuraChannel.getMessageClassification());
        }

        topicTokens.add(kuraChannel.getScope());
        topicTokens.add(kuraChannel.getRequesterClientId());
        topicTokens.add(kuraChannel.getAppId());
        topicTokens.add(replyPart);
        topicTokens.add(kuraChannel.getRequestId());

        return new AmqpTopic(topicTokens.toArray(new String[0]));
    }

    public AmqpPayload translate(KuraPayload kuraPayload)
            throws TranslateException {
        return new AmqpPayload(kuraPayload.toByteArray());
    }

    @Override
    public Class<KuraRequestMessage> getClassFrom() {
        return KuraRequestMessage.class;
    }

    @Override
    public Class<AmqpMessage> getClassTo() {
        return AmqpMessage.class;
    }
}
