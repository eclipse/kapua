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
import org.eclipse.kapua.service.datastore.model.query.MetricPredicate;
import org.eclipse.kapua.service.elasticsearch.client.exception.DatamodelMappingException;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.query.predicate.PredicateConstants;
import org.eclipse.kapua.service.storable.model.query.predicate.RangePredicateImpl;
import org.eclipse.kapua.service.storable.model.utils.MappingUtils;

/**
 * Implementation of query predicate for matching range values
 *
 * @since 1.0
 */
public class MetricPredicateImpl extends RangePredicateImpl implements MetricPredicate {

    private Class<?> type;

    public <V extends Comparable<V>> MetricPredicateImpl(String fieldName, Class<V> type, V minValue, V maxValue) {
        super(fieldName, minValue, maxValue);

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

    /**
     * <pre>
     *  {
     *      "query": {
     *          "range" : {
     *              "metrics.temperature.int" : {
     *                  "gte" : 10,
     *                  "lte" : 20,
     *              }
     *          }
     *      }
     *  }
     * </pre>
     *
     * @throws DatamodelMappingException
     */
    @Override
    public ObjectNode toSerializedMap() throws MappingException {
        ObjectNode rootNode = MappingUtils.newObjectNode();
        ObjectNode valuesNode = MappingUtils.newObjectNode();
        if (maxValue != null) {
            MappingUtils.appendField(valuesNode, PredicateConstants.LTE_KEY, maxValue);
        }
        if (minValue != null) {
            MappingUtils.appendField(valuesNode, PredicateConstants.GTE_KEY, minValue);
        }
        ObjectNode termNode = MappingUtils.newObjectNode();
        termNode.set(String.format("metrics.%s.%s", field, DatastoreUtils.getClientMetricFromAcronym(type.getSimpleName().toLowerCase())), valuesNode);
        rootNode.set(PredicateConstants.RANGE_KEY, termNode);
        return rootNode;
    }

}
