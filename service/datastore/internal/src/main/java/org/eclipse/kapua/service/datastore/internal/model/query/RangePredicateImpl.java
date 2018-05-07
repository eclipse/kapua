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
package org.eclipse.kapua.service.datastore.internal.model.query;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorableField;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Implementation of query predicate for matching range values
 *
 * @since 1.0.0
 */
public class RangePredicateImpl implements RangePredicate {

    protected String field;
    protected Object minValue;
    protected Object maxValue;

    private <V extends Comparable<V>> void checkRange(Class<V> clazz) throws KapuaException {
        if (minValue == null || maxValue == null) {
            return;
        }

        V min = clazz.cast(minValue);
        V max = clazz.cast(maxValue);
        if (min.compareTo(max) > 0) {
            throw KapuaException.internalError("Min value must not be graeter than max value");
        }
    }

    /**
     * Default constructor
     */
    public RangePredicateImpl() {
    }

    public <V extends Comparable<V>> RangePredicateImpl(StorableField field, V minValue, V maxValue) {
        this(field.field(), minValue, maxValue);
    }

    /**
     * Construct a range predicate given the field and the values
     *
     * @param field
     * @param minValue
     * @param maxValue
     */
    public <V extends Comparable<V>> RangePredicateImpl(String field, V minValue, V maxValue) {
        this.field = field;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public String getField() {
        return field;
    }

    public RangePredicate setField(StorableField field) {
        return setField(field.field());
    }

    /**
     * Get the field
     *
     * @param field
     * @return
     */
    public RangePredicate setField(String field) {
        this.field = field;
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

    /**
     * Set the minimum value (typed)
     *
     * @param clazz
     * @param minValue
     * @return
     * @throws KapuaException
     */
    public <V extends Comparable<V>> RangePredicate setMinValue(Class<V> clazz, V minValue) throws KapuaException {
        this.minValue = minValue;
        checkRange(clazz);
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

    /**
     * Set the maximum value (typed)
     *
     * @param clazz
     * @param maxValue
     * @return
     * @throws KapuaException
     */
    public <V extends Comparable<V>> RangePredicate setMaxValue(Class<V> clazz, V maxValue) throws KapuaException {
        this.maxValue = maxValue;
        checkRange(clazz);
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
        termNode.set(field, valuesNode);
        rootNode.set(PredicateConstants.RANGE_KEY, termNode);
        return rootNode;
    }

}
