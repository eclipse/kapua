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
package org.eclipse.kapua.translator.kapua.kura;

import java.util.Date;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.KuraChannel;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.kura.KuraPosition;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;

/**
 * Messages translator implementation from {@link KapuaMessage} to {@link KuraMessage}
 * 
 * @since 1.0
 *
 */
@SuppressWarnings("rawtypes")
public class TranslatorDataKapuaKura extends Translator<KapuaMessage, KuraMessage> {

    @Override
    public KuraMessage translate(KapuaMessage kapuaMessage)
            throws KapuaException {
        //
        // Kura channel
        KuraChannel kuraChannel = translate(kapuaMessage.getChannel());

        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);

        Account account = accountService.find(kapuaMessage.getScopeId());
        Device device = deviceRegistryService.find(account.getId(),
                kapuaMessage.getDeviceId());

        kuraChannel.setScope(account.getName());
        kuraChannel.setClientId(device.getClientId());

        //
        // Kura payload
        KuraPayload kuraPayload = translate(kapuaMessage.getPayload());
        kuraPayload.setPosition(translate(kapuaMessage.getPosition()));

        //
        // Kura Message
        return new KuraMessage<KuraChannel, KuraPayload>(kuraChannel,
                new Date(),
                kuraPayload);
    }

    private KuraDataChannel translate(KapuaChannel kapuaChannel) throws KapuaException {
        KuraDataChannel kuraChannel = new KuraDataChannel();
        kuraChannel.setSemanticChannelParts(kapuaChannel.getSemanticParts());

        //
        // Return Kura Channel
        return kuraChannel;
    }

    private KuraPayload translate(KapuaPayload kapuaPayload) throws KapuaException {
        KuraPayload kuraPayload = new KuraPayload();
        kuraPayload.getMetrics().putAll(kapuaPayload.getProperties());
        kuraPayload.setBody(kapuaPayload.getBody());

        //
        // Return Kura Payload
        return kuraPayload;
    }

    private KuraPosition translate(KapuaPosition position) {
        KuraPosition kuraPosition = new KuraPosition();

        kuraPosition.setAltitude(position.getAltitude());
        kuraPosition.setHeading(position.getHeading());
        kuraPosition.setLatitude(position.getLatitude());
        kuraPosition.setLongitude(position.getLongitude());
        kuraPosition.setPrecision(position.getPrecision());
        kuraPosition.setSatellites(position.getSatellites());
        kuraPosition.setSpeed(position.getSpeed());
        kuraPosition.setStatus(position.getStatus());
        kuraPosition.setTimestamp(position.getTimestamp());

        //
        // Return Kura Position
        return kuraPosition;
    }

    @Override
    public Class<KapuaMessage> getClassFrom() {
        return KapuaMessage.class;
    }

    @Override
    public Class<KuraMessage> getClassTo() {
        return KuraMessage.class;
    }

}
