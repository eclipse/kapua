/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.kura.app.response;

import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseCode;

/**
 * {@link DeviceResponseCode} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation
 *
 * @since 1.0.0
 */
public enum KuraResponseCode implements DeviceResponseCode {

    /**
     * Accepted request.
     *
     * @since 1.0.0
     */
    ACCEPTED(200),

    /**
     * Bad request.
     *
     * @since 1.0.0
     */
    BAD_REQUEST(400),

    /**
     * Resource not found.
     *
     * @since 1.0.0
     */
    NOT_FOUND(404),

    /**
     * Internal error.
     *
     * @since 1.0.0
     */
    INTERNAL_ERROR(500);

    /**
     * The REST-like response code.
     *
     * @since 1.0.0
     */
    private int code;

    /**
     * Constructor.
     *
     * @param code The REST-like response code.
     * @since 1.0.0
     */
    KuraResponseCode(int code) {
        this.code = code;
    }

    /**
     * Gets the REST-like response code.
     *
     * @return The REST-like response code.
     * @since 1.0.0
     */
    public int getCode() {
        return code;
    }

    /**
     * Constructs a {@link KuraResponseCode} from a {@link String} representation.
     *
     * @param responseCode The {@link String} response code.
     * @return The matching {@link KuraResponseCode}.
     * @since 1.0.0
     */
    public static KuraResponseCode fromResponseCode(String responseCode) {
        return fromResponseCode(Integer.valueOf(responseCode));
    }

    /**
     * Constructs a {@link KuraResponseCode} from an {@link Integer} representation.
     *
     * @param responseCode The REST-like response code.
     * @return The matching {@link KuraResponseCode}.
     * @since 1.0.0
     */
    public static KuraResponseCode fromResponseCode(int responseCode) {
        KuraResponseCode result = null;
        for (KuraResponseCode krc : KuraResponseCode.values()) {
            if (krc.getCode() == responseCode) {
                result = krc;
                break;
            }
        }

        return result;
    }

    @Override
    public boolean isAccepted() {
        return ACCEPTED.equals(this);
    }

    @Override
    public boolean isBadRequest() {
        return BAD_REQUEST.equals(this);
    }

    @Override
    public boolean isNotFound() {
        return NOT_FOUND.equals(this);
    }

    @Override
    public boolean isInternalError() {
        return INTERNAL_ERROR.equals(this);
    }
}
