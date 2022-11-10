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
package org.eclipse.kapua.translator.jms.kura.event;

import org.eclipse.kapua.service.device.call.message.kura.event.configuration.KuraConfigurationEventChannel;
import org.eclipse.kapua.service.device.call.message.kura.event.configuration.KuraConfigurationEventMessage;
import org.eclipse.kapua.service.device.call.message.kura.event.configuration.KuraConfigurationEventPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.jms.JmsMessage;

import java.util.Date;

/**
 * {@link Translator} implementation from {@link JmsMessage} to {@link KuraConfigurationEventMessage}
 *
 * @since 2.0.0
 */
public class TranslatorEventConfigurationManagementJmsKura extends AbstractTranslatorEventJmsKura<KuraConfigurationEventChannel, KuraConfigurationEventPayload, KuraConfigurationEventMessage> {

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    public TranslatorEventConfigurationManagementJmsKura() {
        super(KuraConfigurationEventMessage.class);
    }

    @Override
    public KuraConfigurationEventMessage createMessage(KuraConfigurationEventChannel deviceChannel, Date receivedOn, KuraConfigurationEventPayload devicePayload) {
        return new KuraConfigurationEventMessage(deviceChannel, receivedOn, devicePayload);
    }

    @Override
    public KuraConfigurationEventPayload createPayload() {
        return new KuraConfigurationEventPayload();
    }

    @Override
    public KuraConfigurationEventChannel createChannel(String messageClassifier, String scopeName, String clientId) {
        return new KuraConfigurationEventChannel(messageClassifier, scopeName, clientId);
    }
}
