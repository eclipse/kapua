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
package org.eclipse.kapua.commons.util;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;

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

    /**
     * Reads a resource fully and returns it as a string
     *
     * @param resourceUrl
     *            The URL to read from
     * @param charset
     *            the character set used to read the file
     * @return the resource content as string
     * @throws IOException
     *             in case of an I/O error
     */
    public static String readResource(final URL resourceUrl, final Charset charset) throws IOException {
        return Resources.toString(resourceUrl, charset);
    }

    /**
     * Reads a resource fully and returns it as a byte array
     * 
     * @param resourceUrl
     *            The URL to read from
     * @return the resource content as byte array
     * @throws IOException
     *             in case of an I/O error
     */
    public static byte[] readResourceAsByteArray(final URL resourceUrl) throws IOException {
        return Resources.toByteArray(resourceUrl);
    }
}
