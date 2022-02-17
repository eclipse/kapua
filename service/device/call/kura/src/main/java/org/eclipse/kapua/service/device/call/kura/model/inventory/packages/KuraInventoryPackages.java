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
package org.eclipse.kapua.service.device.call.kura.model.inventory.packages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import org.checkerframework.checker.nullness.qual.Nullable;
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

    @JsonProperty("deploymentPackages")
    public List<KuraInventoryPackage> packages;

    /**
     * Gets the {@link KuraInventoryPackage}s {@link List}.
     *
     * @return The {@link KuraInventoryPackage}s {@link List}.
     * @since 1.5.0
     */
    public List<KuraInventoryPackage> getPackages() {
        if (packages == null) {
            packages = new ArrayList<>();
        }

        return packages;
    }

    /**
     * Adds a {@link KuraInventoryPackage} to the {@link List}
     *
     * @param aPackage The {@link KuraInventoryPackage} to add.
     * @since 1.5.0
     */
    public void addPackage(@NotNull KuraInventoryPackage aPackage) {
        getPackages().add(aPackage);
    }

    /**
     * Sets the {@link KuraInventoryPackage}s {@link List}.
     *
     * @param packages The {@link KuraInventoryPackage}s {@link List}.
     * @since 1.5.0
     */
    public void setPackages(@Nullable List<KuraInventoryPackage> packages) {
        this.packages = packages;
    }

}
