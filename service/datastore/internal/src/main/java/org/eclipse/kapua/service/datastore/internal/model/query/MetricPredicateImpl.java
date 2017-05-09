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

import org.eclipse.kapua.service.datastore.model.query.MetricPredicate;

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
}
