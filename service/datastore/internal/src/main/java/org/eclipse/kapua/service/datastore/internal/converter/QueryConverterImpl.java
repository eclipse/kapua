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
package org.eclipse.kapua.service.datastore.internal.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.elasticsearch.client.QueryConverter;
import org.eclipse.kapua.service.elasticsearch.client.SchemaKeys;
import org.eclipse.kapua.service.elasticsearch.client.exception.DatamodelMappingException;
import org.eclipse.kapua.service.elasticsearch.client.exception.QueryMappingException;
import org.eclipse.kapua.service.storable.model.query.AbstractStorableQuery;
import org.eclipse.kapua.service.storable.model.query.SortField;
import org.eclipse.kapua.service.storable.model.utils.MappingUtils;

import java.util.List;

/**
 * Query converter implementation
 *
 * @since 1.0
 */
public class QueryConverterImpl implements QueryConverter {

    @Override
    public JsonNode convertQuery(Object query) throws QueryMappingException, DatamodelMappingException, KapuaException {
        if (!(query instanceof AbstractStorableQuery)) {
            throw new QueryMappingException();
        }

        ObjectNode rootNode = MappingUtils.newObjectNode();
        AbstractStorableQuery storableQuery = (AbstractStorableQuery) query;
        // includes/excludes
        ObjectNode includesFields = MappingUtils.newObjectNode();
        includesFields.set(SchemaKeys.KEY_INCLUDES, MappingUtils.newArrayNode(storableQuery.getIncludes(storableQuery.getFetchStyle())));
        includesFields.set(SchemaKeys.KEY_EXCLUDES, MappingUtils.newArrayNode(storableQuery.getExcludes(storableQuery.getFetchStyle())));
        rootNode.set(SchemaKeys.KEY_SOURCE, includesFields);
        // query
        if (storableQuery.getPredicate() != null) {
            try {
                rootNode.set(SchemaKeys.KEY_QUERY, storableQuery.getPredicate().toSerializedMap());
            } catch (Exception e) {
                throw new DatamodelMappingException(e, "Cannot serialize predicates");
            }
        }
        // sort
        ArrayNode sortNode = MappingUtils.newArrayNode();
        List<SortField> sortFields = storableQuery.getSortFields();
        if (sortFields != null) {
            for (SortField field : sortFields) {
                sortNode.add(MappingUtils.newObjectNode(field.getField(), field.getSortDirection().name()));
            }
        }
        // offset and limit settings
        Integer offset = storableQuery.getOffset();
        if (offset != null) {
            rootNode.set(SchemaKeys.KEY_FROM, MappingUtils.newNumericNode(offset));
        }
        Integer limit = storableQuery.getLimit();
        if (limit != null) {
            rootNode.set(SchemaKeys.KEY_SIZE, MappingUtils.newNumericNode(limit));
        }
        rootNode.set(SchemaKeys.KEY_SORT, sortNode);
        return rootNode;
    }

    @Override
    public Object getFetchStyle(Object query) throws QueryMappingException {
        if (!(query instanceof AbstractStorableQuery)) {
            throw new QueryMappingException();
        }

        return ((AbstractStorableQuery) query).getFetchStyle();
    }

}
