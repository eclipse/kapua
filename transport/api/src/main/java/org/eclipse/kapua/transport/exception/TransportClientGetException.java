/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.exception;

import javax.validation.constraints.NotNull;

/**
 * The {@link TransportException} to throw when is not possible to get an instance of the {@link org.eclipse.kapua.transport.TransportFacade}
 *
 * @since 1.2.0
 */
public class TransportClientGetException extends TransportException {

    private final String serverIp;
    private final String causeMessage;

    /**
     * Constructor.
     *
     * @param serverIp The serverIp to which connect the {@link org.eclipse.kapua.transport.TransportFacade}
     * @since 1.2.0
     */
    public TransportClientGetException(@NotNull String serverIp) {
        super(TransportErrorCodes.CLIENT_GET, serverIp);

        this.serverIp = serverIp;
        this.causeMessage = null;
    }

    /**
     * Constructor.
     *
     * @param cause    the root cause of the {@link Exception}.
     * @param serverIp The serverIp to which connect the {@link org.eclipse.kapua.transport.TransportFacade}
     * @since 1.2.0
     */
    public TransportClientGetException(@NotNull Throwable cause, @NotNull String serverIp) {
        super(TransportErrorCodes.CLIENT_GET_WITH_CAUSE, cause, serverIp, cause.getMessage());

        this.serverIp = serverIp;
        this.causeMessage = cause.getMessage();
    }

    /**
     * Gets the IP to which we wanted unsuccessfully to connect.
     *
     * @return The IP to which we wanted unsuccessfully to connect.
     * @since 1.2.0
     * @deprecated Since 2.0.0. It was not renamed after copy-pasting from another class. Please make use of {@link #getServerIp()}.
     */
    @Deprecated
    public String getRequestMessage() {
        return getServerIp();
    }

    /**
     * Gets the IP to which we wanted unsuccessfully to connect.
     *
     * @return The IP to which we wanted unsuccessfully to connect.
     * @since 2.0.0
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * Gets the {@link Throwable#getMessage()} if provided.
     *
     * @return The {@link Throwable#getMessage()} if provided.
     * @since 2.0.0
     */
    public String getCauseMessage() {
        return causeMessage;
    }
}
