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

/**
 * Client unavailable exception
 *
 * @since 1.0
 */
public class ClientUnavailableException extends ClientException {

    private static final long serialVersionUID = 2211521053876589804L;

    /**
     * Construct the exception with the provided message
     *
     * @param message
     */
    public ClientUnavailableException(String message) {
        super(ClientErrorCodes.CLIENT_UNAVAILABLE, message);
    }

    /**
     * Construct the exception with the provided message and throwable
     *
     * @param message
     * @param t
     */
    public ClientUnavailableException(String message, Throwable t) {
        super(ClientErrorCodes.CLIENT_UNAVAILABLE, t, message);
    }

}
