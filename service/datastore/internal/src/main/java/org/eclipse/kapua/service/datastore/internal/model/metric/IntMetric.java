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
 * Integer metric implementation
 *
 * @since 1.0
 */
public class IntMetric extends MetricImpl<Integer> implements Metric<Integer> {

    /**
     * Construct an integer metric with the provided name and value
     *
     * @param name
     * @param value
     */
    public IntMetric(String name, Object value) {
        setName(name);
        setType(Integer.class);
        setValue((Integer) value);
    }

    @Override
    public int compareTo(Integer o) {
        return super.getValue().compareTo(o);
    }
}
