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
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsPayload;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaAppsChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaAppsMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaAppsPayloadImpl;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsPayload;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;

/**
 * Messages translator implementation from {@link KuraAppsMessage} to {@link KapuaAppsMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorLifeAppsKuraKapua extends Translator<KuraAppsMessage, KapuaAppsMessage> {

    @Override
    public KapuaAppsMessage translate(KuraAppsMessage kuraAppsMessage)
            throws KapuaException {
        KapuaAppsMessage kapuaAppsMessage = new KapuaAppsMessageImpl();
        kapuaAppsMessage.setChannel(translate(kuraAppsMessage.getChannel()));
        kapuaAppsMessage.setPayload(translate(kuraAppsMessage.getPayload()));

        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.findByName(kuraAppsMessage.getChannel().getScope());

        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, kuraAppsMessage.getChannel().getScope());
        }

        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        Device device = deviceRegistryService.findByClientId(account.getId(), kuraAppsMessage.getChannel().getClientId());
        if (device == null) {
            throw new KapuaEntityNotFoundException(Device.class.toString(), kuraAppsMessage.getChannel().getClientId());
        }

        kapuaAppsMessage.setDeviceId(device.getId());
        kapuaAppsMessage.setScopeId(account.getId());
        kapuaAppsMessage.setCapturedOn(kuraAppsMessage.getPayload().getTimestamp());
        kapuaAppsMessage.setSentOn(kuraAppsMessage.getPayload().getTimestamp());
        kapuaAppsMessage.setReceivedOn(kuraAppsMessage.getTimestamp());
        kapuaAppsMessage.setPosition(TranslatorKuraKapuaUtils.translate(kuraAppsMessage.getPayload().getPosition()));

        return kapuaAppsMessage;
    }

    private KapuaAppsChannel translate(KuraAppsChannel kuraAppsChannel)
            throws KapuaException {
        KapuaAppsChannel kapuaAppsChannel = new KapuaAppsChannelImpl();
        kapuaAppsChannel.setClientId(kuraAppsChannel.getClientId());
        return kapuaAppsChannel;
    }

    private KapuaAppsPayload translate(KuraAppsPayload kuraAppsPayload)
            throws KapuaException {
        return new KapuaAppsPayloadImpl(
                kuraAppsPayload.getUptime(),
                kuraAppsPayload.getDisplayName(),
                kuraAppsPayload.getModelName(),
                kuraAppsPayload.getModelId(),
                kuraAppsPayload.getPartNumber(),
                kuraAppsPayload.getSerialNumber(),
                kuraAppsPayload.getFirmware(),
                kuraAppsPayload.getFirmwareVersion(),
                kuraAppsPayload.getBios(),
                kuraAppsPayload.getBiosVersion(),
                kuraAppsPayload.getOs(),
                kuraAppsPayload.getOsVersion(),
                kuraAppsPayload.getJvm(),
                kuraAppsPayload.getJvmVersion(),
                kuraAppsPayload.getJvmProfile(),
                kuraAppsPayload.getContainerFramework(),
                kuraAppsPayload.getContainerFrameworkVersion(),
                kuraAppsPayload.getApplicationFramework(),
                kuraAppsPayload.getApplicationFrameworkVersion(),
                kuraAppsPayload.getConnectionInterface(),
                kuraAppsPayload.getConnectionIp(),
                kuraAppsPayload.getAcceptEncoding(),
                kuraAppsPayload.getApplicationIdentifiers(),
                kuraAppsPayload.getAvailableProcessors(),
                kuraAppsPayload.getTotalMemory(),
                kuraAppsPayload.getOsArch(),
                kuraAppsPayload.getModemImei(),
                kuraAppsPayload.getModemImsi(),
                kuraAppsPayload.getModemIccid());
    }

    @Override
    public Class<KuraAppsMessage> getClassFrom() {
        return KuraAppsMessage.class;
    }

    @Override
    public Class<KapuaAppsMessage> getClassTo() {
        return KapuaAppsMessage.class;
    }

}
