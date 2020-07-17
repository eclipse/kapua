/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.exception;

/**
 * {@link ClientException} to throw when something really bad happens.
 *
 * @since 1.3.0
 */
public class ClientInternalError extends ClientException {

    /**
     * Constructor.
     *
     * @since 1.3.0
     */
    public ClientInternalError() {
        super(ClientErrorCodes.INTERNAL_ERROR);
    }


    /**
     * Constructor.
     *
     * @param cause The root {@link Throwable} of this {@link ClientInternalError}.
     * @since 1.3.0
     */
    public ClientInternalError(Throwable cause) {
        super(ClientErrorCodes.INTERNAL_ERROR, cause);
    }
}
