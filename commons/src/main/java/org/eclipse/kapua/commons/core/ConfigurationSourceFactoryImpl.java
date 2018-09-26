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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.eclipse.kapua.commons.core.spi.ConfigurationSourceFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Creates a {@link ConfigurationSource} from a JSON file specified by the invoker of 
 * method .
 * <p>
 * Keys are obtained from the JSON by concatenating JSON keys in a dotted notation 
 * (e.g. "comp.feature.property").
 */
public class ConfigurationSourceFactoryImpl implements ConfigurationSourceFactory {

    private static Logger logger = LoggerFactory.getLogger(ConfigurationSourceFactoryImpl.class);

    private static final String EXTERNAL_CONFIG_URL_PARAM = "kapua.application.config.url";

    public ConfigurationSource create(ClassLoader classLoader, String name) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
//      JavaPropsMapper propsMapper = new JavaPropsMapper();

        CompositeConfiguration compositeConfig = new CompositeConfiguration();
        compositeConfig.addConfiguration(new SystemConfiguration());
        compositeConfig.addConfiguration(new EnvironmentConfiguration());
        HashMap<String, Object> m = null;
        if (compositeConfig.containsKey(EXTERNAL_CONFIG_URL_PARAM)) {
            m = readValueFromUrl(mapper, compositeConfig.getString(EXTERNAL_CONFIG_URL_PARAM));
        } else {
            m = readValueFromResource(mapper, classLoader, name);
        }
//            String q = propsMapper.writeValueAsString(m);
//            Properties props = new Properties();
//            props.load(new StringReader(q));
        Properties props = new Properties();
        toProperties(m, "", props);

        compositeConfig.addConfiguration(ConfigurationConverter.getConfiguration(props));
        CompositeSource compositeSource = new CompositeSource(compositeConfig);
        return compositeSource;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> readValueFromUrl(ObjectMapper mapper, String urlStr) throws Exception {
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

    // TODO Replace with jackson JavaPropsMapper
    private void toProperties(Map<String, Object> map, String prefix, Properties props) {
        for(String key:map.keySet()) {
            Object value = map.get(key);
            String propKey = prefix + (prefix.isEmpty() ? "" : ".") + key;
            if (value == null) {
                props.setProperty(propKey, "");   
            } else if (value instanceof String) {
                props.setProperty(propKey, (String) value);  
            } else if (value instanceof Boolean) {
                props.setProperty(propKey, Boolean.toString((Boolean) value));
            } else if (value instanceof Integer) {
                props.setProperty(propKey, Integer.toString((int) value));
            } else if (value instanceof Long) {
                props.setProperty(propKey, Long.toString((long) value));
            } else if (value instanceof Float) {
                props.setProperty(propKey, Float.toString((float) value));
            } else if (value instanceof Double) {
                props.setProperty(propKey, Double.toString((double) value));
            } else if (value instanceof Map) {
                toProperties((Map<String, Object>)value, propKey, props);
            } else {
                throw new RuntimeException("Un recognized value type: " + value.toString());
            }
        }
    }
}
