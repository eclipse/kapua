/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
     * An error occurred when sending the {@link org.eclipse.kapua.service.device.call.message.DeviceMessage}.
     *
     * @since 1.1.0
     */
    SEND_ERROR,

    /**
     * A response as not been received within the given timeout.
     *
     * @since 1.1.0
     */
    TIMEOUT

}
