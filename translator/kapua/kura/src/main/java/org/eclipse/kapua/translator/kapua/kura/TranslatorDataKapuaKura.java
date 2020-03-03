/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.kapua.kura;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;

import java.util.HashMap;

/**
 * {@link Translator} implementation from {@link KapuaDataMessage} to {@link KuraDataMessage}
 *
 * @since 1.0.0
 */
public class TranslatorDataKapuaKura extends Translator<KapuaDataMessage, KuraDataMessage> {

    @Override
    public KuraDataMessage translate(KapuaDataMessage kapuaMessage) throws TranslateException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);

        try {
            Account account = accountService.find(kapuaMessage.getScopeId());

            if (account == null) {
                throw new KapuaEntityNotFoundException(Account.TYPE, kapuaMessage.getScopeId());
            }

            //
            // Kapua Channel
            KuraDataChannel kuraDataChannel = translate(kapuaMessage.getChannel());
            kuraDataChannel.setClientId(kapuaMessage.getClientId());
            kuraDataChannel.setScope(account.getName());

            //
            // Kapua payload
            KuraDataPayload kuraDataPayload = translate(kapuaMessage.getPayload());
            kuraDataPayload.setBody(kapuaMessage.getPayload().getBody());
            kuraDataPayload.setMetrics(kapuaMessage.getPayload().getMetrics());
            kuraDataPayload.setPosition(TranslatorKapuaKuraUtils.translate(kapuaMessage.getPosition()));
            kuraDataPayload.setTimestamp(kapuaMessage.getSentOn());

            //
            // Kapua message
            KuraDataMessage kuraDataMessage = new KuraDataMessage();
            kuraDataMessage.setChannel(kuraDataChannel);
            kuraDataMessage.setPayload(kuraDataPayload);

            // Return Kapua Message
            return kuraDataMessage;
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (KapuaException ke) {
            throw new InvalidMessageException(ke, kapuaMessage);
        }
    }

    protected KuraDataChannel translate(KapuaDataChannel kapuaChannel) {
        KuraDataChannel kuraChannel = new KuraDataChannel();
        kuraChannel.setSemanticChannelParts(kapuaChannel.getSemanticParts());

        // Return Kapua Channel
        return kuraChannel;
    }

    protected KuraDataPayload translate(KapuaDataPayload kapuaPayload) {
        KuraDataPayload kuraPayload = new KuraDataPayload();

        if (kapuaPayload.getMetrics() != null) {
            kuraPayload.setMetrics(new HashMap<>(kapuaPayload.getMetrics()));
        }

        if (kapuaPayload.getBody() != null) {
            kuraPayload.setBody(kapuaPayload.getBody());
        }

        // Return Kura payload
        return kuraPayload;
    }

    @Override
    public Class<KapuaDataMessage> getClassFrom() {
        return KapuaDataMessage.class;
    }

    @Override
    public Class<KuraDataMessage> getClassTo() {
        return KuraDataMessage.class;
    }
}
