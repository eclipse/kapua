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
package org.eclipse.kapua.service.device.management.message.response;

/**
 * {@link KapuaResponseMessage} response code.
 *
 * @since 1.0.0
 */
public enum KapuaResponseCode {

    /**
     * Accepted.
     *
     * @since 1.0.0
     */
    ACCEPTED, // 200, 204

    /**
     * Bad request.
     *
     * @since 1.0.0
     */
    BAD_REQUEST, // 400

    /**
     * Resource not found.
     *
     * @since 1.0.0
     */
    NOT_FOUND, // 404

    /**
     * Request has been sent
     *
     * @since 1.5.0
     */
    SENT,

    /**
     * Internal error.
     *
     * @since 1.0.0
     */
    INTERNAL_ERROR; // 500

    public boolean isAccepted() {
        return ACCEPTED.equals(this);
    }

    public boolean isBadRequest() {
        return BAD_REQUEST.equals(this);
    }

    public boolean isNotFound() {
        return NOT_FOUND.equals(this);
    }

    public boolean isInternalError() {
        return INTERNAL_ERROR.equals(this);
    }
}
