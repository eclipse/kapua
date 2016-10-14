/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.model.query;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorableField;

public class RangePredicateImpl implements RangePredicate
{
    private StorableField field;
    private Object           minValue;
    private Object            maxValue;

    private <V extends Comparable<V>> void checkRange(Class<V> clazz) throws KapuaException
    {
        if (minValue == null || maxValue == null)
            return;
        
        V min = clazz.cast(minValue);
        V max = clazz.cast(maxValue);
        if (min.compareTo(max) > 0)
            throw KapuaException.internalError("Min value must not be graeter than max value");
    }
    
    public RangePredicateImpl()
    {
    }

    public <V extends Comparable<V>> RangePredicateImpl(StorableField field, V minValue, V maxValue)
    {
        this.field = field;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public StorableField getField()
    {
        return this.field;
    }

    public RangePredicate setField(StorableField field)
    {
        this.field = field;
        return this;
    }

    @Override
    public Object getMinValue()
    {
        return minValue;
    }

    @Override
    public <V extends Comparable<V>> V getMinValue(Class<V> clazz)
    {
        return clazz.cast(minValue);
    }

    public <V extends Comparable<V>> RangePredicate setMinValue(Class<V> clazz, V minValue) throws KapuaException
    {
        this.minValue = minValue;
        checkRange(clazz);
        return this;
    }

    @Override
    public Object getMaxValue()
    {
        return maxValue;
    }

    @Override
    public <V extends Comparable<V>> V getMaxValue(Class<V> clazz)
    {
        return clazz.cast(maxValue);
    }

    public <V extends Comparable<V>> RangePredicate setMaxValue(Class<V> clazz, V maxValue) throws KapuaException
    {
        this.maxValue = maxValue;
        checkRange(clazz);
        return this;
    }
}
