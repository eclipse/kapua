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

import java.util.HashMap;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.eclipse.kapua.message.internal.device.data.KapuaDataChannelImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataMessageImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataPayloadImpl;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.KuraChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;

/**
 * Messages translator implementation from {@link KuraDataMessage} to {@link KapuaDataMessage}
 *
 * @since 1.0
 *
 */
public class TranslatorDataKuraKapua extends Translator<KuraDataMessage, KapuaDataMessage> {

    @Override
    public KapuaDataMessage translate(KuraDataMessage kuraDataMessage)
            throws KapuaException {
        //
        // Kapua Channel
        KapuaDataChannel kapuaDataChannel = translate(kuraDataMessage.getChannel());

        //
        // Kapua payload
        KapuaDataPayload kapuaDataPayload = translate(kuraDataMessage.getPayload());

        //
        // Kapua message
        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.findByName(kuraDataMessage.getChannel().getScope());

        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, kuraDataMessage.getChannel().getScope());
        }

        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        Device device = deviceRegistryService.findByClientId(account.getId(),
                kuraDataMessage.getChannel().getClientId());

        KapuaDataMessage kapuaDataMessage = new KapuaDataMessageImpl();
        kapuaDataMessage.setScopeId(account.getId());
        kapuaDataMessage.setDeviceId(device != null ? device.getId() : null);
        kapuaDataMessage.setClientId(kuraDataMessage.getChannel().getClientId());
        kapuaDataMessage.setChannel(kapuaDataChannel);
        kapuaDataMessage.setPayload(kapuaDataPayload);
        kapuaDataMessage.setCapturedOn(kuraDataMessage.getPayload().getTimestamp());
        kapuaDataMessage.setSentOn(kuraDataMessage.getPayload().getTimestamp());
        kapuaDataMessage.setReceivedOn(kuraDataMessage.getTimestamp());
        kapuaDataMessage.setPosition(TranslatorKuraKapuaUtils.translate(kuraDataMessage.getPayload().getPosition()));

        // Return Kapua Message
        return kapuaDataMessage;
    }

    private KapuaDataChannel translate(KuraChannel kuraChannel)
            throws KapuaException {
        KapuaDataChannel kapuaChannel = new KapuaDataChannelImpl();
        kapuaChannel.setSemanticParts(kuraChannel.getSemanticChannelParts());

        //
        // Return Kapua Channel
        return kapuaChannel;
    }

    private KapuaDataPayload translate(KuraDataPayload kuraPayload)
            throws KapuaException {
        KapuaDataPayload kapuaPayload = new KapuaDataPayloadImpl();

        if (kuraPayload.getMetrics() != null) {
            kapuaPayload.setMetrics(new HashMap<>(kuraPayload.getMetrics()));
        }

        if (kuraPayload.getBody() != null) {
            kapuaPayload.setBody(kuraPayload.getBody());
        }

        //
        // Return Kapua payload
        return kapuaPayload;
    }

    @Override
    public Class<KuraDataMessage> getClassFrom() {
        return KuraDataMessage.class;
    }

    @Override
    public Class<KapuaDataMessage> getClassTo() {
        return KapuaDataMessage.class;
    }

}
