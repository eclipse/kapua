/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.packages.model.uninstall.internal;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallStatus;

/**
 * {@link DevicePackageUninstallOperation} implementation.
 *
 * @since 1.0.0
 */
public class DevicePackageUninstallOperationImpl implements DevicePackageUninstallOperation {

    private KapuaId id;
    private String name;
    private String version;
    private DevicePackageUninstallStatus status;

    @Override
    public KapuaId getId() {
        return id;
    }

    @Override
    public void setId(KapuaId id) {
        this.id = id;
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
    public DevicePackageUninstallStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(DevicePackageUninstallStatus status) {
        this.status = status;
    }
}
