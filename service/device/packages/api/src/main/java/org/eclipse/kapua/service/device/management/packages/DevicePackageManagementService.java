/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOperation;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOperation;

public interface DevicePackageManagementService extends KapuaService
{
    public DevicePackages getInstalled(KapuaId scopeId, KapuaId deviceId, Long timeout)
        throws KapuaException;

    public void downloadExec(KapuaId scopeId, KapuaId deviceId, DevicePackageDownloadRequest packageDownloadRequest, Long timeout)
        throws KapuaException;

    public void downloadStop(KapuaId scopeId, KapuaId deviceId, Long timeout)
        throws KapuaException;

    public DevicePackageDownloadOperation downloadStatus(KapuaId scopeId, KapuaId deviceId, Long timeout)
        throws KapuaException;

    public void installExec(KapuaId scopeId, KapuaId deviceId, DevicePackageInstallRequest packageInstallRequest, Long timeout)
        throws KapuaException;

    public DevicePackageInstallOperation installStatus(KapuaId scopeId, KapuaId deviceId, Long timeout)
        throws KapuaException;

    public void uninstallExec(KapuaId scopeId, KapuaId deviceId, DevicePackageUninstallRequest packageUninstallRequest, Long timeout)
        throws KapuaException;

    public DevicePackageUninstallOperation uninstallStatus(KapuaId scopeId, KapuaId deviceId, Long timeout)
        throws KapuaException;
}
