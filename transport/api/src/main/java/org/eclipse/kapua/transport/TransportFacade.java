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
import org.eclipse.kapua.transport.message.TransportChannel;
import org.eclipse.kapua.transport.message.TransportMessage;
import org.eclipse.kapua.transport.message.TransportPayload;

/**
 * API to use the Transport layer for the device communication.
 *
 * @param <C>  FIXME [javadoc] document generic
 * @param <P>  FIXME [javadoc] document generic
 * @param <MQ> The {@link TransportMessage} implementation class that the implementation of {@link org.eclipse.kapua.transport} API uses for request messages to the device.
 * @param <MS> The {@link TransportMessage} implementation class that the implementation of {@link org.eclipse.kapua.transport} API uses for response messages to the device.
 * @author alberto.codutti
 * @since 1.0.0
 */
public interface TransportFacade<C extends TransportChannel, P extends TransportPayload, MQ extends TransportMessage<C, P>, MS extends TransportMessage<C, P>> {

    //
    // Message management
    //

    /**
     * Send a request message to a device waiting for the response.
     * <p>
     * The timeout is optional. If {@code null} the default one defined by the implementation will be used.
     * </p>
     *
     * @param message The request message to send.
     * @param timeout The timeout for the operation.
     * @return The response to the request message.
     * @throws KapuaException FIXME [javadoc] document exception
     * @since 1.0.0
     */
    public MS sendSync(MQ message, Long timeout)
            throws KapuaException;

    /**
     * Send a request message to a device without waiting for the response
     *
     * @param message The request message to send.
     * @throws KapuaException FIXME document exception
     * @since 1.0.0
     */
    public void sendAsync(MQ message)
            throws KapuaException;

    //
    // Utilities
    //

    /**
     * Gets the id of the instance of {@link TransportFacade}
     * FIXME [javadoc] Change it in "getId()"?
     *
     * @return The id of this {@link TransportFacade}
     * @since 1.0.0
     */
    public String getClientId();

    /**
     * Executes clean operations for this {@link TransportFacade}
     * <p>
     * This method must be called by the device layer after being used.
     * </p>
     *
     * @since 1.0.0
     */
    public void clean();

    /**
     * Returns the {@code class} of the type of {@link TransportMessage} implementation used by this implementation of the {@link TransportFacade}.
     * <p>
     * <p>
     * This is meant to be used while requesting a translator from the device layer to the transport layer.
     * </p>
     *
     * @return The {@code class} of the type of {@link TransportMessage} implementation used by this implementation of the {@link TransportFacade}.
     */
    public Class<MQ> getMessageClass();
}
