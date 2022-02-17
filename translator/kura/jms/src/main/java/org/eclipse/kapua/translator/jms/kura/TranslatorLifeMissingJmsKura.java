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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.translator.jms.kura;

import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.jms.JmsMessage;

import java.util.Date;

/**
 * {@link Translator} implementation from {@link JmsMessage} to {@link KuraMissingMessage}
 *
 * @since 1.0.0
 */
public class TranslatorLifeMissingJmsKura extends AbstractTranslatorLifecycleJmsKura<KuraMissingChannel, KuraMissingPayload, KuraMissingMessage> {

    public TranslatorLifeMissingJmsKura() {
        super(KuraMissingMessage.class);
    }

    @Override
    public KuraMissingMessage createLifecycleMessage(KuraMissingChannel kuraMissingChannel, Date receivedOn, KuraMissingPayload kuraMissingPayload) {
        return new KuraMissingMessage(kuraMissingChannel, receivedOn, kuraMissingPayload);
    }

    @Override
    public KuraMissingPayload createLifecyclePayload() {
        return new KuraMissingPayload();
    }

    @Override
    public KuraMissingChannel createLifecycleChannel(String messageClassifier, String scopeName, String clientId) {
        return new KuraMissingChannel(messageClassifier, scopeName, clientId);
    }

    @Override
    public boolean isRawPayloadToBody() {
        return Boolean.TRUE;
    }
}
