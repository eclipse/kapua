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
package org.eclipse.kapua.service.device.call.kura.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link KuraDeviceCallErrorCodes} for {@link KuraDeviceCallErrorCodes}.
 *
 * @since 1.0.0
 */
public enum KuraDeviceCallErrorCodes implements KapuaErrorCode {
    /**
     * Call error
     */
    CALL_ERROR,
    /**
     * Call timeout
     */
    CALL_TIMEOUT,
    /**
     * Borrow client form the pool error
     */
    CLIENT_BORROW_ERROR,
    /**
     * Return client to the pool error
     */
    CLIENT_RETURN_ERROR,
    /**
     * Send call error
     */
    CLIENT_SEND_ERROR,
    ;
}
