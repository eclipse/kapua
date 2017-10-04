/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *      Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.DeviceCall;
import org.eclipse.kapua.service.device.call.kura.exception.KuraMqttDeviceCallErrorCodes;
import org.eclipse.kapua.service.device.call.kura.exception.KuraMqttDeviceCallException;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestPayload;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraMessage;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.TransportClientFactory;
import org.eclipse.kapua.transport.TransportFacade;
import org.eclipse.kapua.transport.message.TransportMessage;

/**
 * Kura device call implementation.
 */
@SuppressWarnings("rawtypes")
public class KuraDeviceCallImpl implements DeviceCall<KuraRequestMessage, KuraResponseMessage> {

    @Override
    public KuraResponseMessage create(KuraRequestMessage requestMessage, Long timeout)
            throws KapuaException {
        return send(requestMessage, timeout);
    }

    @Override
    public KuraResponseMessage read(KuraRequestMessage requestMessage, Long timeout)
            throws KapuaException {
        return send(requestMessage, timeout);
    }

    @Override
    public KuraResponseMessage options(KuraRequestMessage requestMessage, Long timeout)
            throws KapuaException {
        return send(requestMessage, timeout);
    }

    @Override
    public KuraResponseMessage delete(KuraRequestMessage requestMessage, Long timeout)
            throws KapuaException {
        return send(requestMessage, timeout);
    }

    @Override
    public KuraResponseMessage execute(KuraRequestMessage requestMessage, Long timeout)
            throws KapuaException {
        return send(requestMessage, timeout);
    }

    @Override
    public KuraResponseMessage write(KuraRequestMessage requestMessage, Long timeout)
            throws KapuaException {
        return send(requestMessage, timeout);
    }

    @SuppressWarnings({ "unchecked" })
    private KuraResponseMessage send(KuraRequestMessage requestMessage, Long timeout)
            throws KuraMqttDeviceCallException {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        AccountService accountService = locator.getService(AccountService.class);
        KuraResponseMessage response = null;
        TransportFacade transportFacade = null;
        try {
            Account account = accountService.findByName(requestMessage.getChannel().getScope());
            Device device = deviceRegistryService.findByClientId(account.getId(), requestMessage.getChannel().getClientId());
            String serverIp = device.getConnection().getServerIp();

            //
            // Borrow a KapuaClient
            transportFacade = borrowClient(serverIp);

            //
            // Get Kura to transport translator for the request and vice versa
            Translator translatorKuraTransport = getTranslator(KuraRequestMessage.class, transportFacade.getMessageClass());
            Translator translatorTransportKura = getTranslator(transportFacade.getMessageClass(), KuraResponseMessage.class);

            //
            // Make the request
            // Add requestId and requesterClientId to both payload and channel if response is expected
            // Note: Adding to both payload and channel to let the translator choose what to do base on the transport used.
            KuraRequestChannel requestChannel = requestMessage.getChannel();
            KuraRequestPayload requestPayload = requestMessage.getPayload();
            if (timeout != null) {
                // FIXME: create an utilty class to use the same synchronized random instance to avoid duplicates
                Random r = new Random();
                String requestId = String.valueOf(r.nextLong());

                requestChannel.setRequestId(requestId);
                requestChannel.setRequesterClientId(transportFacade.getClientId());

                requestPayload.setRequestId(requestId);
                requestPayload.setRequesterClientId(transportFacade.getClientId());
            }

            //
            // Do send
            try {
                // Set current timestamp
                requestMessage.setTimestamp(new Date());

                // Send
                TransportMessage transportResponseMessage = transportFacade.sendSync((TransportMessage) translatorKuraTransport.translate(requestMessage), timeout);

                // Translate response
                response = (KuraResponseMessage) translatorTransportKura.translate(transportResponseMessage);
            } catch (KapuaException e) {
                throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CLIENT_SEND_ERROR, e);
            }
        } catch (KapuaException ke) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR, ke);
        } finally {
            if (transportFacade != null) {
                transportFacade.clean();
            }
        }

        return response;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<KuraMessage> getBaseMessageClass() {
        return KuraMessage.class;
    }

    //
    // Private methods
    //
    private TransportFacade borrowClient(String serverIp)
            throws KuraMqttDeviceCallException {
        TransportFacade transportFacade;
        Map<String, Object> configParameters = new HashMap<>();
        configParameters.put("serverIp", serverIp);
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TransportClientFactory transportClientFactory = locator.getFactory(TransportClientFactory.class);
            transportFacade = transportClientFactory.getFacade(configParameters);
        } catch (Exception e) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR, e);
        }
        return transportFacade;
    }

    @SuppressWarnings("unchecked")
    private Translator getTranslator(Class from, Class to)
            throws KuraMqttDeviceCallException {
        Translator translator;
        try {
            translator = Translator.getTranslatorFor(from, to);
        } catch (KapuaException e) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR, e);
        }
        return translator;
    }
}
