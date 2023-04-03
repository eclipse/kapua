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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link KapuaErrorCode}s for {@link TransportException}s.
 *
 * @since 1.1.0
 */
public enum TransportErrorCodes implements KapuaErrorCode {

    /**
     * @see TransportSendException
     * @since 1.1.0
     */
    SEND_ERROR,

    /**
     * @see TransportSendException
     * @since 2.0.0
     */
    SEND_ERROR_WITH_CAUSE,

    /**
     * @see TransportTimeoutException
     * @since 1.1.0
     */
    TIMEOUT,

    /**
     * @see TransportClientGetException
     * @since 1.2.0
     */
    CLIENT_GET,

    /**
     * @see TransportClientGetException
     * @since 2.0.0
     */
    CLIENT_GET_WITH_CAUSE,

    /**
     * @see TransportClientPoolExhaustedException
     * @since 2.0.0
     */
    CLIENT_POOL_EXHAUSTED
}
