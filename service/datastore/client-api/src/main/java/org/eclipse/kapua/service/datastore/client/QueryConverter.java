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

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Query converter definition. This object is responsible for translating datastore query model to client query.
 * 
 * @since 1.0
 */
public interface QueryConverter {

    /**
     * Query fetch style key
     */
    public String QUERY_FETCH_STYLE_KEY = "query_fetch_style";

    /**
     * Convert the datastore query to the client query
     * 
     * @param query
     * @return
     * @throws QueryMappingException
     * @throws DatamodelMappingException
     */
    public JsonNode convertQuery(Object query) throws QueryMappingException, DatamodelMappingException;

    /**
     * Get the query fetch style
     * 
     * @param query
     * @return
     * @throws QueryMappingException
     */
    public Object getFetchStyle(Object query) throws QueryMappingException;

}
