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
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectPayload;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaDisconnectChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaDisconnectMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaDisconnectPayloadImpl;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectPayload;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;

/**
 * Messages translator implementation from {@link KuraDisconnectMessage} to {@link KapuaDisconnectMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorLifeDisconnectKuraKapua extends Translator<KuraDisconnectMessage, KapuaDisconnectMessage>
{

    @Override
    public KapuaDisconnectMessage translate(KuraDisconnectMessage kuraDisconnectMessage)
        throws KapuaException
    {
        KapuaDisconnectMessage kapuaDisconnectMessage = new KapuaDisconnectMessageImpl();
        kapuaDisconnectMessage.setChannel(translate(kuraDisconnectMessage.getChannel()));
        kapuaDisconnectMessage.setPayload(translate(kuraDisconnectMessage.getPayload()));

        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.findByName(kuraDisconnectMessage.getChannel().getScope());

        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        Device device = deviceRegistryService.findByClientId(account.getId(), kuraDisconnectMessage.getChannel().getClientId());
        if (device == null) {
        	throw new KapuaEntityNotFoundException(Device.class.toString(), kuraDisconnectMessage.getChannel().getClientId());
        }

        kapuaDisconnectMessage.setDeviceId(device.getId());
        kapuaDisconnectMessage.setScopeId(account.getId());
        kapuaDisconnectMessage.setCapturedOn(kuraDisconnectMessage.getPayload().getTimestamp());
        kapuaDisconnectMessage.setSentOn(kuraDisconnectMessage.getPayload().getTimestamp());
        kapuaDisconnectMessage.setReceivedOn(kuraDisconnectMessage.getTimestamp());
        kapuaDisconnectMessage.setPosition(TranslatorKuraKapuaUtils.translate(kuraDisconnectMessage.getPayload().getPosition()));

        return kapuaDisconnectMessage;
    }

    private KapuaDisconnectChannel translate(KuraDisconnectChannel kuraDisconnectChannel)
        throws KapuaException
    {
        KapuaDisconnectChannel kapuaDisconnectChannel = new KapuaDisconnectChannelImpl();
        kapuaDisconnectChannel.setClientId(kuraDisconnectChannel.getClientId());
        return kapuaDisconnectChannel;
    }

    private KapuaDisconnectPayload translate(KuraDisconnectPayload kuraDisconnectPayload)
        throws KapuaException
    {
        return new KapuaDisconnectPayloadImpl(kuraDisconnectPayload.getUptime(),
                                              kuraDisconnectPayload.getDisplayName());
    }

    @Override
    public Class<KuraDisconnectMessage> getClassFrom()
    {
        return KuraDisconnectMessage.class;
    }

    @Override
    public Class<KapuaDisconnectMessage> getClassTo()
    {
        return KapuaDisconnectMessage.class;
    }

}
