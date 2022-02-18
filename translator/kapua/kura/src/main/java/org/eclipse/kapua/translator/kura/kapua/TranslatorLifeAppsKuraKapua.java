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
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;

/**
 * {@link Translator} implementation from {@link KuraAppsMessage} to {@link KapuaAppsMessage}
 *
 * @since 1.0.0
 */
public class TranslatorLifeAppsKuraKapua extends Translator<KuraAppsMessage, KapuaAppsMessage> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);
    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);

    @Override
    public KapuaAppsMessage translate(KuraAppsMessage kuraAppsMessage) throws TranslateException {
        try {
            KapuaAppsMessage kapuaAppsMessage = new KapuaAppsMessageImpl();
            kapuaAppsMessage.setChannel(translate(kuraAppsMessage.getChannel()));
            kapuaAppsMessage.setPayload(translate(kuraAppsMessage.getPayload()));

            Account account = ACCOUNT_SERVICE.findByName(kuraAppsMessage.getChannel().getScope());
            if (account == null) {
                throw new KapuaEntityNotFoundException(Account.TYPE, kuraAppsMessage.getChannel().getScope());
            }

            Device device = DEVICE_REGISTRY_SERVICE.findByClientId(account.getId(), kuraAppsMessage.getChannel().getClientId());
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
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, kuraAppsMessage);
        }
    }

    private KapuaAppsChannel translate(KuraAppsChannel kuraAppsChannel) {
        KapuaAppsChannel kapuaAppsChannel = new KapuaAppsChannelImpl();
        kapuaAppsChannel.setClientId(kuraAppsChannel.getClientId());
        return kapuaAppsChannel;
    }

    private KapuaAppsPayload translate(KuraAppsPayload kuraAppsPayload) {
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
                kuraAppsPayload.getModemIccid(),
                kuraAppsPayload.getExtendedProperties());
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
