/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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
 * {@link ClientException} to throw when a request fails.
 *
 * @since 1.3.0
 */
public class ClientActionResponseException extends ClientException {

    private final String action;
    private final String reason;

    /**
     * Constructor.
     *
     * @param action The action that generated the {@link ClientActionResponseException}.
     * @param reason The reason of the {@link ClientActionResponseException}.
     * @since 1.3.0
     */
    public ClientActionResponseException(String action, String reason) {
        super(ClientErrorCodes.ACTION_RESPONSE_ERROR, null, action, reason);

        this.reason = reason;
        this.action = action;
    }

    /**
     * Constructor.
     *
     * @param cause  The root {@link Throwable} of this {@link ClientActionResponseException}.
     * @param action The action that generated the {@link ClientActionResponseException}.
     * @param reason The reason of the {@link ClientActionResponseException}.
     * @since 1.3.0
     */
    public ClientActionResponseException(Throwable cause, String action, String reason) {
        super(ClientErrorCodes.ACTION_RESPONSE_ERROR, cause, action, reason);

        this.reason = reason;
        this.action = action;
    }

    /**
     * Gets the action that generated the {@link ClientActionResponseException}.
     *
     * @return The action that generated the {@link ClientActionResponseException}.
     * @since 1.3.0
     */
    public String getAction() {
        return action;
    }

    /**
     * Gets the reason of the {@link ClientActionResponseException}.
     *
     * @return The reason of the {@link ClientActionResponseException}.
     * @since 1.3.0
     */
    public String getReason() {
        return reason;
    }
}
