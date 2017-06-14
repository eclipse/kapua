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
 *******************************************************************************/
package org.eclipse.kapua.service.stream.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.service.device.call.kura.exception.KuraMqttDeviceCallErrorCodes;
import org.eclipse.kapua.service.device.call.kura.exception.KuraMqttDeviceCallException;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;
import org.eclipse.kapua.service.stream.StreamService;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.TransportClientFactory;
import org.eclipse.kapua.transport.TransportFacade;
import org.eclipse.kapua.transport.message.TransportMessage;

import java.util.Date;

@KapuaProvider
public class StreamServiceImpl implements StreamService {

    @Override public KapuaResponseMessage publish(KapuaDataMessage requestMessage, Long timeout)
            throws KapuaException {
        TransportFacade transportFacade = null;
        try {
            ArgumentValidator.notNull(requestMessage.getClientId(), "clientId");
            ArgumentValidator.notNull(requestMessage.getScopeId(), "scopeId");

            //
            // Borrow a KapuaClient
            transportFacade = borrowClient();

            //
            // Get Kura to transport translator for the request and vice versa
            Translator<KapuaDataMessage, KuraDataMessage> translatorKapuaKura = getTranslator(KapuaDataMessage.class, KuraDataMessage.class);
            Translator translatorKuraTransport = getTranslator(KuraDataMessage.class, transportFacade.getMessageClass());

            KuraDataMessage kuraDataMessage = translatorKapuaKura.translate(requestMessage);

            //
            // Do send
            try {
                // Set current timestamp
                kuraDataMessage.setTimestamp(new Date());

                // Send
                transportFacade.sendAsync((TransportMessage) translatorKuraTransport.translate(kuraDataMessage));

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
    private TransportFacade borrowClient()
            throws KuraMqttDeviceCallException {
        TransportFacade transportFacade;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TransportClientFactory transportClientFactory = locator.getFactory(TransportClientFactory.class);
            transportFacade = transportClientFactory.getFacade();
        } catch (Exception e) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR,
                    e,
                    (Object[]) null);
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
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR,
                    e,
                    (Object[]) null);
        }
        return translator;
    }
}
