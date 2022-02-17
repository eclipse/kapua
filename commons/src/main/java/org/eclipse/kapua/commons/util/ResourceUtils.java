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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource utilities
 *
 * @since 1.0
 *
 */
public class ResourceUtils {

    public static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);

    private ResourceUtils() {
    }

    /**
     * Get the URL of a resource
     *
     * @param resource
     *            to locate
     * @return The URL to the resource, or {@code null} if it cannot be found
     */
    public static URL getResource(final String resource) {
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

    /**
     * Open a URL as {@link Reader}
     *
     * @param url
     *            the URL to open
     * @param charset
     *            the character set to use
     * @return the reader
     * @throws IOException
     *             If any I/O error occurs
     */
    public static Reader openAsReader(final URL url, final Charset charset) throws IOException {
        Objects.requireNonNull(url);
        Objects.requireNonNull(charset);

        return new InputStreamReader(url.openStream(), charset);
    }
}
