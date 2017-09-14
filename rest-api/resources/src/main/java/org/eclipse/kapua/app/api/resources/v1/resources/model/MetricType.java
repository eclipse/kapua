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
package org.eclipse.kapua.app.api.resources.v1.resources.model;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.model.type.ObjectTypeConverter;

import com.google.common.base.Strings;

public class MetricType<V extends Comparable<V>> {

    private Class<V> type;

    @SuppressWarnings("unchecked")
    public MetricType(String stringMetricType) throws KapuaIllegalArgumentException {
        if (!Strings.isNullOrEmpty(stringMetricType)) {
            try {
                type = (Class<V>) ObjectTypeConverter.fromString(stringMetricType);
            } catch (ClassNotFoundException e) {
                throw new KapuaIllegalArgumentException("type", stringMetricType);
            }
        }
    }

    public Class<V> getType() {
        return type;
    }

}
