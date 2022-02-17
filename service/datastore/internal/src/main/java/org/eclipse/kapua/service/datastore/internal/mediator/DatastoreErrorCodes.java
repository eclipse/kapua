/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Datastore error codes used by the datastore exceptions
 *
 * @since 1.0
 */
public enum DatastoreErrorCodes implements KapuaErrorCode {

    /**
     * Wrong or missing configuration parameters
     */
    CONFIGURATION_ERROR,
    /**
     * Invalid channel format
     */
    INVALID_CHANNEL,
    /**
     * Error on communication with the underlying datastore layer (timeout, no node available ...)
     */
    COMMUNICATION_ERROR,
    /**
     * Generic internal error
     */
    INTERNAL_ERROR
}
