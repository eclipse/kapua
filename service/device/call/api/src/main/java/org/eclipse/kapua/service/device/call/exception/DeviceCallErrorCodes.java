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
package org.eclipse.kapua.service.device.call.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * @since 1.1.0
 */
public enum DeviceCallErrorCodes implements KapuaErrorCode {

    /**
     * @see DeviceCallSendException
     * @since 1.1.0
     */
    SEND_ERROR,

    /**
     * @see DeviceCallTimeoutException
     * @since 1.1.0
     */
    TIMEOUT

}
