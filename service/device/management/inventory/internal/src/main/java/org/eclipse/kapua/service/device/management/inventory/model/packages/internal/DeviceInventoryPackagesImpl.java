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
package org.eclipse.kapua.service.device.management.inventory.model.packages.internal;

import org.eclipse.kapua.service.device.management.inventory.model.packages.DeviceInventoryPackage;
import org.eclipse.kapua.service.device.management.inventory.model.packages.DeviceInventoryPackages;
import org.eclipse.kapua.service.device.management.inventory.model.system.DeviceInventorySystemPackages;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceInventorySystemPackages} implementation.
 *
 * @since 1.5.0
 */
public class DeviceInventoryPackagesImpl implements DeviceInventoryPackages {

    private static final long serialVersionUID = -8272540683907649241L;

    private List<DeviceInventoryPackage> packages;

    @Override
    public List<DeviceInventoryPackage> getPackages() {
        if (packages == null) {
            packages = new ArrayList<>();
        }

        return packages;
    }

    @Override
    public void addPackage(DeviceInventoryPackage aPackage) {
        getPackages().add(aPackage);
    }

    @Override
    public void setPackages(List<DeviceInventoryPackage> packages) {
        this.packages = packages;
    }
}
