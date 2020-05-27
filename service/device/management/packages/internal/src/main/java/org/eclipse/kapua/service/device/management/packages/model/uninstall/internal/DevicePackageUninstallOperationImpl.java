/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model.uninstall.internal;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallStatus;

/**
 * Device package uninstall operation entity.
 *
 * @since 1.0
 *
 */
public class DevicePackageUninstallOperationImpl implements DevicePackageUninstallOperation {

    @XmlElement(name = "id")
    private KapuaEid id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "version")
    private String version;

    @XmlElement(name = "status")
    private DevicePackageUninstallStatus status;

    @Override
    public KapuaEid getId() {
        return id;
    }

    @Override
    public void setId(KapuaId id) {
        if (id != null) {
            this.id = new KapuaEid(id.getId());
        }
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
