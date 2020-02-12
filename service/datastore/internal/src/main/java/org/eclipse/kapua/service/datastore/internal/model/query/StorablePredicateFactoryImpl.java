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

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ChannelMatchPredicate;
import org.eclipse.kapua.service.datastore.model.query.ExistsPredicate;
import org.eclipse.kapua.service.datastore.model.query.MetricExistsPredicate;
import org.eclipse.kapua.service.datastore.model.query.MetricPredicate;
import org.eclipse.kapua.service.datastore.model.query.OrPredicate;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorableField;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicateFactory;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;

@KapuaProvider
public class StorablePredicateFactoryImpl implements StorablePredicateFactory {

    @Override
    public AndPredicate newAndPredicate() {
        return new AndPredicateImpl();
    }

    @Override
    public ChannelMatchPredicate newChannelMatchPredicate(String expression) {
        return new ChannelMatchPredicateImpl(expression);
    }

    @Override
    public <V extends Comparable<V>> RangePredicate newRangePredicate(String fieldName, V minValue, V maxValue) {
        return new RangePredicateImpl(fieldName, minValue, maxValue);
    }

    @Override
    public <V extends Comparable<V>> MetricPredicate newMetricPredicate(String fieldName, Class<V> type, V minValue, V maxValue) {
        return new MetricPredicateImpl(fieldName, type, minValue, maxValue);
    }

    @Override
    public <V> TermPredicate newTermPredicate(StorableField field, V value) {
        return new TermPredicateImpl(field, value);
    }

    @Override
    public ExistsPredicate newExistsPredicate(String fieldName) {
        return new ExistsPredicateImpl(fieldName);
    }

    @Override
    public <V extends Comparable<V>> MetricExistsPredicate newMetricExistsPredicate(String fieldName, Class<V> type) {
        return new MetricExistsPredicateImpl(fieldName, type);
    }

    @Override
    public OrPredicate newOrPredicate() {
        return new OrPredicateImpl();
    }

}
