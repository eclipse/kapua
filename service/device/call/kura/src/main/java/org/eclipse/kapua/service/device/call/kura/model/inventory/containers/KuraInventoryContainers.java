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
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link KuraInventoryContainers} definition.
 *
 * @since 2.0.0
 */
@JsonRootName("inventoryContainer")
public class KuraInventoryContainers {

    @JsonProperty("containers")
    public List<KuraInventoryContainer> inventoryContainers;

    /**
     * Gets the {@link KuraInventoryContainer}s {@link List}.
     *
     * @return The {@link KuraInventoryContainer}s {@link List}.
     * @since 2.0.0
     */
    public List<KuraInventoryContainer> getInventoryContainers() {
        if (inventoryContainers == null) {
            inventoryContainers = new ArrayList<>();
        }

        return inventoryContainers;
    }

    /**
     * Adds a {@link KuraInventoryContainer} to the {@link List}
     *
     * @param inventoryContainer The {@link KuraInventoryContainer} to add.
     * @since 2.0.0
     */
    public void addInventoryContainer(@NotNull KuraInventoryContainer inventoryContainer) {
        getInventoryContainers().add(inventoryContainer);
    }

    /**
     * Sets the {@link KuraInventoryContainer}s {@link List}.
     *
     * @param inventoryContainers The {@link KuraInventoryContainer}s {@link List}.
     * @since 2.0.0
     */
    public void setInventoryContainers(@Nullable List<KuraInventoryContainer> inventoryContainers) {
        this.inventoryContainers = inventoryContainers;
    }

}
