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
package org.eclipse.kapua.service.datastore.internal.model.query.predicate;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.datastore.model.query.predicate.ChannelMatchPredicate;
import org.eclipse.kapua.service.datastore.model.query.predicate.DatastorePredicateFactory;
import org.eclipse.kapua.service.datastore.model.query.predicate.MetricExistsPredicate;
import org.eclipse.kapua.service.datastore.model.query.predicate.MetricPredicate;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateFactoryImpl;

/**
 * {@link DatastorePredicateFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DatastorePredicateFactoryImpl extends StorablePredicateFactoryImpl implements DatastorePredicateFactory {

    @Override
    public ChannelMatchPredicate newChannelMatchPredicate(String expression) {
        return new ChannelMatchPredicateImpl(expression);
    }

    @Override
    public <V extends Comparable<V>> MetricPredicate newMetricPredicate(String field, Class<V> type, V minValue, V maxValue) {
        return new MetricPredicateImpl(field, type, minValue, maxValue);
    }

    @Override
    public <V extends Comparable<V>> MetricExistsPredicate newMetricExistsPredicate(String metricName, Class<V> type) {
        return new MetricExistsPredicateImpl(metricName, type);
    }
}
