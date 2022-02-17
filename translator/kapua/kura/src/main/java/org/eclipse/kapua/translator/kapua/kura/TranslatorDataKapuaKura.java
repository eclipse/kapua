/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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

/**
 * {@link Translator} implementation from {@link KapuaDataMessage} to {@link KuraDataMessage}
 *
 * @since 1.0.0
 */
public class TranslatorDataKapuaKura extends Translator<KapuaDataMessage, KuraDataMessage> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);

    @Override
    public KuraDataMessage translate(KapuaDataMessage kapuaMessage) throws TranslateException {
        try {
            Account account = ACCOUNT_SERVICE.find(kapuaMessage.getScopeId());

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
        kuraChannel.setSemanticParts(kapuaChannel.getSemanticParts());

        // Return Kapua Channel
        return kuraChannel;
    }

    protected KuraDataPayload translate(KapuaDataPayload kapuaPayload) {
        KuraDataPayload kuraPayload = new KuraDataPayload();

        if (!kapuaPayload.getMetrics().isEmpty()) {
            kuraPayload.setMetrics(kapuaPayload.getMetrics());
        }

        if (kapuaPayload.hasBody()) {
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
