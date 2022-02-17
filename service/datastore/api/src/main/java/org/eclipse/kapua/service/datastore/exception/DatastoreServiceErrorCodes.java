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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Datastore {@link org.eclipse.kapua.service.KapuaService} {@link KapuaErrorCode}s
 *
 * @since 1.3.0
 */
public enum DatastoreServiceErrorCodes implements KapuaErrorCode {

    /**
     * See {@link DatastoreUnavailableException}
     *
     * @since 1.3.0
     */
    DATASTORE_UNAVAILABLE_EXCEPTION,

    /**
     * See {@link DatastoreDisabledException}
     *
     * @since 1.3.0
     */
    DATASTORE_DISABLED_EXCEPTION,

    /**
     * See {@link DatastoreOperationException}
     *
     * @since 1.3.0
     */
    DATASTORE_OPERATION_EXCEPTION,

    /**
     * See {@link DatastoreInternalError}.
     *
     * @since 1.3.0.
     */
    INTERNAL_ERROR,
}
