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
 *
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthPayloadImpl;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthPayload;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;

/**
 * Messages translator implementation from {@link KuraBirthMessage} to {@link KapuaBirthMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorLifeBirthKuraKapua extends Translator<KuraBirthMessage, KapuaBirthMessage> {

    @Override
    public KapuaBirthMessage translate(KuraBirthMessage kuraBirthMessage)
            throws KapuaException {
        KapuaBirthMessage kapuaBirthMessage = new KapuaBirthMessageImpl();
        kapuaBirthMessage.setChannel(translate(kuraBirthMessage.getChannel()));
        kapuaBirthMessage.setPayload(translate(kuraBirthMessage.getPayload()));

        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.findByName(kuraBirthMessage.getChannel().getScope());

        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, kuraBirthMessage.getChannel().getScope());
        }

        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        Device device = deviceRegistryService.findByClientId(account.getId(), kuraBirthMessage.getChannel().getClientId());

        kapuaBirthMessage.setScopeId(account.getId());
        if (device != null) {
            kapuaBirthMessage.setDeviceId(device.getId());
        } else {
            kapuaBirthMessage.setClientId(kuraBirthMessage.getChannel().getClientId());
        }
        kapuaBirthMessage.setCapturedOn(kuraBirthMessage.getPayload().getTimestamp());
        kapuaBirthMessage.setSentOn(kuraBirthMessage.getPayload().getTimestamp());
        kapuaBirthMessage.setReceivedOn(kuraBirthMessage.getTimestamp());
        kapuaBirthMessage.setPosition(TranslatorKuraKapuaUtils.translate(kuraBirthMessage.getPayload().getPosition()));

        return kapuaBirthMessage;
    }

    private KapuaBirthChannel translate(KuraBirthChannel kuraBirthChannel)
            throws KapuaException {
        KapuaBirthChannel kapuaBirthChannel = new KapuaBirthChannelImpl();
        kapuaBirthChannel.setClientId(kuraBirthChannel.getClientId());
        return kapuaBirthChannel;
    }

    private KapuaBirthPayload translate(KuraBirthPayload kuraBirthPayload)
            throws KapuaException {
        return new KapuaBirthPayloadImpl(
                kuraBirthPayload.getUptime(),
                kuraBirthPayload.getDisplayName(),
                kuraBirthPayload.getModelName(),
                kuraBirthPayload.getModelId(),
                kuraBirthPayload.getPartNumber(),
                kuraBirthPayload.getSerialNumber(),
                kuraBirthPayload.getFirmware(),
                kuraBirthPayload.getFirmwareVersion(),
                kuraBirthPayload.getBios(),
                kuraBirthPayload.getBiosVersion(),
                kuraBirthPayload.getOs(),
                kuraBirthPayload.getOsVersion(),
                kuraBirthPayload.getJvm(),
                kuraBirthPayload.getJvmVersion(),
                kuraBirthPayload.getJvmProfile(),
                kuraBirthPayload.getContainerFramework(),
                kuraBirthPayload.getContainerFrameworkVersion(),
                kuraBirthPayload.getApplicationFramework(),
                kuraBirthPayload.getApplicationFrameworkVersion(),
                kuraBirthPayload.getConnectionInterface(),
                kuraBirthPayload.getConnectionIp(),
                kuraBirthPayload.getAcceptEncoding(),
                kuraBirthPayload.getApplicationIdentifiers(),
                kuraBirthPayload.getAvailableProcessors(),
                kuraBirthPayload.getTotalMemory(),
                kuraBirthPayload.getOsArch(),
                kuraBirthPayload.getModemImei(),
                kuraBirthPayload.getModemImsi(),
                kuraBirthPayload.getModemIccid());
    }

    @Override
    public Class<KuraBirthMessage> getClassFrom() {
        return KuraBirthMessage.class;
    }

    @Override
    public Class<KapuaBirthMessage> getClassTo() {
        return KapuaBirthMessage.class;
    }

}
