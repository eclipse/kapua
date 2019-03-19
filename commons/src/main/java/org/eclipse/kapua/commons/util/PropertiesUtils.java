/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

/**
 * Utilities around the Java {@link Properties} class.
 *
 * @since 1.1.0
 */
public class PropertiesUtils {

    private PropertiesUtils() {
    }

    /**
     * Reads the given {@link String} and loads it into the returned {@link Properties}.
     *
     * @param stringProperties The source {@link String}
     * @return The loaded {@link Properties}
     * @throws IOException If the {@link String} is not properly formatted.
     * @see Properties#load(Reader)
     * @since 1.1.0
     */
    public static Properties readPropertiesFromString(@Nullable String stringProperties) throws IOException {
        Properties props = new Properties();
        if (!Strings.isNullOrEmpty(stringProperties)) {
            try (Reader reader = new StringReader(stringProperties)) {
                props.load(reader);
            }
        }
        return props;
    }

    /**
     * Writes the given {@link Properties} into the returned {@link String}
     *
     * @param properties The source {@link Properties}
     * @return The written {@link Properties}.
     * @throws IOException if error occurs while writing properties.
     * @see Properties#store(Writer, String)
     * @since 1.1.0
     */
    public static String writePropertiesToString(@Nullable Properties properties) throws IOException {
        String stringProperties = null;
        if (properties != null) {
            try (Writer writer = new StringWriter()) {
                properties.store(writer, null);
                stringProperties = writer.toString();
            }
        }
        return stringProperties;
    }
}
