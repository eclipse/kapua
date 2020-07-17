/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Elasticsearch Client {@link KapuaErrorCode}s
 *
 * @since 1.0.0
 */
public enum ClientErrorCodes implements KapuaErrorCode {

    /**
     * See {@link ClientUnavailableException}.
     *
     * @since 1.0.0
     */
    CLIENT_UNAVAILABLE,

    /**
     * See {@link ClientInitializationException}.
     *
     * @since 1.3.0
     */
    CLIENT_INITIALIZATION_ERROR,

    /**
     * See {@link ClientUndefinedException}.
     *
     * @since 1.0.0
     */
    CLIENT_UNDEFINED,
    /**
     * See {@link DatamodelMappingException}.
     *
     * @since 1.0.0
     */
    DATAMODEL_MAPPING_EXCEPTION,
    /**
     * Schema mapping error.
     *
     * @since 1.0.0
     */
    SCHEMA_MAPPING_EXCEPTION,
    /**
     * Query mapping error.
     *
     * @since 1.0.0
     */
    QUERY_MAPPING_EXCEPTION,
    /**
     * Error on performed action.
     *
     * @since 1.0.0
     */
    ACTION_ERROR,
    /**
     * Error on communication with the underlying datastore layer (timeout, no node available ...).
     *
     * @since 1.0.0
     */
    COMMUNICATION_ERROR,

    /**
     * See {@link ClientInternalError}.
     *
     * @since 1.3.0
     */
    INTERNAL_ERROR,

    /**
     * See {@link ClientActionResponseException}
     *
     * @since 1.3.0
     */
    ACTION_RESPONSE_ERROR
}
