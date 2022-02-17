/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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

/**
 * {@link TransportException} to throw when the response is not received within the given timeout.
 *
 * @since 1.1.0
 */
public class TransportTimeoutException extends TransportException {

    private final Long timeout;

    /**
     * Constructor.
     *
     * @param timeout The timeout of the request.
     * @since 1.1.0
     */
    public TransportTimeoutException(Long timeout) {
        super(TransportErrorCodes.TIMEOUT, timeout);

        this.timeout = timeout;
    }

    /**
     * Gets the timeout of the request.
     *
     * @return The timeout of the request.
     * @since 1.1.0
     */
    public Long getTimeout() {
        return timeout;
    }
}
