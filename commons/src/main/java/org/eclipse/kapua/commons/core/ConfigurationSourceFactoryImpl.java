/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.eclipse.kapua.commons.core.spi.ConfigurationSourceFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;

/**
 * Creates a {@link ConfigurationSource} from a JSON file specified by the invoker of 
 * method .
 * <p>
 * Keys are obtained from the JSON by concatenating JSON keys in a dotted notation 
 * (e.g. "comp.feature.property").
 */
public class ConfigurationSourceFactoryImpl implements ConfigurationSourceFactory {

    private static final String EXTERNAL_CONFIG_URL_PARAM = "kapua.application.config.url";

    public ConfigurationSource create(ClassLoader classLoader, String name) throws IOException {

        // Define a composite configuration with priority to system properties then
        // environment variables then local resources.
        CompositeConfiguration compositeConfig = new CompositeConfiguration();
        compositeConfig.addConfiguration(new SystemConfiguration());
        compositeConfig.addConfiguration(new EnvironmentConfiguration());

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> object = null;
        if (compositeConfig.containsKey(EXTERNAL_CONFIG_URL_PARAM)) {
            object = readValueFromUrl(objectMapper, compositeConfig.getString(EXTERNAL_CONFIG_URL_PARAM));
        } else {
            object = readValueFromResource(objectMapper, classLoader, name);
        }

        JavaPropsMapper propsMapper = new JavaPropsMapper();
        String q = propsMapper.writeValueAsString(object);
        Properties props = new Properties();
        props.load(new StringReader(q));

        compositeConfig.addConfiguration(ConfigurationConverter.getConfiguration(props));
        CompositeSource compositeSource = new CompositeSource(compositeConfig);
        return compositeSource;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> readValueFromUrl(ObjectMapper mapper, String urlStr) throws IOException {
        InputStream inputStream = null;
        HashMap<String, Object> map = null;
        try {
            URL url = new URL(urlStr);
            inputStream = url.openStream();
            map = mapper.readValue(inputStream, HashMap.class);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> readValueFromResource(ObjectMapper mapper, ClassLoader classLoader, String name) throws IOException {
        InputStream inputStream = null;
        HashMap<String, Object> map = null;
        try {
            inputStream = classLoader.getResourceAsStream(name);
            map = mapper.readValue(inputStream, HashMap.class);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return map;
    }
}
