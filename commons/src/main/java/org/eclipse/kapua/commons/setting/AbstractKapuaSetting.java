/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
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
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractKapuaSetting<K extends SettingKey>
{
    private static final Logger s_logger = LoggerFactory.getLogger(AbstractKapuaSetting.class);

    protected DataConfiguration config;

    protected AbstractKapuaSetting(String configResourceName)
    {
        // env+properties configuration
        CompositeConfiguration compositeConfig = new CompositeConfiguration();
        compositeConfig.addConfiguration(new SystemConfiguration());
        try {
            URL configLocalUrl = ResourceUtils.getResource(configResourceName);
            compositeConfig.addConfiguration(new PropertiesConfiguration(configLocalUrl));
        }
        catch (Exception e) {
            s_logger.error("Error loading PropertiesConfiguration", e);
            throw new ExceptionInInitializerError(e);
        }

        this.config = new DataConfiguration(compositeConfig);
    }

    public <T> T get(Class<T> cls, K key)
    {
        return config.get(cls, key.key());
    }

    public <T> T get(Class<T> cls, K key, T defaultValue)
    {
        return config.get(cls, key.key(), defaultValue);
    }

    public <T> List<T> getList(Class<T> cls, K key)
    {
        return config.getList(cls, key.key());
    }

    public <V> Map<String, V> getMap(Class<V> valueType, K prefixKey, String regex)
    {
        Map<String, V> map = new HashMap<String, V>();
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

    public <V> Map<String, V> getMap(Class<V> valueType, K prefixKey)
    {
        Map<String, V> map = new HashMap<String, V>();
        Configuration subsetConfig = config.subset(prefixKey.key());
        DataConfiguration subsetDataConfig = new DataConfiguration(subsetConfig);
        for (Iterator<String> it = subsetConfig.getKeys(); it.hasNext();) {
            String key = it.next();
            map.put(key, subsetDataConfig.get(valueType, key));
        }
        return map;
    }

    public int getInt(K key)
    {
        return config.getInt(key.key());
    }

    public int getInt(K key, int defaultValue)
    {
        return config.getInt(key.key(), defaultValue);
    }

    public int getInt(K key, Integer defaultValue)
    {
        return config.getInt(key.key(), defaultValue);
    }

    public boolean getBoolean(K key)
    {
        return config.getBoolean(key.key());
    }

    public boolean getBoolean(K key, boolean defaultValue)
    {
        return config.getBoolean(key.key(), defaultValue);
    }

    public boolean getBoolean(K key, Boolean defaultValue)
    {
        return config.getBoolean(key.key(), defaultValue);
    }

    public String getString(K key)
    {
        return config.getString(key.key());
    }

    public String getString(K key, String defaultValue)
    {
        return config.getString(key.key(), defaultValue);
    }

    public long getLong(K key)
    {
        return config.getLong(key.key());
    }

    public long getLong(K key, long defaultValue)
    {
        return config.getLong(key.key(), defaultValue);
    }

    public long getLong(K key, Long defaultValue)
    {
        return config.getLong(key.key(), defaultValue);
    }

    public float getFloat(K key)
    {
        return config.getFloat(key.key());
    }

    public float getFloat(K key, float defaultValue)
    {
        return config.getFloat(key.key(), defaultValue);
    }

    public float getFloat(K key, Float defaultValue)
    {
        return config.getFloat(key.key(), defaultValue);
    }

    public double getDouble(K key)
    {
        return config.getDouble(key.key());
    }

    public double getDouble(K key, double defaultValue)
    {
        return config.getDouble(key.key(), defaultValue);
    }

    public double getDouble(K key, Double defaultValue)
    {
        return config.getDouble(key.key(), defaultValue);
    }
}
