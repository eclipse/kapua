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

import java.util.List;

public class ConfigurationImpl implements Configuration {

    private ConfigurationSource configSource;

    public ConfigurationSource getSource() {
        return configSource;
    }

    public void setSource(ConfigurationSource source) {
        this.configSource = source;
    }

    @Override
    public List<String> getKeys() {
        return configSource.getKeys();
    }

    @Override
    public String getString(String key) {
        return configSource.getString(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return configSource.getString(key, defaultValue);
    }

    @Override
    public Integer getInteger(String key) {
        return configSource.getInteger(key);
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        return configSource.getInteger(key, defaultValue);
    }

    @Override
    public Long getLong(String key) {
        return configSource.getLong(key);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return configSource.getLong(key, defaultValue);
    }
}
