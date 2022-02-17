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
package org.eclipse.kapua.service.device.management.inventory.model.system.internal;

import org.eclipse.kapua.service.device.management.inventory.model.system.DeviceInventorySystemPackage;
import org.eclipse.kapua.service.device.management.inventory.model.system.DeviceInventorySystemPackages;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceInventorySystemPackages} implementation.
 *
 * @since 1.5.0
 */
public class DeviceInventorySystemPackagesImpl implements DeviceInventorySystemPackages {

    private static final long serialVersionUID = -3824464082139248997L;

    private List<DeviceInventorySystemPackage> systemPackages;

    @Override
    public List<DeviceInventorySystemPackage> getSystemPackages() {
        if (systemPackages == null) {
            systemPackages = new ArrayList<>();
        }

        return systemPackages;
    }

    @Override
    public void addSystemPackage(DeviceInventorySystemPackage systemPackage) {
        getSystemPackages().add(systemPackage);
    }

    @Override
    public void setSystemPackages(List<DeviceInventorySystemPackage> systemPackages) {
        this.systemPackages = systemPackages;
    }
}
