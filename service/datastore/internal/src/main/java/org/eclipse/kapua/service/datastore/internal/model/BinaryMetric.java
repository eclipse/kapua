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

public class BinaryMetric extends MetricImpl<Byte[]> implements Metric<Byte[]> {

    public BinaryMetric(String name, Object value) {
        setName(name);
        setType(Byte[].class);
        setValue((Byte[]) value);
    }

    @Override
    public int compareTo(Byte[] o) {
        if (getValue().length != o.length) {
            return getValue().length - o.length;
        }

        int i = 0;
        while (i < getValue().length) {

            if (!getValue()[i].equals(o[i])) {
                return getValue()[i].compareTo(o[i]);
            }

            i++;
        }

        return 0;
    }
}
