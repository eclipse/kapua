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
 *******************************************************************************/
package org.eclipse.kapua.transport;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.transport.message.TransportChannel;
import org.eclipse.kapua.transport.message.TransportMessage;
import org.eclipse.kapua.transport.message.TransportPayload;

import java.util.Map;

/**
 * Factory class for {@link TransportFacade} related objects
 *
 * @param <D>  FIXME [javadoc] document generic
 * @param <P>  FIXME [javadoc] document generic
 * @param <MQ> The {@link TransportMessage} implementation class that the implementation of {@link org.eclipse.kapua.transport} API uses for request messages to the device.
 * @param <MS> The {@link TransportMessage} implementation class that the implementation of {@link org.eclipse.kapua.transport} API uses for response messages to the device.
 * @param <C>  FIXME [javadoc] document generic
 * @param <CO> The {@link TransportClientConnectOptions} implementation class that the implementation of {@link org.eclipse.kapua.transport} API uses.
 * @author alberto.codutti
 * @since 1.0.0
 */
public interface TransportClientFactory<D extends TransportChannel, P extends TransportPayload, MQ extends TransportMessage<D, P>, MS extends TransportMessage<D, P>, C extends TransportFacade<D, P, MQ, MS>, CO extends TransportClientConnectOptions>
        extends KapuaObjectFactory {

    /**
     * Gets an instance of the {@link TransportFacade} implementing class.
     * <p>
     * <p>
     * The instance is ready to be used by the device layer.
     * </p>
     *
     * @return An instance of the {@link TransportFacade} implementing class.
     * @param configParameters a {@link Map} containing optional config values for the facade
     * @throws KapuaException FIXME [javadoc] document exception
     */
    public C getFacade(Map<String, Object> configParameters)
            throws KapuaException;

    /**
     * Gets an instance of the {@link TransportClientConnectOptions} implementing class.
     *
     * @return An instance of the {@link TransportClientConnectOptions} implementing class.
     * @throws KapuaException FIXME [javadoc] document exception
     */
    public CO newConnectOptions()
            throws KapuaException;
}
