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
package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.model.Metric;

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
        setValue(Double.valueOf((Double) value).doubleValue());
    }

    @Override
    public int compareTo(Double o) {
        return getValue().compareTo(o);
    }
}
