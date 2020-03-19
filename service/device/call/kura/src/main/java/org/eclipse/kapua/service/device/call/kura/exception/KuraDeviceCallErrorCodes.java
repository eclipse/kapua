/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
