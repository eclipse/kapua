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
import org.eclipse.kapua.service.device.call.kura.model.inventory.bundles.KuraInventoryBundle;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link KuraInventoryPackage} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("inventoryPackage")
public class KuraInventoryPackage {

    @JsonProperty("name")
    public String name;

    @JsonProperty("version")
    public String version;

    @JsonProperty("bundles")
    List<KuraInventoryBundle> packageBundles;

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
     * Gets the {@link List} of {@link KuraInventoryBundle}s
     *
     * @return The {@link List} of {@link KuraInventoryBundle}s
     * @since 1.5.0
     */
    public List<KuraInventoryBundle> getPackageBundles() {
        if (packageBundles == null) {
            packageBundles = new ArrayList<>();
        }

        return packageBundles;
    }

    /**
     * Adds a {@link KuraInventoryBundle} to the {@link List}.
     *
     * @param packageBundle The {@link KuraInventoryBundle} to add.
     * @since 1.5.0
     */
    public void addPackageBundle(KuraInventoryBundle packageBundle) {
        getPackageBundles().add(packageBundle);
    }

    /**
     * Sets the {@link List} of {@link KuraInventoryBundle}s
     *
     * @param packageBundles The {@link List} of {@link KuraInventoryBundle}s
     * @since 1.5.0
     */
    public void setPackageBundles(List<KuraInventoryBundle> packageBundles) {
        this.packageBundles = packageBundles;
    }
}
