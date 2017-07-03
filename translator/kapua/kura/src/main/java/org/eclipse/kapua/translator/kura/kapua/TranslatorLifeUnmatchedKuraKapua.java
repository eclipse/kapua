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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedPayload;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaUnmatchedChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaUnmatchedMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaUnmatchedPayloadImpl;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraUnmatchedChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraUnmatchedMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraUnmatchedPayload;
import org.eclipse.kapua.translator.Translator;

/**
 * Messages translator implementation from {@link KuraUnmatchedMessage} to {@link KapuaUnmatchedMessage}
 *
 * @since 1.0
 *
 */
public class TranslatorLifeUnmatchedKuraKapua extends Translator<KuraUnmatchedMessage, KapuaUnmatchedMessage> {

    @Override
    public KapuaUnmatchedMessage translate(KuraUnmatchedMessage kuraUnmatchedMessage)
            throws KapuaException {
        KapuaUnmatchedMessage kapuaUnmatchedMessage = new KapuaUnmatchedMessageImpl();
        kapuaUnmatchedMessage.setChannel(translate(kuraUnmatchedMessage.getChannel()));
        kapuaUnmatchedMessage.setPayload(translate(kuraUnmatchedMessage.getPayload()));

        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.findByName(kuraUnmatchedMessage.getChannel().getScope());

        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, kuraUnmatchedMessage.getChannel().getScope());
        }

        // no device information since may it uses an mqtt connection pooling with devices not registered in the device tables
        kapuaUnmatchedMessage.setScopeId(account.getId());
        kapuaUnmatchedMessage.setCapturedOn(kuraUnmatchedMessage.getPayload().getTimestamp());
        kapuaUnmatchedMessage.setSentOn(kuraUnmatchedMessage.getPayload().getTimestamp());
        kapuaUnmatchedMessage.setReceivedOn(kuraUnmatchedMessage.getTimestamp());
        kapuaUnmatchedMessage.setPosition(TranslatorKuraKapuaUtils.translate(kuraUnmatchedMessage.getPayload().getPosition()));

        return kapuaUnmatchedMessage;
    }

    private KapuaUnmatchedChannel translate(KuraUnmatchedChannel kuraUnmatchedChannel)
            throws KapuaException {
        KapuaUnmatchedChannel kapuaUnmatchedChannel = new KapuaUnmatchedChannelImpl();
        kapuaUnmatchedChannel.setClientId(kuraUnmatchedChannel.getClientId());
        return kapuaUnmatchedChannel;
    }

    private KapuaUnmatchedPayload translate(KuraUnmatchedPayload kuraUnmatchedPayload)
            throws KapuaException {
        KapuaUnmatchedPayload kapuaUnmatchedPayload = new KapuaUnmatchedPayloadImpl();
        kapuaUnmatchedPayload.setBody(kuraUnmatchedPayload.getBody());
        kapuaUnmatchedPayload.setMetrics(kuraUnmatchedPayload.getMetrics());
        return kapuaUnmatchedPayload;
    }

    @Override
    public Class<KuraUnmatchedMessage> getClassFrom() {
        return KuraUnmatchedMessage.class;
    }

    @Override
    public Class<KapuaUnmatchedMessage> getClassTo() {
        return KapuaUnmatchedMessage.class;
    }

}
