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
package org.eclipse.kapua.service.device.call.kura.model.inventory.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import org.checkerframework.checker.nullness.qual.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link KuraInventorySystemPackages} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("inventorySystemPackages")
public class KuraInventorySystemPackages {

    @JsonProperty("systemPackages")
    public List<KuraInventorySystemPackage> systemPackages;

    /**
     * Gets the {@link KuraInventorySystemPackage}s {@link List}.
     *
     * @return The {@link KuraInventorySystemPackage}s {@link List}.
     * @since 1.5.0
     */
    public List<KuraInventorySystemPackage> getSystemPackages() {
        if (systemPackages == null) {
            systemPackages = new ArrayList<>();
        }

        return systemPackages;
    }

    /**
     * Adds a {@link KuraInventorySystemPackage} to the {@link List}
     *
     * @param systemPackage The {@link KuraInventorySystemPackage} to add.
     * @since 1.5.0
     */
    public void addSystemPackage(@NotNull KuraInventorySystemPackage systemPackage) {
        getSystemPackages().add(systemPackage);
    }

    /**
     * Sets the {@link KuraInventorySystemPackage}s {@link List}.
     *
     * @param systemPackages The {@link KuraInventorySystemPackage}s {@link List}.
     * @since 1.5.0
     */
    public void setSystemPackages(@Nullable List<KuraInventorySystemPackage> systemPackages) {
        this.systemPackages = systemPackages;
    }

}
