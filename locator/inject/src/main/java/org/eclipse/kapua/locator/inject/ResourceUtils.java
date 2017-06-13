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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.locator.inject;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource utilities
 *
 * @since 1.0
 *
 */
public class ResourceUtils {

    private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);

    private ResourceUtils() {
    }

    /**
     * Get the URL of a resource
     *
     * @param resource
     *            to locate
     * @return The URL to the resource, or {@code null} if it cannot be found
     */
    public static URL getResource(String resource) {
        // Try with the Thread Context Loader.
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            URL url = classLoader.getResource(resource);
            if (url != null) {
                return url;
            }
        }

        // Let's now try with the classloader that loaded this class.
        classLoader = ResourceUtils.class.getClassLoader();
        if (classLoader != null) {
            URL url = classLoader.getResource(resource);
            if (url != null) {
                return url;
            }
        }

        // Last ditch attempt. Get the resource from the classpath.
        return ClassLoader.getSystemResource(resource);
    }
}
