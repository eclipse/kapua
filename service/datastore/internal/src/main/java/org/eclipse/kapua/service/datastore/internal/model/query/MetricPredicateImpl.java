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

import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.query.MetricPredicate;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Implementation of query predicate for matching range values
 *
 * @since 1.0
 *
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
    public ObjectNode toSerializedMap() throws DatamodelMappingException {
        ObjectNode rootNode = SchemaUtil.getObjectNode();
        ObjectNode valuesNode = SchemaUtil.getObjectNode();
        if (maxValue != null) {
            SchemaUtil.appendField(valuesNode, PredicateConstants.LTE_KEY, maxValue);
        }
        if (minValue != null) {
            SchemaUtil.appendField(valuesNode, PredicateConstants.GTE_KEY, minValue);
        }
        ObjectNode termNode = SchemaUtil.getObjectNode();
        termNode.set(String.format("metrics.%s.%s", field, DatastoreUtils.getClientMetricFromAcronym(type.getSimpleName().toLowerCase())), valuesNode);
        rootNode.set(PredicateConstants.RANGE_KEY, termNode);
        return rootNode;
    }

}
