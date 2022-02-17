/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal.device.lifecycle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

/**
 * Birth extended property definition.
 *
 * @since 1.5.0
 */
@JsonRootName("deviceExtendedProperties")
public class BirthExtendedProperties {

    @JsonProperty("version")
    private String version;

    @JsonProperty("properties")
    @JsonDeserialize(keyAs = String.class, contentAs = BirthExtendedProperty.class)
    private Map<String, BirthExtendedProperty> extendedProperties;

    /**
     * Gets the version.
     *
     * @return The version.
     * @since 1.5.0
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version The version.
     * @since 1.5.0
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the {@link Map} of {@link BirthExtendedProperty}es.
     *
     * @return The {@link Map} of {@link BirthExtendedProperty}es.
     * @since 1.5.0
     */
    public Map<String, BirthExtendedProperty> getExtendedProperties() {
        if (extendedProperties == null) {
            extendedProperties = new HashMap<>();
        }

        return extendedProperties;
    }

    /**
     * Sets the {@link Map} of {@link BirthExtendedProperty}es.
     *
     * @param extendedProperties The {@link Map} of {@link BirthExtendedProperty}es.
     * @since 1.5.0
     */
    public void setExtendedProperties(Map<String, BirthExtendedProperty> extendedProperties) {
        this.extendedProperties = extendedProperties;
    }
}
