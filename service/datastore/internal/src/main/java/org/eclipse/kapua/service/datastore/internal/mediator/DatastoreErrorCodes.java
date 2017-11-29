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
     * Unhandled query predicate type
     */
    UNHANDLED_QUERY_PREDICATE_TYPE,
    /*
     * Generic internal error
     */
    INTERNAL_ERROR;

}
