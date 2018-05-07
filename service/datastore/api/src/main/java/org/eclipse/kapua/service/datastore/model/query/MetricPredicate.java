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
package org.eclipse.kapua.service.datastore.model.query;

/**
 * Query predicate for matching range values on message metrics implementation
 *
 * @since 1.0.0
 */
public interface MetricPredicate extends RangePredicate {

    /**
     * Gets the metric type to search.
     * This is required because metric with the same name can have different types.
     *
     * @return The metric type
     * @since 1.0.0
     */
    Class<?> getType();

    /**
     * Sets the metric type so search.
     *
     * @param type The metric type to search.
     * @since 1.0.0
     */
    void setType(Class<?> type);

}
