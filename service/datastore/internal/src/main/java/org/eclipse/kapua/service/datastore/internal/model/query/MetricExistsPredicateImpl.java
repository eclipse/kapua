/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.model.query.MetricExistsPredicate;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.query.predicate.ExistsPredicateImpl;
import org.eclipse.kapua.service.storable.model.query.predicate.PredicateConstants;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;
import org.eclipse.kapua.service.storable.model.utils.MappingUtils;

/**
 * Implementation of query predicate for checking if a field exists
 *
 * @since 1.2.0
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
    public ObjectNode toSerializedMap() throws MappingException {
        ObjectNode rootNode = MappingUtils.newObjectNode();
        String fieldName = type == null ? String.format("metrics.%s", name) : String.format("metrics.%s.%s", name, DatastoreUtils.getClientMetricFromAcronym(type.getSimpleName().toLowerCase()));
        ObjectNode termNode = MappingUtils.getField(new KeyValueEntry[]{new KeyValueEntry(PredicateConstants.FIELD_KEY, fieldName)});
        rootNode.set(PredicateConstants.EXISTS_KEY, termNode);
        return rootNode;
    }

}
