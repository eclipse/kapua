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
 * Boolean metric implementation
 *
 * @since 1.0
 */
public class BooleanMetric extends MetricImpl<Boolean> implements Metric<Boolean> {

    /**
     * Construct a boolean metric with the provided name and value
     *
     * @param name
     * @param value
     */
    public BooleanMetric(String name, Object value) {
        setName(name);
        setType(Boolean.class);
        setValue((Boolean) value);
    }

    @Override
    public int compareTo(Boolean o) {
        return getValue().compareTo(o);
    }
}
