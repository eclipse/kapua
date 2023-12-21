/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.kura.model.inventory.containers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * {@link KuraInventoryContainer} definition.
 *
 * @since 2.0.0
 */
@JsonRootName("container")
public class KuraInventoryContainer {

    @JsonProperty("name")
    public String name;

    @JsonProperty("version")
    public String version;

    @JsonProperty("type")
    public String type;

    @JsonProperty("state")
    public String state;

    /**
     * Gets the name.
     *
     * @return The name.
     * @since 2.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name The name.
     * @since 2.0.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the version.
     *
     * @return The version.
     * @since 2.0.0
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the version.
     *
     * @param version The version.
     * @since 2.0.0
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the type.
     *
     * @return The type.
     * @since 2.0.0
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type The type.
     * @since 2.0.0
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the state
     *
     * @return The state
     * @since 2.0.0
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state
     *
     * @param state The state
     * @since 2.0.0
     */
    public void setState(String state) {
        this.state = state;
    }
}
