/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.utils;

import com.codahale.metrics.Counter;

import java.util.Arrays;

public class Utils {

    private Utils() {

    }

    /**
     * Initialize an array of Counter
     *
     * @param counters the counters to be initialized
     */
    public static void initCounter(Counter... counters){
        Arrays.stream(counters).forEach(c -> {
            long counterValue = c.getCount();
            if (counterValue > 0) {
                c.dec(counterValue);
            } else {
                c.inc(counterValue);
            }
        });
    }
}
