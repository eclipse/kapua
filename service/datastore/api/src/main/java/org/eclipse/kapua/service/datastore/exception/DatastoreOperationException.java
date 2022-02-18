/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.exception;

/**
 * {@link DatastoreServiceException} to {@code throw} when performing an operation on the datastore causes an exception.
 *
 * @since 1.3.0
 */
public class DatastoreOperationException extends DatastoreServiceException {

    private final String operation;

    /**
     * Constructor.
     *
     * @param cause @param cause The root {@link Throwable} of this {@link DatastoreOperationException}.
     * @since 1.3.0
     */
    public DatastoreOperationException(Throwable cause, String operation) {
        super(DatastoreServiceErrorCodes.DATASTORE_OPERATION_EXCEPTION, cause, operation);

        this.operation = operation;
    }

    /**
     * Gets the operation that failed.
     *
     * @return The operation that failed.
     * @since 1.3.0
     */
    public String getOperation() {
        return operation;
    }
}
