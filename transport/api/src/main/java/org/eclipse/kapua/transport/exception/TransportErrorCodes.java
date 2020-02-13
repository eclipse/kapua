/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
