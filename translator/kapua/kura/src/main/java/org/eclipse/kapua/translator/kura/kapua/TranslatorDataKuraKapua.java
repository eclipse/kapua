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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataMessageFactory;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;

/**
 * {@link Translator} implementation from {@link KuraDataMessage} to {@link KapuaDataMessage}
 *
 * @since 1.0.0
 */
public class TranslatorDataKuraKapua extends Translator<KuraDataMessage, KapuaDataMessage> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);
    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);

    private static final KapuaDataMessageFactory DATA_MESSAGE_FACTORY = LOCATOR.getFactory(KapuaDataMessageFactory.class);

    @Override
    public KapuaDataMessage translate(KuraDataMessage kuraMessage) throws TranslateException {
        try {
            //
            // Kapua Channel
            KapuaDataChannel kapuaDataChannel = translate(kuraMessage.getChannel());

            //
            // Kapua payload
            KapuaDataPayload kapuaDataPayload = translate(kuraMessage.getPayload());

            //
            // Kapua message
            Account account = ACCOUNT_SERVICE.findByName(kuraMessage.getChannel().getScope());

            if (account == null) {
                throw new KapuaEntityNotFoundException(Account.TYPE, kuraMessage.getChannel().getScope());
            }

            Device device = DEVICE_REGISTRY_SERVICE.findByClientId(account.getId(), kuraMessage.getChannel().getClientId());

            KapuaDataMessage kapuaDataMessage = DATA_MESSAGE_FACTORY.newKapuaDataMessage();
            kapuaDataMessage.setScopeId(account.getId());
            kapuaDataMessage.setDeviceId(device != null ? device.getId() : null);
            kapuaDataMessage.setClientId(kuraMessage.getChannel().getClientId());
            kapuaDataMessage.setChannel(kapuaDataChannel);
            kapuaDataMessage.setPayload(kapuaDataPayload);
            kapuaDataMessage.setCapturedOn(kuraMessage.getPayload().getTimestamp());
            kapuaDataMessage.setSentOn(kuraMessage.getPayload().getTimestamp());
            kapuaDataMessage.setReceivedOn(kuraMessage.getTimestamp());
            kapuaDataMessage.setPosition(TranslatorKuraKapuaUtils.translate(kuraMessage.getPayload().getPosition()));

            // Return Kapua Message
            return kapuaDataMessage;
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, kuraMessage);
        }
    }

    private KapuaDataChannel translate(KuraDataChannel kuraChannel) {
        KapuaDataChannel kapuaChannel = DATA_MESSAGE_FACTORY.newKapuaDataChannel();
        kapuaChannel.setSemanticParts(kuraChannel.getSemanticParts());

        // Return Kapua Channel
        return kapuaChannel;
    }

    private KapuaDataPayload translate(KuraDataPayload kuraPayload) {
        KapuaDataPayload kapuaPayload = DATA_MESSAGE_FACTORY.newKapuaDataPayload();

        if (!kuraPayload.getMetrics().isEmpty()) {
            kapuaPayload.setMetrics(kuraPayload.getMetrics());
        }

        if (kuraPayload.hasBody()) {
            kapuaPayload.setBody(kuraPayload.getBody());
        }

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
