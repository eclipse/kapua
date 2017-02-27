/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
