/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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