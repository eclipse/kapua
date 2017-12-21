/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.app.response.kura;

import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseCode;

/**
 * Kura device response code definition.
 *
 * @since 1.0
 */
public enum KuraResponseCode implements DeviceResponseCode {
    /**
     * Accepted request
     */
    ACCEPTED(200),
    /**
     * Bad request
     */
    BAD_REQUEST(400),
    /**
     * Resource not found
     */
    NOT_FOUND(404),
    /**
     * Internal error
     */
    INTERNAL_ERROR(500);

    private int code;

    /**
     * Constructor
     *
     * @param code
     */
    KuraResponseCode(int code) {
        this.code = code;
    }

    /**
     * Get the response code
     *
     * @return
     */
    public int getCode() {
        return code;
    }

    /**
     * Constructs a {@link KuraResponseCode} from a string representation
     *
     * @param responseCode
     * @return
     */
    public static KuraResponseCode fromResponseCode(String responseCode) {
        return fromResponseCode(Integer.valueOf(responseCode));
    }

    /**
     * Constructs a {@link KuraResponseCode} from an integer representation
     *
     * @param responseCode
     * @return
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
