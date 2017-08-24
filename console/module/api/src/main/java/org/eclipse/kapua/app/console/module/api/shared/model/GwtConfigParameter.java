/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.shared.model;

import java.io.Serializable;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtConfigParameter implements Serializable {

    private static final long serialVersionUID = -1738441153196315880L;

    public enum GwtConfigParameterType implements Serializable, IsSerializable {
        STRING, LONG, DOUBLE, FLOAT, INTEGER, BYTE, CHAR, BOOLEAN, SHORT, PASSWORD;

        GwtConfigParameterType() {
        }

        public static GwtConfigParameterType fromString(String enumString) {
            for (GwtConfigParameterType p : GwtConfigParameterType.values()) {
                if (p.name().toLowerCase().equals(enumString.toLowerCase())) {
                    return p;
                }
            }
            return null;
        }
    }

    private String id;
    private String name;
    private String description;
    private GwtConfigParameterType type;
    private boolean required;
    private String defaultValue;
    private int cardinality;
    private Map<String, String> options;
    private String min;
    private String max;
    private String value;      // used for fields with single cardinality
    private String[] values;     // used for fields with multiple cardinality

    public GwtConfigParameter() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GwtConfigParameterType getType() {
        return type;
    }

    public void setType(GwtConfigParameterType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefault() {
        return defaultValue;
    }

    public void setDefault(String default1) {
        this.defaultValue = default1;
    }

    public int getCardinality() {
        return cardinality;
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
