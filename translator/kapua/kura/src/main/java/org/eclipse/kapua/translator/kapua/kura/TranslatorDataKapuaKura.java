/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import java.util.HashMap;

/**
 * Messages translator implementation from {@link KuraDataMessage} to {@link KapuaDataMessage}
 *
 * @since 1.0
 *
 */
public class TranslatorDataKapuaKura extends Translator<KapuaDataMessage, KuraDataMessage> {

    @Override
    public KuraDataMessage translate(KapuaDataMessage kapuaDataMessage)
            throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.find(kapuaDataMessage.getScopeId());

        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, kapuaDataMessage.getScopeId());
        }

        //
        // Kapua Channel
        KuraDataChannel kuraDataChannel = translate(kapuaDataMessage.getChannel());
        kuraDataChannel.setClientId(kapuaDataMessage.getClientId());
        kuraDataChannel.setScope(account.getName());

        //
        // Kapua payload
        KuraDataPayload kuraDataPayload = translate(kapuaDataMessage.getPayload());
        kuraDataPayload.setBody(kapuaDataMessage.getPayload().getBody());
        kuraDataPayload.setMetrics(kapuaDataMessage.getPayload().getMetrics());
        kuraDataPayload.setPosition(TranslatorKapuaKuraUtils.translate(kapuaDataMessage.getPosition()));
        kuraDataPayload.setTimestamp(kapuaDataMessage.getSentOn());

        //
        // Kapua message
        KuraDataMessage kuraDataMessage = new KuraDataMessage();
        kuraDataMessage.setChannel(kuraDataChannel);
        kuraDataMessage.setPayload(kuraDataPayload);

        // Return Kapua Message
        return kuraDataMessage;
    }

    private KuraDataChannel translate(KapuaDataChannel kapuaChannel)
            throws KapuaException {
        KuraDataChannel kuraChannel = new KuraDataChannel();
        kuraChannel.setSemanticChannelParts(kapuaChannel.getSemanticParts());

        //
        // Return Kapua Channel
        return kuraChannel;
    }

    private KuraDataPayload translate(KapuaDataPayload kapuaPayload)
            throws KapuaException {
        KuraDataPayload kuraPayload = new KuraDataPayload();

        if (kapuaPayload.getMetrics() != null) {
            kuraPayload.setMetrics(new HashMap<>(kapuaPayload.getMetrics()));
        }

        if (kapuaPayload.getBody() != null) {
            kuraPayload.setBody(kapuaPayload.getBody());
        }

        //
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
