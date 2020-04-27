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
