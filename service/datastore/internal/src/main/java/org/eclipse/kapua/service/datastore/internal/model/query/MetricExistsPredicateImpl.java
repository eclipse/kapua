/*******************************************************************************
 * Copyright (c) 2011, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.model.query;

import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.schema.KeyValueEntry;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.query.MetricExistsPredicate;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Implementation of query predicate for checking if a field exists
 *
 * @since 1.2.0
 *
 */
public class MetricExistsPredicateImpl extends ExistsPredicateImpl implements MetricExistsPredicate {

    private Class<?> type;

    public <V extends Comparable<V>> MetricExistsPredicateImpl(String fieldName, Class<V> type) {
        super(fieldName);

        this.type = type;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public void setType(Class<?> type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    /**
     * <pre>
     *  {
     *      "query": {
     *          "exists" : { "field" : "metrics.metric.typ" }
     *      }
     *   }
     * </pre>
     */
    public ObjectNode toSerializedMap() throws DatamodelMappingException {
        ObjectNode rootNode = SchemaUtil.getObjectNode();
        String fieldName = type == null ? String.format("metrics.%s", name) : String.format("metrics.%s.%s", name, DatastoreUtils.getClientMetricFromAcronym(type.getSimpleName().toLowerCase()));
        ObjectNode termNode = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(PredicateConstants.FIELD_KEY, fieldName) });
        rootNode.set(PredicateConstants.EXISTS_KEY, termNode);
        return rootNode;
    }

}
