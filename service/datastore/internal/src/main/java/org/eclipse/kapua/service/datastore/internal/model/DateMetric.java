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

import java.util.Date;

import org.eclipse.kapua.service.datastore.model.Metric;

public class DateMetric extends MetricImpl<Date> implements Metric<Date>{

    public DateMetric(String name, Object value) {
        setName(name);
        setType(Date.class);
        setValue((Date) value);
    }
    
    @Override
    public int compareTo(Date o) {
        return getValue().compareTo(o);
    }
}
