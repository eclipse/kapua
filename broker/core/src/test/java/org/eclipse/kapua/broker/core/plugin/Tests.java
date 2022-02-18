/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import java.util.HashMap;
import java.util.Map;

public final class Tests {

    private Tests() {
    }

    public static <X extends Throwable> void runWithProperties(Map<String, String> properties, TestFragment<X> fragment) throws X {
        final Map<String, String> old = new HashMap<>(properties.size());

        // set properties

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            String value = System.setProperty(entry.getKey(), entry.getValue());
            old.put(entry.getKey(), value);
        }

        try {

            // run fragment

            fragment.run();
        } finally {

            // restore state

            for (Map.Entry<String, String> entry : old.entrySet()) {
                String value = entry.getValue();
                if (value == null) {
                    System.clearProperty(entry.getKey());
                } else {
                    System.setProperty(entry.getKey(), value);
                }
            }
        }
    }

}
