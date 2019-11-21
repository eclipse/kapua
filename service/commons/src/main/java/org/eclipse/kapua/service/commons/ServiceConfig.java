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
package org.eclipse.kapua.service.commons;

import java.util.HashMap;
import java.util.Map;

public class ServiceConfig {

    private String name;
    private String type;
    private int instances;

    private Map<String, String> properties = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getType() {
        return type;
    }

    public void setType(String aType) {
        type = aType;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int noInstances) {
        instances = noInstances;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> someProperties) {
        properties = someProperties;
    }

    @Override
    public String toString() {
        return String.format("\"name\":\"%s\""
                + ", \"type\":\"%s\""
                + ", \"instances\":\"%d\""
                + ", \"properties\":{%s}", name, type, instances, properties == null ? "null" : properties.toString());
    }
}