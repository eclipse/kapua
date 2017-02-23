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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.commons.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Device management error codes.
 *
 * @since 1.0
 */
public enum DeviceManagementErrorCodes implements KapuaErrorCode {
    /**
     * Request exception
     */
    REQUEST_EXCEPTION,
    /**
     * Bad request method
     */
    REQUEST_BAD_METHOD,

    /**
     * Response parse exception
     */
    RESPONSE_PARSE_EXCEPTION,

    /**
     * Bad request
     */
    RESPONSE_BAD_REQUEST,
    /**
     * Response not found
     */
    RESPONSE_NOT_FOUND,
    /**
     * Response internal error
     */
    RESPONSE_INTERNAL_ERROR;
}
