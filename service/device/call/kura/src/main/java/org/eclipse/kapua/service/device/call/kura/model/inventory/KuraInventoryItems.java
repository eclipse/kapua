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
package org.eclipse.kapua.service.device.call.kura.model.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import org.checkerframework.checker.nullness.qual.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link KuraInventoryItems} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("inventoryItems")
public class KuraInventoryItems {

    @JsonProperty("inventory")
    public List<KuraInventoryItem> inventoryItems;

    /**
     * Gets the {@link KuraInventoryItem}s {@link List}.
     *
     * @return The {@link KuraInventoryItem}s {@link List}.
     * @since 1.5.0
     */
    public List<KuraInventoryItem> getInventoryItems() {
        if (inventoryItems == null) {
            inventoryItems = new ArrayList<>();
        }

        return inventoryItems;
    }

    /**
     * Adds a {@link KuraInventoryItem} to the {@link List}
     *
     * @param inventoryItem The {@link KuraInventoryItem} to add.
     * @since 1.5.0
     */
    public void addInventoryItem(@NotNull KuraInventoryItem inventoryItem) {
        getInventoryItems().add(inventoryItem);
    }

    /**
     * Sets the {@link KuraInventoryItem}s {@link List}.
     *
     * @param inventoryItems The {@link KuraInventoryItem}s {@link List}.
     * @since 1.5.0
     */
    public void setInventoryItems(@Nullable List<KuraInventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

}
