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
package org.eclipse.kapua.service.datastore.exception;

/**
 * {@link DatastoreServiceError} to {@code throw} when there is an unrecoverable error performing an operation
 *
 * @since 1.3.0
 */
public class DatastoreInternalError extends DatastoreServiceError {

    private final String message;

    /**
     * Constructor.
     *
     * @param cause @param cause The root {@link Throwable} of this {@link DatastoreInternalError}.
     * @since 1.3.0
     */
    public DatastoreInternalError(Throwable cause, String message) {
        super(DatastoreServiceErrorCodes.INTERNAL_ERROR, cause, message);

        this.message = message;
    }

    /**
     * Gets the detail message associated with this {@link DatastoreInternalError}
     *
     * @return The message
     * @since 1.3.0
     */
    @Override
    public String getMessage() {
        return message;
    }
}
