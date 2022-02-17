/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.event;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;

/**
 * Event bus exception
 *
 * @since 1.0
 *
 */
public class ServiceEventBusException extends KapuaException {

    private static final long serialVersionUID = -7004520562049645299L;

    /**
     * Constructs an event bus exception with the provided parameters
     *
     * @param cause
     */
    public ServiceEventBusException(Throwable cause) {
        // TODO Add error code for event bus
        super(KapuaErrorCodes.INTERNAL_ERROR, cause, (Object[])null);
    }

    /**
     * Constructs an event bus exception with the provided parameters
     *
     * @param message
     */
    public ServiceEventBusException(String message) {
        // TODO Add error code for event bus
        super(KapuaErrorCodes.INTERNAL_ERROR, message);
    }

}
