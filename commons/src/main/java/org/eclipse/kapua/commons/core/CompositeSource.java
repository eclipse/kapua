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
        return (String)config.getString(key);
    }

}
