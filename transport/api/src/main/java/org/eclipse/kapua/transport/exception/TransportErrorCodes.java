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
 * @since 1.1.0
 */
public enum TransportErrorCodes implements KapuaErrorCode {

    /**
     * An error occurred when sending the {@link org.eclipse.kapua.transport.message.TransportMessage}.
     *
     * @since 1.1.0
     */
    SEND_ERROR,

    /**
     * A response as not been received within the given timeout.
     *
     * @since 1.1.0
     */
    TIMEOUT,

    /**
     * Getting the {@link org.eclipse.kapua.transport.TransportFacade} produces an error.
     *
     * @since 1.2.0
     */
    CLIENT_GET
}
