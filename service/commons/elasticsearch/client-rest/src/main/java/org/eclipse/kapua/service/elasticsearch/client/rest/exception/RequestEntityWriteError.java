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
package org.eclipse.kapua.service.elasticsearch.client.rest.exception;

import org.eclipse.kapua.service.elasticsearch.client.exception.ClientInternalError;

/**
 * {@link ClientInternalError} to {@code throw} when there is an error on {@link com.fasterxml.jackson.databind.ObjectMapper#writeValueAsString(Object)}
 *
 * @since 1.3.0
 */
public class RequestEntityWriteError extends ClientInternalError {

    /**
     * Constructor.
     *
     * @param cause The original cause of the error.
     * @since 1.3.0
     */
    public RequestEntityWriteError(Throwable cause) {
        super(cause, "Error managing ObjectMapper.write");
    }
}
