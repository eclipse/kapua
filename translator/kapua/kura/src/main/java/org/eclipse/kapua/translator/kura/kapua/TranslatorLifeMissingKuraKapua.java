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
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingPayload;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaMissingChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaMissingMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaMissingPayloadImpl;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingPayload;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;

/**
 * {@link Translator} implementation from {@link KuraMissingMessage} to {@link KapuaMissingMessage}
 *
 * @since 1.0.0
 */
public class TranslatorLifeMissingKuraKapua extends Translator<KuraMissingMessage, KapuaMissingMessage> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);
    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);

    @Override
    public KapuaMissingMessage translate(KuraMissingMessage kuraMissingMessage) throws TranslateException {
        try {
            KapuaMissingMessage kapuaMissingMessage = new KapuaMissingMessageImpl();
            kapuaMissingMessage.setChannel(translate(kuraMissingMessage.getChannel()));
            kapuaMissingMessage.setPayload(translate(kuraMissingMessage.getPayload()));

            Account account = ACCOUNT_SERVICE.findByName(kuraMissingMessage.getChannel().getScope());
            if (account == null) {
                throw new KapuaEntityNotFoundException(Account.TYPE, kuraMissingMessage.getChannel().getScope());
            }

            Device device = DEVICE_REGISTRY_SERVICE.findByClientId(account.getId(), kuraMissingMessage.getChannel().getClientId());
            if (device == null) {
                throw new KapuaEntityNotFoundException(Device.class.toString(), kuraMissingMessage.getChannel().getClientId());
            }

            kapuaMissingMessage.setDeviceId(device.getId());
            kapuaMissingMessage.setScopeId(account.getId());
            kapuaMissingMessage.setCapturedOn(kuraMissingMessage.getPayload().getTimestamp());
            kapuaMissingMessage.setSentOn(kuraMissingMessage.getPayload().getTimestamp());
            kapuaMissingMessage.setReceivedOn(kuraMissingMessage.getTimestamp());
            kapuaMissingMessage.setPosition(TranslatorKuraKapuaUtils.translate(kuraMissingMessage.getPayload().getPosition()));

            return kapuaMissingMessage;
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, kuraMissingMessage);
        }
    }

    private KapuaMissingChannel translate(KuraMissingChannel kuraMissingChannel) {
        KapuaMissingChannel kapuaMissingChannel = new KapuaMissingChannelImpl();
        kapuaMissingChannel.setClientId(kuraMissingChannel.getClientId());
        return kapuaMissingChannel;
    }

    private KapuaMissingPayload translate(KuraMissingPayload kuraMissingPayload) {
        KapuaMissingPayload kapuaMissingPayload = new KapuaMissingPayloadImpl();
        kapuaMissingPayload.setBody(kuraMissingPayload.getBody());
        kapuaMissingPayload.setMetrics(kuraMissingPayload.getMetrics());
        return kapuaMissingPayload;
    }

    @Override
    public Class<KuraMissingMessage> getClassFrom() {
        return KuraMissingMessage.class;
    }

    @Override
    public Class<KapuaMissingMessage> getClassTo() {
        return KapuaMissingMessage.class;
    }
}
