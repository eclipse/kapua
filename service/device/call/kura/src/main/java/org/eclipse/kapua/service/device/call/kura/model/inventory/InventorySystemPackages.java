/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.kura.model.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link InventorySystemPackages} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("inventorySystemPackages")
public class InventorySystemPackages {

    @JsonProperty("inventory")
    public List<InventorySystemPackage> systemPackages;

    /**
     * Gets the {@link InventorySystemPackage}s {@link List}.
     *
     * @return The {@link InventorySystemPackage}s {@link List}.
     * @since 1.5.0
     */
    public List<InventorySystemPackage> getSystemPackages() {
        if (systemPackages == null) {
            systemPackages = new ArrayList<>();
        }

        return systemPackages;
    }

    /**
     * Adds a {@link InventorySystemPackage} to the {@link List}
     *
     * @param kuraBundle The {@link InventorySystemPackage} to add.
     * @since 1.5.0
     */
    public void addSystemPackage(@NotNull InventorySystemPackage kuraBundle) {
        getSystemPackages().add(kuraBundle);
    }

    /**
     * Sets the {@link InventorySystemPackage}s {@link List}.
     *
     * @param systemPackages The {@link InventorySystemPackage}s {@link List}.
     * @since 1.5.0
     */
    public void setSystemPackages(@Nullable List<InventorySystemPackage> systemPackages) {
        this.systemPackages = systemPackages;
    }

}
