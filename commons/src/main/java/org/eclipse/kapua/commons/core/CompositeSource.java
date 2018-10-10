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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;

public class CompositeSource implements ConfigurationSource {

    private CompositeConfiguration config;

    public CompositeSource(CompositeConfiguration config) {
        this.config = config;
    }

    @Override
    public List<String> getKeys() {       
        List<String> keys = new ArrayList<>();
        if (config != null) {
            config.getKeys().forEachRemaining(key -> {
                keys.add(key);
            });
        }
        return keys;
    }

    @Override
    public String getString(String key) {
        return config.getString(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    @Override
    public Integer getInteger(String key) {
        return config.getInt(key);
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        return config.getInt(key, defaultValue);
    }

    @Override
    public Long getLong(String key) {
        return config.getLong(key);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return config.getLong(key, defaultValue);
    }

}
