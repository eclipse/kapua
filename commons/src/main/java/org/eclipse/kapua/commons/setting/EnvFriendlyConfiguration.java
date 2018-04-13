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
 *******************************************************************************/
package org.eclipse.kapua.commons.setting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.configuration.CompositeConfiguration;

import com.google.common.collect.ImmutableList;

public class EnvFriendlyConfiguration extends CompositeConfiguration {

    @Override
    public Iterator<String> getKeys() {
        List<String> keys = ImmutableList.copyOf(super.getKeys());
        List<String> envKeys = keys.stream().map(this::envKey).collect(Collectors.toList());
        List<String> result = new ArrayList<>();
        result.addAll(keys);
        result.addAll(envKeys);
        return result.iterator();
    }

    @Override
    public Object getProperty(String key) {
        Object property = super.getProperty(key);
        if(property != null) {
            return property;
        }
        property = super.getProperty(envKey(key));
        if(property != null) {
            return property;
        } else {
            return null;
        }
    }

    @Override
    public boolean containsKey(String key) {
        boolean contains = super.containsKey(key);
        return contains || super.containsKey(envKey(key));
    }

    private String envKey(String key) {
        return key.toUpperCase().replaceAll("\\.", "_");
    }

}
