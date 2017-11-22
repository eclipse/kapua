/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.user.steps;

import java.util.Map;

/**
 * Data object used in Gherkin to transfer configuration data.
 */
public class TestConfig {

    /** Name of type that config value has. */
    private String type;

    /** Name of config parameter. */
    private String name;

    /** String representation of parameter value. */
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Adds this config parameter to specified map.
     *
     * @param valueMap
     *            map to add parameter to
     */
    public void addConfigToMap(Map<String, Object> valueMap) {

        switch (type) {
        case "integer":
            valueMap.put(name, Integer.valueOf(value));
            break;
        case "string":
            valueMap.put(name, value);
            break;
        case "boolean":
            valueMap.put(name, Boolean.valueOf(value));
            break;
        }
    }
}
