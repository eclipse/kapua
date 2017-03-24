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
 * String metric implementation
 * 
 * @since 1.0
 */
public class StringMetric extends MetricImpl<String> implements Metric<String> {

    /**
     * Construct an integer metric with the provided name and value
     * 
     * @param name
     * @param value
     */
    public StringMetric(String name, Object value) {
        setName(name);
        setType(String.class);
        setValue(new String((String) value));
    }

    @Override
    public int compareTo(String o) {
        return getValue().compareTo(o);
    }
}
