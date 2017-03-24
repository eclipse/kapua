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
package org.eclipse.kapua.service.datastore.client;

import org.eclipse.kapua.KapuaErrorCodes;

/**
 * Document not found exception
 *
 * @since 1.0
 */
public class DocumentNotFoundException extends ClientException {

    private static final long serialVersionUID = 7791025048562316938L;

    /**
     * Construct the exception with the provided message
     *
     * @param message
     */
    public DocumentNotFoundException(String message) {
        super(KapuaErrorCodes.ENTITY_NOT_FOUND, message);
    }

    /**
     * Construct the exception with the provided throwable
     *
     * @param t
     * @param message
     */
    public DocumentNotFoundException(Throwable t, String message) {
        super(KapuaErrorCodes.ENTITY_NOT_FOUND, t, message);
    }

}
