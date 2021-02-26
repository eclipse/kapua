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
package org.eclipse.kapua.service.device.management.bundle.model.internal;

import org.eclipse.kapua.service.device.management.inventory.model.DeviceInventoryPackage;

/**
 * {@link DeviceInventoryPackage} implementation.
 *
 * @since 1.5.0
 */
public class DeviceInventoryPackageImpl implements DeviceInventoryPackage {

    public String name;
    public String version;
    public String packageType;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceInventoryPackageImpl() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getPackageType() {
        return packageType;
    }

    @Override
    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }
}
