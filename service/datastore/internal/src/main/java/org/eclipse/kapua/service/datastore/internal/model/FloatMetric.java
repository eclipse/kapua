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
 * Float metric implementation
 * 
 * @since 1.0
 */
public class FloatMetric extends MetricImpl<Float> implements Metric<Float> {

    /**
     * Construct a float metric with the provided name and value
     * 
     * @param name
     * @param value
     */
    public FloatMetric(String name, Object value) {
        setName(name);
        setType(Float.class);
        setValue(new Float((Float) value).floatValue());
    }

    @Override
    public int compareTo(Float o) {
        return getValue().compareTo(o);
    }
}
