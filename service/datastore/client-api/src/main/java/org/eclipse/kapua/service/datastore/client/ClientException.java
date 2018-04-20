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
package org.eclipse.kapua.service.datastore.client;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.KapuaException;

/**
 * Generic client exception
 *
 * @since 1.0
 */
public class ClientException extends KapuaException {

    private static final long serialVersionUID = 2393001020208113850L;

    /**
     * Construct the exception with the provided error code
     *
     * @param code
     */
    public ClientException(KapuaErrorCode code) {
        super(code);
    }

    /**
     * Construct the exception with the provided code and message
     *
     * @param code
     * @param message
     */
    public ClientException(KapuaErrorCode code, String message) {
        super(code, message);
    }

    /**
     * Construct the exception with the provided code and message
     *
     * @param code
     * @param message
     * @param t
     */
    public ClientException(KapuaErrorCode code, String message, Throwable t) {
        super(code, message, t);
    }

    /**
     * Construct the exception with the provided code, throwable and message
     * 
     * @param code
     * @param t
     * @param message
     */
    public ClientException(KapuaErrorCode code, Throwable t, String message) {
        super(code, t, message);
    }

    /**
     * Construct the exception with the provided code, throwable and message
     * 
     * @param code
     * @param t
     */
    public ClientException(KapuaErrorCode code, Throwable t) {
        super(code, t);
    }

}
