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

import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.jms.JmsMessage;

import java.util.Date;

/**
 * {@link Translator} implementation from {@link JmsMessage} to {@link KuraDisconnectMessage}
 *
 * @since 1.0.0
 */
public class TranslatorLifeDisconnectJmsKura extends AbstractTranslatorLifecycleJmsKura<KuraDisconnectChannel, KuraDisconnectPayload, KuraDisconnectMessage> {

    public TranslatorLifeDisconnectJmsKura() {
        super(KuraDisconnectMessage.class);
    }

    @Override
    public KuraDisconnectMessage createLifecycleMessage(KuraDisconnectChannel kuraDisconnectChannel, Date receivedOn, KuraDisconnectPayload kuraDisconnectPayload) {
        return new KuraDisconnectMessage(kuraDisconnectChannel, receivedOn, kuraDisconnectPayload);
    }

    @Override
    public KuraDisconnectPayload createLifecyclePayload() {
        return new KuraDisconnectPayload();
    }

    @Override
    public KuraDisconnectChannel createLifecycleChannel(String messageClassifier, String scopeName, String clientId) {
        return new KuraDisconnectChannel(messageClassifier, scopeName, clientId);
    }
}
