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
package org.eclipse.kapua.service.elasticsearch.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.kapua.service.elasticsearch.client.exception.DatamodelMappingException;
import org.eclipse.kapua.service.elasticsearch.client.exception.QueryMappingException;

/**
 * Query converter definition.
 * <p>
 * This object is responsible for translating datastore query model to client query.
 *
 * @since 1.0.0
 */
public interface QueryConverter {

    /**
     * Query fetch style key
     *
     * @since 1.0.0
     */
    String QUERY_FETCH_STYLE_KEY = "query_fetch_style";

    /**
     * Converts the Elasticsearch query to the client query
     *
     * @param query The queyr to convert.
     * @return The converted query.
     * @throws QueryMappingException     if query mappings are not correst.
     * @throws DatamodelMappingException if model mappings are not correct.
     * @since 1.0.0
     */
    JsonNode convertQuery(Object query) throws QueryMappingException, DatamodelMappingException;

    /**
     * Gets the query fetch style
     *
     * @param query The query fetch style
     * @return The query fetch style
     * @throws QueryMappingException if query mappings are not correst.
     * @since 1.0.0
     */
    Object getFetchStyle(Object query) throws QueryMappingException;

}
