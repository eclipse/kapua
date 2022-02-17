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

import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.jms.JmsMessage;

import java.util.Date;

/**
 * {@link Translator} implementation from {@link JmsMessage} to {@link KuraAppsMessage}
 *
 * @since 1.0.0
 */
public class TranslatorLifeAppsJmsKura extends AbstractTranslatorLifecycleJmsKura<KuraAppsChannel, KuraAppsPayload, KuraAppsMessage> {

    public TranslatorLifeAppsJmsKura() {
        super(KuraAppsMessage.class);
    }

    @Override
    public KuraAppsMessage createLifecycleMessage(KuraAppsChannel kuraAppsChannel, Date receivedOn, KuraAppsPayload kuraAppsPayload) {
        return new KuraAppsMessage(kuraAppsChannel, receivedOn, kuraAppsPayload);
    }

    @Override
    public KuraAppsPayload createLifecyclePayload() {
        return new KuraAppsPayload();
    }

    @Override
    public KuraAppsChannel createLifecycleChannel(String messageClassifier, String scopeName, String clientId) {
        return new KuraAppsChannel(messageClassifier, scopeName, clientId);
    }
}
