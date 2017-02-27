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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource utilities
 * 
 * @since 1.0
 *
 */
public class ResourceUtils 
{
    public static final Logger s_logger = LoggerFactory.getLogger(ResourceUtils.class);

    /**
     * Get the URL of a resource
     * 
     * @param resource to locate
     * @return The URL to the resource, or {@code null} if it cannot be found
     */
    public static URL getResource(String resource) {
        //Try with the Thread Context Loader.
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader != null) {
            URL url = classLoader.getResource(resource);
            if(url != null) {
                return url;
            }
        }

        //Let's now try with the classloader that loaded this class.
        classLoader = ResourceUtils.class.getClassLoader();
        if(classLoader != null) {
            URL url = classLoader.getResource(resource);
            if(url != null) {
                return url;
            }
        }

        //Last ditch attempt. Get the resource from the classpath.
        return ClassLoader.getSystemResource(resource);
    }


    /**
     * Reads a resource fully and returns it as a string.
     *
     * @param resourceUrl
     * @return
     * @throws IOException
     */
    public static String readResource(URL resourceUrl)
    throws IOException {
        String result = null;
        BufferedReader resourceBr = null;
        InputStreamReader resourceIsr = null;
        try {

            resourceIsr = new InputStreamReader(resourceUrl.openStream());
            resourceBr  = new BufferedReader(resourceIsr);

            int iRead = 0;
            char[] buffer = new char[1024];
            StringWriter sw = new StringWriter();
            while ((iRead = resourceBr.read(buffer)) != -1) {
                sw.write(buffer, 0, iRead);
            }
            result = sw.toString();
        } finally {
            if (resourceBr != null) {
                try {
                    resourceBr.close();
                } catch (IOException e)  {
                    s_logger.warn("Error closing reader", e);
                }
                try {
                    resourceIsr.close();
                } catch (IOException e)  {
                    s_logger.warn("Error closing reader", e);
                }
            }
        }
        return result;
    }
}
