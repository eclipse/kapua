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
package org.eclipse.kapua.service.device.call.kura.model.inventory.bundles;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * {@link KuraInventoryBundle} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("systemPackage")
public class KuraInventoryBundle {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("version")
    public String version;

    @JsonProperty("state")
    public String state;

    @JsonProperty("signed")
    public Boolean signed;

    /**
     * Gets the identifier.
     *
     * @return The identifier.
     * @since 1.5.0
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the identifier.
     *
     * @param id The identifier.
     * @since 1.5.0
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return The name.
     * @since 1.5.0
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name The name.
     * @since 1.5.0
     */
    public void setName(String name) {
        this.name = name;
    }

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
     * Gets the version.
     *
     * @param version The version.
     * @since 1.5.0
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the state.
     *
     * @return The state.
     * @since 1.5.0
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state The state.
     * @since 1.5.0
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Whether the bundle is signed.
     *
     * @return {@code true} if is signed, {@code false} if not or {@code null} if the information is not known.
     * @since 1.6.0
     */
    public Boolean getSigned() {
        return signed;
    }

    /**
     * Sets whether the bundle is signed.
     *
     * @param signed {@code true} if is signed, {@code false} if not or {@code null} if the information is not known.
     * @since 1.6.0
     */
    public void setSigned(Boolean signed) {
        this.signed = signed;
    }
}
