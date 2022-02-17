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

import org.eclipse.kapua.transport.exception.TransportSendException;
import org.eclipse.kapua.transport.exception.TransportTimeoutException;
import org.eclipse.kapua.transport.message.TransportChannel;
import org.eclipse.kapua.transport.message.TransportMessage;
import org.eclipse.kapua.transport.message.TransportPayload;

import org.checkerframework.checker.nullness.qual.Nullable;
import javax.validation.constraints.NotNull;

/**
 * API to use the Transport layer for the device communication.
 * <p>
 * It extends {@link AutoCloseable} {@code interface} so the user MUST invoke the {@link #close()} method after usage.
 *
 * @param <C>  The {@link TransportChannel} implementation class for the request.
 * @param <P>  The {@link TransportPayload} implementation class for the request.
 * @param <MQ> The {@link TransportMessage} implementation class for the request.
 * @param <MS> The {@link TransportMessage} implementation class for the response.
 * @author alberto.codutti
 * @see #close()
 * @since 1.0.0
 */
public interface TransportFacade<C extends TransportChannel, P extends TransportPayload, MQ extends TransportMessage<C, P>, MS extends TransportMessage<C, P>> extends AutoCloseable {

    //
    // Message management
    //

    /**
     * Send a request message to a device waiting for the response.
     * <p>
     * The timeout is optional. If {@code null} the default one defined by the implementation will be used.
     *
     * @param message The request message to send.
     * @param timeout The timeout for the operation.
     * @return The response to the request message.
     * @throws TransportTimeoutException if waiting of the response goes on timeout.
     * @throws TransportSendException    if sending the request produces any error.
     * @since 1.0.0
     */
    MS sendSync(@NotNull MQ message, @Nullable Long timeout) throws TransportTimeoutException, TransportSendException;

    /**
     * Send a request message to a device without waiting for the response
     *
     * @param message The request message to send.
     * @throws TransportTimeoutException if waiting of the response goes on timeout.
     * @throws TransportSendException    if sending the request produces any error.
     * @since 1.0.0
     */
    void sendAsync(@NotNull MQ message) throws TransportTimeoutException, TransportSendException;

    //
    // Utilities
    //

    /**
     * Gets the id of the instance of {@link TransportFacade}
     *
     * @return The id of this {@link TransportFacade}
     * @since 1.0.0
     */
    String getClientId();

    /**
     * Executes clean operations for this {@link TransportFacade}
     * <p>
     * This method must be called by the device layer after being used.
     *
     * @since 1.0.0
     * @deprecated since 1.2.0 this {@code interface} extends {@link AutoCloseable}. Please make use of {@link #close()}
     */
    @Deprecated
    void clean();

    /**
     * Executes close operations for this {@link TransportFacade}
     * <p>
     * This method MUST be invoked by the user of the {@link TransportFacade}.
     * Fail to do so will result in resource leak and other bad things!
     * It is recommended to use Java's try-with-resource.
     *
     * @since 1.2.0
     */
    @Override
    void close();

    /**
     * Returns the {@code class} of the type of {@link TransportMessage} implementation used by this implementation of the {@link TransportFacade}.
     * <p>
     * This is meant to be used while requesting a translator from the device layer to the transport layer.
     *
     * @return The {@code class} of the type of {@link TransportMessage} implementation used by this implementation of the {@link TransportFacade}.
     * @since 1.0.0
     */
    Class<MQ> getMessageClass();
}
