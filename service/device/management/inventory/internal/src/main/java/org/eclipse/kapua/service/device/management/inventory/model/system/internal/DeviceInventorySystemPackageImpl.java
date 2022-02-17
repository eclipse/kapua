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

/**
 * {@link DeviceInventorySystemPackage} implementation.
 *
 * @since 1.5.0
 */
public class DeviceInventorySystemPackageImpl implements DeviceInventorySystemPackage {

    private String name;
    private String version;
    private String packageType;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceInventorySystemPackageImpl() {
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
