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

import org.checkerframework.checker.nullness.qual.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link KuraInventoryBundles} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("inventoryBundles")
public class KuraInventoryBundles {

    @JsonProperty("bundles")
    public List<KuraInventoryBundle> inventoryBundles;

    /**
     * Gets the {@link KuraInventoryBundle}s {@link List}.
     *
     * @return The {@link KuraInventoryBundle}s {@link List}.
     * @since 1.5.0
     */
    public List<KuraInventoryBundle> getInventoryBundles() {
        if (inventoryBundles == null) {
            inventoryBundles = new ArrayList<>();
        }

        return inventoryBundles;
    }

    /**
     * Adds a {@link KuraInventoryBundle} to the {@link List}
     *
     * @param inventoryBundle The {@link KuraInventoryBundle} to add.
     * @since 1.5.0
     */
    public void addInventoryBundle(@NotNull KuraInventoryBundle inventoryBundle) {
        getInventoryBundles().add(inventoryBundle);
    }

    /**
     * Sets the {@link KuraInventoryBundle}s {@link List}.
     *
     * @param inventoryBundles The {@link KuraInventoryBundle}s {@link List}.
     * @since 1.5.0
     */
    public void setInventoryBundles(@Nullable List<KuraInventoryBundle> inventoryBundles) {
        this.inventoryBundles = inventoryBundles;
    }

}
