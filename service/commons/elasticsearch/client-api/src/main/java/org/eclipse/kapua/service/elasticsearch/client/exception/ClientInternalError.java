/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.exception;

/**
 * {@link ClientException} to throw when an unrecoverable error occurs.
 *
 * @since 1.3.0
 */
public class ClientInternalError extends ClientException {

    private final String reason;

    /**
     * Constructor.
     *
     * @param reason The reason that caused the {@link ClientInternalError}.
     * @since 1.3.0
     */
    public ClientInternalError(String reason) {
        this(null, reason);
    }

    /**
     * Constructor.
     *
     * @param cause  The root {@link Throwable} of this {@link ClientInternalError}.
     * @param reason The reason that caused the {@link ClientInternalError}.
     * @since 1.3.0
     */
    public ClientInternalError(Throwable cause, String reason) {
        super(ClientErrorCodes.INTERNAL_ERROR, cause, reason);

        this.reason = reason;
    }

    /**
     * The reason that caused this {@link ClientInternalError}.
     *
     * @return The reason that caused this {@link ClientInternalError}.
     * @since 1.3.0
     */
    public String getReason() {
        return reason;
    }
}
