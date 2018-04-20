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
        setValue(Integer.valueOf((Integer) value).intValue());
    }

    @Override
    public int compareTo(Integer o) {
        return super.getValue().compareTo(o);
    }
}
