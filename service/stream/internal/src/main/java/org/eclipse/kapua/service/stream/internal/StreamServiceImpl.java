/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.stream.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.message.Message;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.service.device.call.kura.exception.KuraMqttDeviceCallErrorCodes;
import org.eclipse.kapua.service.device.call.kura.exception.KuraMqttDeviceCallException;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.stream.StreamService;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.TransportClientFactory;
import org.eclipse.kapua.transport.TransportFacade;
import org.eclipse.kapua.transport.message.TransportMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@KapuaProvider
public class StreamServiceImpl implements StreamService {

    @Override
    public KapuaResponseMessage<?, ?> publish(KapuaDataMessage requestMessage, Long timeout)
            throws KapuaException {
        TransportFacade<?, ?, TransportMessage<?, ?>, ?> transportFacade = null;
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        try {
            ArgumentValidator.notNull(requestMessage.getClientId(), "clientId");
            ArgumentValidator.notNull(requestMessage.getScopeId(), "scopeId");

            Device device = deviceRegistryService.find(requestMessage.getScopeId(), requestMessage.getDeviceId());
            String brokerUri = device.getConnection().getServerIp();

            //
            // Borrow a KapuaClient
            transportFacade = borrowClient(brokerUri);

            //
            // Get Kura to transport translator for the request and vice versa
            Translator<KapuaDataMessage, KuraDataMessage> translatorKapuaKura = getTranslator(KapuaDataMessage.class, KuraDataMessage.class);
            Translator<KuraDataMessage, ?> translatorKuraTransport = getTranslator(KuraDataMessage.class, transportFacade.getMessageClass());

            KuraDataMessage kuraDataMessage = translatorKapuaKura.translate(requestMessage);

            //
            // Do send
            try {
                // Set current timestamp
                kuraDataMessage.setTimestamp(new Date());

                // Send
                transportFacade.sendAsync((TransportMessage<?, ?>) translatorKuraTransport.translate(kuraDataMessage));

            } catch (KapuaException e) {
                throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CLIENT_SEND_ERROR,
                        e,
                        (Object[]) null);
            }
        } catch (KapuaException ke) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR,
                    ke,
                    (Object[]) null);
        } finally {
            if (transportFacade != null) {
                transportFacade.clean();
            }
        }

        return null;
    }

    //
    // Private methods
    //
    @SuppressWarnings("unchecked")
    private TransportFacade<?, ?, TransportMessage<?, ?>, ?> borrowClient(String brokerUri)
            throws KuraMqttDeviceCallException {
        TransportFacade<?, ?, TransportMessage<?, ?>, ?> transportFacade;
        Map<String, Object> configParameters = new HashMap<>();
        configParameters.put("brokerUri", brokerUri);
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TransportClientFactory<?, ?, ?, ?, ?, ?> transportClientFactory = locator.getFactory(TransportClientFactory.class);

            transportFacade = (TransportFacade<?, ?, TransportMessage<?, ?>, ?>) transportClientFactory.getFacade(configParameters);
        } catch (Exception e) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR,
                    e,
                    (Object[]) null);
        }
        return transportFacade;
    }

    private <T1 extends Message<?, ?>, T2 extends Message<?, ?>> Translator<T1, T2> getTranslator(Class<T1> from, Class<T2> to)
            throws KuraMqttDeviceCallException {
        Translator<T1, T2> translator;
        try {
            translator = Translator.getTranslatorFor(from, to);
        } catch (KapuaException e) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR,
                    e,
                    (Object[]) null);
        }
        return translator;
    }
}
