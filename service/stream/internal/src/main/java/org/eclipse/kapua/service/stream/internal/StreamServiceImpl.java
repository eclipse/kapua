/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.message.Message;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.call.kura.exception.KuraMqttDeviceCallErrorCodes;
import org.eclipse.kapua.service.device.call.kura.exception.KuraMqttDeviceCallException;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoAttributes;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoQuery;
import org.eclipse.kapua.service.endpoint.EndpointInfoService;
import org.eclipse.kapua.service.stream.StreamDomains;
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

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);

    private static final EndpointInfoService ENDPOINT_INFO_SERVICE = LOCATOR.getService(EndpointInfoService.class);
    private static final EndpointInfoFactory ENDPOINT_INFO_FACTORY = LOCATOR.getFactory(EndpointInfoFactory.class);

    @Override
    public KapuaResponseMessage<?, ?> publish(KapuaDataMessage requestMessage, Long timeout)
            throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(requestMessage.getScopeId(), "dataMessage.scopeId");
        ArgumentValidator.notNull(requestMessage.getChannel(), "dataMessage.channel");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(StreamDomains.STREAM_DOMAIN, Actions.write, requestMessage.getScopeId()));

        //
        // Check Device existence
        Device device = checkDeviceInfo(requestMessage);

        // Resolve URI
        String serverURI;
        if (device != null) {
            serverURI = device.getConnection().getServerIp();
        } else {
            serverURI = getEndpointInfoDNS(requestMessage);
        }

        //
        // Do publish
        TransportFacade<?, ?, TransportMessage<?, ?>, ?> transportFacade = null;
        try {
            //
            // Borrow a KapuaClient
            transportFacade = borrowClient(serverURI);

            //
            // Get Kura to transport translator for the request and vice versa
            Translator<KapuaDataMessage, KuraDataMessage> translatorKapuaKura = getTranslator(KapuaDataMessage.class, KuraDataMessage.class);
            Translator<KuraDataMessage, ?> translatorKuraTransport = getTranslator(KuraDataMessage.class, transportFacade.getMessageClass());

            KuraDataMessage kuraDataMessage = translatorKapuaKura.translate(requestMessage);

            //
            // Do send

            // Set current timestamp
            kuraDataMessage.setTimestamp(new Date());

            // Send
            transportFacade.sendAsync((TransportMessage<?, ?>) translatorKuraTransport.translate(kuraDataMessage));

        } catch (KapuaException ke) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR, ke, (Object[]) null);
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

    /**
     * Checks the {@link KapuaDataMessage#getDeviceId()} and {@link KapuaDataMessage#getClientId()}.
     * <p>
     * At least one of the two must be present if not both:
     * <ul>
     *     <li>Only {@link KapuaDataMessage#getClientId()} is specified: The given clientId will be resolved to the matching {@link Device}. If not found {@code null} will be returned</li>
     *     <li>Only {@link KapuaDataMessage#getDeviceId()} is specified: The gived {@link Device#getId()} will be resolved to the {@link Device} and the {@link KapuaDataMessage#getClientId()} will be populated with its value</li>
     *     <li>Both specified: The {@link Device#getClientId()} and the {@link KapuaDataMessage#getClientId()} must match</li>
     * </ul>
     *
     * @param requestMessage
     * @return
     * @throws KapuaException
     * @since 1.2.0
     */
    private Device checkDeviceInfo(KapuaDataMessage requestMessage) throws KapuaException {
        Device device = null;
        if (requestMessage.getDeviceId() != null) {
            device = DEVICE_REGISTRY_SERVICE.find(requestMessage.getScopeId(), requestMessage.getDeviceId());

            if (device == null) {
                throw new KapuaEntityNotFoundException(Device.TYPE, requestMessage.getDeviceId());
            } else {
                if (requestMessage.getClientId() == null) {
                    requestMessage.setClientId(device.getClientId());
                } else if (!device.getClientId().equals(requestMessage.getClientId())) {
                    throw new KapuaIllegalArgumentException("dataMessage.clientId", requestMessage.getClientId());
                }
            }
        }
        return device;
    }

    /**
     * Looko for the available {@link EndpointInfo} with {@link EndpointInfo#getSchema()} = "mqtt"
     *
     * @param requestMessage
     * @return
     * @throws KapuaException
     * @since 1.2.0
     */
    private String getEndpointInfoDNS(KapuaDataMessage requestMessage) throws KapuaException {
        String serverURI;
        EndpointInfoQuery query = ENDPOINT_INFO_FACTORY.newQuery(requestMessage.getScopeId());
        query.setPredicate(
                query.andPredicate(
                        query.attributePredicate(EndpointInfoAttributes.SCHEMA, "mqtt"),
                        query.attributePredicate(EndpointInfoAttributes.SECURE, Boolean.FALSE)
                )
        );

        EndpointInfo endpointInfo = ENDPOINT_INFO_SERVICE.query(query).getFirstItem();
        if (endpointInfo == null) {
            throw KapuaException.internalError("No endpoint defined!");
        }
        serverURI = endpointInfo.getDns();
        return serverURI;
    }


    /**
     * @param serverUri
     * @return
     * @throws KuraMqttDeviceCallException
     * @since 1.0.0
     */
    private TransportFacade<?, ?, TransportMessage<?, ?>, ?> borrowClient(String serverUri)
            throws KuraMqttDeviceCallException {
        TransportFacade<?, ?, TransportMessage<?, ?>, ?> transportFacade;
        Map<String, Object> configParameters = new HashMap<>();
        configParameters.put("serverAddress", serverUri);
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

    /**
     * @param from
     * @param to
     * @param <T1>
     * @param <T2>
     * @return
     * @throws KuraMqttDeviceCallException
     * @since 1.0.0
     */
    private <T1 extends Message<?, ?>, T2 extends Message<?, ?>> Translator<T1, T2> getTranslator(Class<T1> from, Class<T2> to)
            throws KuraMqttDeviceCallException {
        Translator<T1, T2> translator;
        try {
            translator = Translator.getTranslatorFor(from, to);
        } catch (KapuaException e) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR, e, (Object[]) null);
        }
        return translator;
    }
}
