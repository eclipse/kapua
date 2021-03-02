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
 * {@link KuraInventoryPackages} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("inventoryPackages")
public class KuraInventoryPackages {

    @JsonProperty("inventory")
    public List<KuraInventoryPackage> inventoryPackages;

    /**
     * Gets the {@link KuraInventoryPackage}s {@link List}.
     *
     * @return The {@link KuraInventoryPackage}s {@link List}.
     * @since 1.5.0
     */
    public List<KuraInventoryPackage> getInventoryPackages() {
        if (inventoryPackages == null) {
            inventoryPackages = new ArrayList<>();
        }

        return inventoryPackages;
    }

    /**
     * Adds a {@link KuraInventoryPackage} to the {@link List}
     *
     * @param inventoryPackage The {@link KuraInventoryPackage} to add.
     * @since 1.5.0
     */
    public void addInventoryPackage(@NotNull KuraInventoryPackage inventoryPackage) {
        getInventoryPackages().add(inventoryPackage);
    }

    /**
     * Sets the {@link KuraInventoryPackage}s {@link List}.
     *
     * @param inventoryPackages The {@link KuraInventoryPackage}s {@link List}.
     * @since 1.5.0
     */
    public void setInventoryPackages(@Nullable List<KuraInventoryPackage> inventoryPackages) {
        this.inventoryPackages = inventoryPackages;
    }

}
