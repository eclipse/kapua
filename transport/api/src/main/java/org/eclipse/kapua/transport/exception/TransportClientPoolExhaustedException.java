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
 * The {@link TransportException} to throw when is not possible to get an instance of the {@link org.eclipse.kapua.transport.TransportFacade} within the configured timeout
 *
 * @since 2.0.0
 */
public class TransportClientPoolExhaustedException extends TransportException {

    private final String serverIp;
    private final Long borrowWaitTimeout;

    /**
     * Constructor.
     *
     * @param serverIp The serverIp to which connect the {@link org.eclipse.kapua.transport.TransportFacade}
     * @since 2.0.0
     */
    public TransportClientPoolExhaustedException(@NotNull String serverIp, Long borrowWaitTimeout) {
        super(TransportErrorCodes.CLIENT_POOL_EXHAUSTED, serverIp, borrowWaitTimeout);

        this.serverIp = serverIp;
        this.borrowWaitTimeout = borrowWaitTimeout;
    }

    /**
     * Constructor.
     *
     * @param cause    the root cause of the {@link Exception}.
     * @param serverIp The serverIp to which connect the {@link org.eclipse.kapua.transport.TransportFacade}
     * @since 2.0.0
     */
    public TransportClientPoolExhaustedException(@NotNull Throwable cause, @NotNull String serverIp, Long borrowWaitTimeout) {
        super(TransportErrorCodes.CLIENT_POOL_EXHAUSTED, cause, serverIp, borrowWaitTimeout);

        this.serverIp = serverIp;
        this.borrowWaitTimeout = borrowWaitTimeout;
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
     * Gets the configured timeout for the borrow operation.
     *
     * @return The configured timeout for the borrow operation.
     * @since 2.0.0
     */
    public Long getBorrowWaitTimeout() {
        return borrowWaitTimeout;
    }
}
