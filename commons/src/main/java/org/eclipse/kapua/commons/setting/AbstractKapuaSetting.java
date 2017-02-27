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
package org.eclipse.kapua.commons.setting;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.DataConfiguration;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Setting reference abstract implementation.
 *
 * @param <K>
 *            setting key type
 *
 * @since 1.0
 *
 */
public abstract class AbstractKapuaSetting<K extends SettingKey> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractKapuaSetting.class);

    protected DataConfiguration config;

    /**
     * Constructor
     *
     * @param configResourceName
     */
    protected AbstractKapuaSetting(String configResourceName) {
        CompositeConfiguration compositeConfig = new EnvFriendlyConfiguration();
        compositeConfig.addConfiguration(new SystemConfiguration());
        compositeConfig.addConfiguration(new EnvironmentConfiguration());
        try {
            URL configLocalUrl = ResourceUtils.getResource(configResourceName);
            if (configLocalUrl == null) {
                logger.warn("Unable to locate resource '{}'", configResourceName);
                throw new IllegalArgumentException(String.format("Unable to locate resource: '%s'", configResourceName));
            }
            compositeConfig.addConfiguration(new PropertiesConfiguration(configLocalUrl));
        } catch (Exception e) {
            logger.error("Error loading PropertiesConfiguration", e);
            throw new ExceptionInInitializerError(e);
        }

        this.config = new DataConfiguration(compositeConfig);
    }

    /**
     * Get the property for the key
     *
     * @param cls
     * @param key
     * @return
     */
    public <T> T get(Class<T> cls, K key) {
        return config.get(cls, key.key());
    }

    /**
     * Get the property for the key 'key'. Returns a default value if no property for that key is found.
     *
     * @param cls
     * @param key
     * @param defaultValue
     * @return
     */
    public <T> T get(Class<T> cls, K key, T defaultValue) {
        return config.get(cls, key.key(), defaultValue);
    }

    /**
     * Get the property list values for the key 'key'
     *
     * @param cls
     * @param key
     * @return
     */
    public <T> List<T> getList(Class<T> cls, K key) {
        return config.getList(cls, key.key());
    }

    /**
     * Get properties map with key matching the provided prefix and regex
     *
     * @param valueType
     * @param prefixKey
     * @param regex
     * @return
     */
    public <V> Map<String, V> getMap(Class<V> valueType, K prefixKey, String regex) {
        Map<String, V> map = new HashMap<>();
        Configuration subsetConfig = config.subset(prefixKey.key());
        DataConfiguration subsetDataConfig = new DataConfiguration(subsetConfig);
        for (Iterator<String> it = subsetConfig.getKeys(); it.hasNext();) {
            String key = it.next();
            if (Pattern.matches(regex, key)) {
                map.put(key, subsetDataConfig.get(valueType, key));
            }
        }
        return map;
    }

    /**
     * Get properties map with key matching the provided prefix
     *
     * @param valueType
     * @param prefixKey
     * @return
     */
    public <V> Map<String, V> getMap(Class<V> valueType, K prefixKey) {
        Map<String, V> map = new HashMap<>();
        Configuration subsetConfig = config.subset(prefixKey.key());
        DataConfiguration subsetDataConfig = new DataConfiguration(subsetConfig);
        for (Iterator<String> it = subsetConfig.getKeys(); it.hasNext();) {
            String key = it.next();
            map.put(key, subsetDataConfig.get(valueType, key));
        }
        return map;
    }

    /**
     * Get an integer property
     *
     * @param key
     * @return
     */
    public int getInt(K key) {
        return config.getInt(key.key());
    }

    /**
     * Get an integer property with a default value (if property is not found)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(K key, int defaultValue) {
        return config.getInt(key.key(), defaultValue);
    }

    /**
     * Get an integer property with a default value (if property is not found)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(K key, Integer defaultValue) {
        return config.getInt(key.key(), defaultValue);
    }

    /**
     * Get a boolean property
     *
     * @param key
     * @return
     */
    public boolean getBoolean(K key) {
        return config.getBoolean(key.key());
    }

    /**
     * Get a boolean property with a default value (if property is not found)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(K key, boolean defaultValue) {
        return config.getBoolean(key.key(), defaultValue);
    }

    /**
     * Get a boolean property with a default value (if property is not found)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(K key, Boolean defaultValue) {
        return config.getBoolean(key.key(), defaultValue);
    }

    /**
     * Get a String property
     *
     * @param key
     * @return
     */
    public String getString(K key) {
        return config.getString(key.key());
    }

    /**
     * Get a String property with a default value (if property is not found)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(K key, String defaultValue) {
        return config.getString(key.key(), defaultValue);
    }

    /**
     * Get a long property
     *
     * @param key
     * @return
     */
    public long getLong(K key) {
        return config.getLong(key.key());
    }

    /**
     * Get a long property with a default value (if property is not found)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLong(K key, long defaultValue) {
        return config.getLong(key.key(), defaultValue);
    }

    /**
     * Get a long property with a default value (if property is not found)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLong(K key, Long defaultValue) {
        return config.getLong(key.key(), defaultValue);
    }

    /**
     * Get a float property
     *
     * @param key
     * @return
     */
    public float getFloat(K key) {
        return config.getFloat(key.key());
    }

    /**
     * Get a float property with a default value (if property is not found)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public float getFloat(K key, float defaultValue) {
        return config.getFloat(key.key(), defaultValue);
    }

    /**
     * Get a float property with a default value (if property is not found)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public float getFloat(K key, Float defaultValue) {
        return config.getFloat(key.key(), defaultValue);
    }

    /**
     * Get a double property
     *
     * @param key
     * @return
     */
    public double getDouble(K key) {
        return config.getDouble(key.key());
    }

    /**
     * Get a double property with a default value (if property is not found)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public double getDouble(K key, double defaultValue) {
        return config.getDouble(key.key(), defaultValue);
    }

    /**
     * Get a double property with a default value (if property is not found)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public double getDouble(K key, Double defaultValue) {
        return config.getDouble(key.key(), defaultValue);
    }
}
