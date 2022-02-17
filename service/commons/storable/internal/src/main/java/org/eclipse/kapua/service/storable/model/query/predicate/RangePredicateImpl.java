/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model.query.predicate;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.query.StorableField;

/**
 * {@link RangePredicate} implementation.
 *
 * @since 1.0.0
 */
public class RangePredicateImpl extends StorablePredicateImpl implements RangePredicate {

    private StorableField storableField;
    private Object minValue;
    private Object maxValue;

    /**
     * Constructor.
     *
     * @param field    The {@link StorableField}.
     * @param minValue The lower bound.
     * @param maxValue The upper bound.
     * @param <V>      The {@link Comparable} type.
     * @since 1.0.0
     */
    public <V extends Comparable<V>> RangePredicateImpl(StorableField field, V minValue, V maxValue) {
        setField(field);

        setMinValue(minValue);
        setMaxValue(maxValue);
    }

    @Override
    public StorableField getField() {
        return storableField;
    }

    @Override
    public RangePredicate setField(StorableField storableField) {
        this.storableField = storableField;

        return this;
    }

    @Override
    public Object getMinValue() {
        return minValue;
    }

    @Override
    public <V extends Comparable<V>> V getMinValue(Class<V> clazz) {
        return clazz.cast(minValue);
    }

    @Override
    public <V extends Comparable<V>> RangePredicate setMinValue(V minValue) {
        this.minValue = minValue;

        return this;
    }

    @Override
    public Object getMaxValue() {
        return maxValue;
    }

    @Override
    public <V extends Comparable<V>> V getMaxValue(Class<V> clazz) {
        return clazz.cast(maxValue);
    }

    @Override
    public <V extends Comparable<V>> RangePredicate setMaxValue(V maxValue) {
        this.maxValue = maxValue;

        return this;
    }

    /**
     * <pre>
     *  {
     *      "query": {
     *          "range" : {
     *              "temperature" : {
     *                  "gte" : 10,
     *                  "lte" : 20,
     *              }
     *          }
     *      }
     *  }
     * </pre>
     */
    @Override
    public ObjectNode toSerializedMap() throws MappingException {

        ObjectNode valuesNode = newObjectNode();
        if (getMaxValue() != null) {
            appendField(valuesNode, PredicateConstants.LTE_KEY, getMaxValue());
        }
        if (getMinValue() != null) {
            appendField(valuesNode, PredicateConstants.GTE_KEY, getMinValue());
        }

        ObjectNode termNode = newObjectNode();
        termNode.set(getField().field(), valuesNode);

        ObjectNode rootNode = newObjectNode();
        rootNode.set(PredicateConstants.RANGE_KEY, termNode);
        return rootNode;
    }
}
