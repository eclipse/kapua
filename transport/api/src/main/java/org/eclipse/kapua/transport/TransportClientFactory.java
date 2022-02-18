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
package org.eclipse.kapua.transport;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.transport.exception.TransportClientGetException;
import org.eclipse.kapua.transport.message.TransportChannel;
import org.eclipse.kapua.transport.message.TransportMessage;
import org.eclipse.kapua.transport.message.TransportPayload;

import java.util.Map;

/**
 * Factory class for {@link TransportFacade} related objects.
 *
 * @param <C>  The {@link TransportChannel} implementation class.
 * @param <P>  The {@link TransportPayload} implementation class.
 * @param <MQ> The {@link TransportMessage} implementation class.
 * @param <MS> The {@link TransportMessage} implementation class.
 * @param <T>  The {@link TransportFacade} implementation class.
 * @param <CO> The {@link TransportClientConnectOptions} implementation class.
 * @author alberto.codutti
 * @since 1.0.0
 */
public interface TransportClientFactory<C extends TransportChannel, P extends TransportPayload, MQ extends TransportMessage<C, P>, MS extends TransportMessage<C, P>, T extends TransportFacade<C, P, MQ, MS>, CO extends TransportClientConnectOptions>
        extends KapuaObjectFactory {

    /**
     * Gets an instance of the {@link TransportFacade} implementing class.
     * <p>
     * The instance is ready to be used by the device layer.
     *
     * @param configParameters a {@link Map} containing optional config values for the facade
     * @return An instance of the {@link TransportFacade} implementing class.
     * @throws TransportClientGetException If error occurs when getting the {@link TransportFacade}.
     * @since 1.0.0
     */
    T getFacade(Map<String, Object> configParameters) throws TransportClientGetException;

    /**
     * Gets an instance of the {@link TransportClientConnectOptions} implementing class.
     *
     * @return An instance of the {@link TransportClientConnectOptions} implementing class.
     * @throws KapuaException If error occurs instantiating the {@link TransportClientConnectOptions}.
     * @since 1.0.0
     */
    CO newConnectOptions() throws KapuaException;
}
