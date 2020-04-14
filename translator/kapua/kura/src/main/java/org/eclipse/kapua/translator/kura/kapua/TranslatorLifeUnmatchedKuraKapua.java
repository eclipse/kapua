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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedPayload;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaUnmatchedChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaUnmatchedMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaUnmatchedPayloadImpl;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.others.KuraUnmatchedChannel;
import org.eclipse.kapua.service.device.call.message.kura.others.KuraUnmatchedMessage;
import org.eclipse.kapua.service.device.call.message.kura.others.KuraUnmatchedPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;

/**
 * {@link Translator} implementation from {@link KuraUnmatchedMessage} to {@link KapuaUnmatchedMessage}
 *
 * @since 1.0.0
 */
public class TranslatorLifeUnmatchedKuraKapua extends Translator<KuraUnmatchedMessage, KapuaUnmatchedMessage> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);

    @Override
    public KapuaUnmatchedMessage translate(KuraUnmatchedMessage kuraUnmatchedMessage) throws TranslateException {
        try {
            KapuaUnmatchedMessage kapuaUnmatchedMessage = new KapuaUnmatchedMessageImpl();
            kapuaUnmatchedMessage.setChannel(translate(kuraUnmatchedMessage.getChannel()));
            kapuaUnmatchedMessage.setPayload(translate(kuraUnmatchedMessage.getPayload()));

            Account account = ACCOUNT_SERVICE.findByName(kuraUnmatchedMessage.getChannel().getScope());

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
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, kuraUnmatchedMessage);
        }
    }

    private KapuaUnmatchedChannel translate(KuraUnmatchedChannel kuraUnmatchedChannel) {
        KapuaUnmatchedChannel kapuaUnmatchedChannel = new KapuaUnmatchedChannelImpl();
        kapuaUnmatchedChannel.setClientId(kuraUnmatchedChannel.getClientId());
        return kapuaUnmatchedChannel;
    }

    private KapuaUnmatchedPayload translate(KuraUnmatchedPayload kuraUnmatchedPayload) {
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
