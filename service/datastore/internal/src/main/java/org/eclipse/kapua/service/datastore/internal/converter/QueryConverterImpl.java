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
package org.eclipse.kapua.service.datastore.internal.converter;

import java.util.List;

import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;
import org.eclipse.kapua.service.datastore.client.QueryConverter;
import org.eclipse.kapua.service.datastore.client.QueryMappingException;
import org.eclipse.kapua.service.datastore.internal.AbstractStorableQuery;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.query.SortField;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_EXCLUDE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_FROM;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_INCLUDE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_QUERY;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_SIZE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_SOURCE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_SORT;

/**
 * Query converter implementation
 * 
 * @since 1.0
 */
public class QueryConverterImpl implements QueryConverter {

    @Override
    public JsonNode convertQuery(Object query) throws QueryMappingException, DatamodelMappingException {
        if (!(query instanceof AbstractStorableQuery<?>)) {
            throw new QueryMappingException("Wrong query type! Only AbstractStorableQuery can be converted!");
        }
        ObjectNode rootNode = SchemaUtil.getObjectNode();
        AbstractStorableQuery<?> storableQuery = (AbstractStorableQuery<?>) query;
        // includes/excludes
        ObjectNode includesFields = SchemaUtil.getObjectNode();
        includesFields.set(KEY_INCLUDE, SchemaUtil.getAsArrayNode(storableQuery.getIncludes(storableQuery.getFetchStyle())));
        includesFields.set(KEY_EXCLUDE, SchemaUtil.getAsArrayNode(storableQuery.getExcludes(storableQuery.getFetchStyle())));
        rootNode.set(KEY_SOURCE, includesFields);
        // query
        if (storableQuery.getPredicate() != null) {
            rootNode.set(KEY_QUERY, storableQuery.getPredicate().toSerializedMap());
        }
        // sort
        ArrayNode sortNode = SchemaUtil.getArrayNode();
        List<SortField> sortFields = storableQuery.getSortFields();
        if (sortFields != null) {
            for (SortField field : sortFields) {
                sortNode.add(SchemaUtil.getField(field.getField(), field.getSortDirection().name()));
            }
        }
        // offset and limit settings
        rootNode.set(KEY_FROM, SchemaUtil.getNumericNode(storableQuery.getOffset()));
        rootNode.set(KEY_SIZE, SchemaUtil.getNumericNode(storableQuery.getLimit()));
        rootNode.set(KEY_SORT, sortNode);
        return rootNode;
    }

    @Override
    public Object getFetchStyle(Object query) throws QueryMappingException {
        if (!(query instanceof AbstractStorableQuery<?>)) {
            throw new QueryMappingException("Wrong query type! Only AbstractStorableQuery can be converted!");
        }
        return ((AbstractStorableQuery<?>) query).getFetchStyle();
    }

}
