/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.KapuaException;

/**
 * Generic datastore exception
 *
 * @since 1.0
 */
public class DatastoreException extends KapuaException {

    private static final long serialVersionUID = -2766345175377211253L;

    /**
     * Construct the exception with the provided error code
     *
     * @param code
     */
    public DatastoreException(KapuaErrorCode code) {
        super(code);
    }

    /**
     * Construct the exception with the provided error code and message
     * 
     * @param code
     * @param message
     */
    public DatastoreException(KapuaErrorCode code, String message) {
        super(code, message);
    }

    /**
     * Construct the exception with the provided error code, throwable and message
     * 
     * @param code
     * @param t
     */
    public DatastoreException(KapuaErrorCode code, Throwable t) {
        super(code, t);
    }

    /**
     * Construct the exception with the provided error code, throwable and message
     * 
     * @param code
     * @param t
     * @param message
     *            message
     */
    public DatastoreException(KapuaErrorCode code, Throwable t, String message) {
        super(code, t, message);
    }

}
