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
