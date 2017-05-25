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
 * Long metric implementation
 * 
 * @since 1.0
 */
public class LongMetric extends MetricImpl<Long> implements Metric<Long> {

    /**
     * Construct a long metric with the provided name and value
     * 
     * @param name
     * @param value
     */
    public LongMetric(String name, Object value) {
        setName(name);
        setType(Long.class);
        setValue(Long.valueOf((Long) value).longValue());
    }

    @Override
    public int compareTo(Long o) {
        return getValue().compareTo(o);
    }
}
