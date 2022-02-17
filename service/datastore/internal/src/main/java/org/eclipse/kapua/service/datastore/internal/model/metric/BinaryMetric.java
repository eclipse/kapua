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
 * Binary metric implementation
 *
 * @since 1.0
 */
public class BinaryMetric extends MetricImpl<Byte[]> implements Metric<Byte[]> {

    /**
     * Construct a binary metric with the provided name and value
     *
     * @param name
     * @param value
     */
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
