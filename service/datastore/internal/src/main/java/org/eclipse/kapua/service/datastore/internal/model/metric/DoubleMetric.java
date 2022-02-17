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
package org.eclipse.kapua.service.datastore.internal.model.metric;

import org.eclipse.kapua.service.datastore.model.metric.Metric;

/**
 * Double metric implementation
 *
 * @since 1.0
 */
public class DoubleMetric extends MetricImpl<Double> implements Metric<Double> {

    /**
     * Construct a double metric with the provided name and value
     *
     * @param name
     * @param value
     */
    public DoubleMetric(String name, Object value) {
        setName(name);
        setType(Double.class);
        setValue((Double) value);
    }

    @Override
    public int compareTo(Double o) {
        return getValue().compareTo(o);
    }
}
