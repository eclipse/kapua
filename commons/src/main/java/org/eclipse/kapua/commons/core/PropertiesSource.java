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
import java.util.Properties;

public class PropertiesSource implements ConfigurationSource {

    private Properties properties;

    public PropertiesSource(Properties props) {
        this.properties = props;
    }

    @Override
    public List<String> getKeys() {       
        List<String> keys = new ArrayList<>();
        for(Object o:properties.keySet()) {
            keys.add((String) o);
        }
        return keys;
    }

    @Override
    public String getString(String key) {
        return (String) this.properties.get(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer getInteger(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getLong(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        throw new UnsupportedOperationException();
    }
}
