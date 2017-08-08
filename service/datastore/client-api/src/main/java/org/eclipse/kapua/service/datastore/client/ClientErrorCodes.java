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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Client error codes to be used by exceptions
 * 
 * @since 1.0
 */
public enum ClientErrorCodes implements KapuaErrorCode {

    /**
     * Client unavailable
     */
    CLIENT_UNAVAILABLE,
    /**
     * Client undefined (not initialized or misconfigured)
     */
    CLIENT_UNDEFINED,
    /**
     * Data model mapping error
     */
    DATAMODEL_MAPPING_EXCEPTION,
    /**
     * Schema mapping error
     */
    SCHEMA_MAPPING_EXCEPTION,
    /**
     * Query mapping error
     */
    QUERY_MAPPING_EXCEPTION,
    /**
     * Error on performed action
     */
    ACTION_ERROR,
    /**
     * Error on communication with the underlying datastore layer (timeout, no node available ...)
     */
    COMMUNICATION_ERROR
}
