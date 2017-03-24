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

public abstract class MetricImpl<T> implements Metric<T> {

    private String name;
    private Class<T> type;
    private T value;
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
      this.name = name;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public void setType(Class<T> type) {
        this.type = type;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
