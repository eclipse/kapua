/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOperation;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOptions;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;

/**
 * {@link DevicePackageManagementService} definition.
 * <p>
 * The {@link DevicePackageManagementService} is used to interact with the DEPLOY-V2 device application.
 *
 * @since 1.0.0
 */
public interface DevicePackageManagementService extends KapuaService {

    /**
     * Gets the installed {@link DevicePackages}s
     *
     * @param scopeId  The scope {@link KapuaId} of the target device
     * @param deviceId The device {@link KapuaId} of the target device
     * @param timeout  The timeout in milliseconds for the request to complete
     * @return The {@link DevicePackages}
     * @throws KapuaException if error occurs during processing
     * @since 1.0.0
     */
    DevicePackages getInstalled(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException;

    //
    // Download
    //

    /**
     * Downloads a {@link org.eclipse.kapua.service.device.management.packages.model.DevicePackage}
     *
     * @param scopeId                The scope {@link KapuaId} of the target device
     * @param deviceId               The device {@link KapuaId} of the target device
     * @param packageDownloadRequest The {@link DevicePackageDownloadRequest} for this request
     * @param timeout                The timeout in milliseconds for the request to complete
     * @throws KapuaException if error occurs during processing
     * @since 1.0.0
     * @deprecated since 1.1.0. Please make use of {@link #downloadExec(KapuaId, KapuaId, DevicePackageDownloadRequest, DevicePackageDownloadOptions)}.
     */
    @Deprecated
    void downloadExec(KapuaId scopeId, KapuaId deviceId, DevicePackageDownloadRequest packageDownloadRequest, Long timeout) throws KapuaException;

    /**
     * Downloads a {@link org.eclipse.kapua.service.device.management.packages.model.DevicePackage}
     *
     * @param scopeId                The scope {@link KapuaId} of the target device
     * @param deviceId               The device {@link KapuaId} of the target device
     * @param packageDownloadRequest The {@link DevicePackageDownloadRequest} for this request
     * @param packageDownloadOptions The {@link DevicePackageDownloadOptions} for this request
     * @throws KapuaException if error occurs during processing
     * @since 1.1.0
     */
    void downloadExec(KapuaId scopeId, KapuaId deviceId, DevicePackageDownloadRequest packageDownloadRequest, DevicePackageDownloadOptions packageDownloadOptions) throws KapuaException;

    /**
     * Interrupts a {@link org.eclipse.kapua.service.device.management.packages.model.DevicePackage} download operation
     *
     * @param scopeId  The scope {@link KapuaId} of the target device
     * @param deviceId The device {@link KapuaId} of the target device
     * @param timeout  The timeout in milliseconds for the request to complete
     * @throws KapuaException
     * @since 1.0.0
     */
    void downloadStop(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException;

    /**
     * Gets the {@link DevicePackageDownloadOperation} status.
     *
     * @param scopeId  The scope {@link KapuaId} of the target device
     * @param deviceId The device {@link KapuaId} of the target device
     * @param timeout  The timeout in milliseconds for the request to complete
     * @return
     * @throws KapuaException if error occurs during processing
     * @since 1.0.0
     */
    DevicePackageDownloadOperation downloadStatus(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException;

    //
    // Install
    //

    /**
     * Installs a {@link org.eclipse.kapua.service.device.management.packages.model.DevicePackage}
     *
     * @param scopeId               The scope {@link KapuaId} of the target device
     * @param deviceId              The device {@link KapuaId} of the target device
     * @param packageInstallRequest The {@link DevicePackageInstallRequest} for this request
     * @param timeout               The timeout in milliseconds for the request to complete
     * @throws KapuaException if error occurs during processing
     * @since 1.0.0
     * @deprecated since 1.1.0. Please make use of {@link #installExec(KapuaId, KapuaId, DevicePackageInstallRequest, DevicePackageInstallOptions)}.
     */
    @Deprecated
    void installExec(KapuaId scopeId, KapuaId deviceId, DevicePackageInstallRequest packageInstallRequest, Long timeout) throws KapuaException;

    /**
     * Installs a {@link org.eclipse.kapua.service.device.management.packages.model.DevicePackage}
     *
     * @param scopeId               The scope {@link KapuaId} of the target device
     * @param deviceId              The device {@link KapuaId} of the target device
     * @param packageInstallRequest The {@link DevicePackageInstallRequest} for this request
     * @param packageInstallOptions The {@link DevicePackageInstallOptions} for this request
     * @throws KapuaException if error occurs during processing
     * @since 1.1.0
     */
    void installExec(KapuaId scopeId, KapuaId deviceId, DevicePackageInstallRequest packageInstallRequest, DevicePackageInstallOptions packageInstallOptions) throws KapuaException;

    /**
     * Gets the {@link DevicePackageInstallOperation} status.
     *
     * @param scopeId  The scope {@link KapuaId} of the target device
     * @param deviceId The device {@link KapuaId} of the target device
     * @param timeout  The timeout in milliseconds for the request to complete
     * @return
     * @throws KapuaException if error occurs during processing
     * @since 1.0.0
     */
    DevicePackageInstallOperation installStatus(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException;

    //
    // Uninstall
    //

    /**
     * Uninstalls a {@link org.eclipse.kapua.service.device.management.packages.model.DevicePackage}
     *
     * @param scopeId                 The scope {@link KapuaId} of the target device
     * @param deviceId                The device {@link KapuaId} of the target device
     * @param packageUninstallRequest The {@link DevicePackageUninstallRequest} for this request
     * @param timeout                 The timeout in milliseconds for the request to complete
     * @throws KapuaException if error occurs during processing
     * @since 1.0.0
     * @deprecated since 1.1.0. Please make use of {@link #uninstallExec(KapuaId, KapuaId, DevicePackageUninstallRequest, DevicePackageUninstallOptions)}.
     */
    @Deprecated
    void uninstallExec(KapuaId scopeId, KapuaId deviceId, DevicePackageUninstallRequest packageUninstallRequest, Long timeout) throws KapuaException;

    /**
     * Uninstalls a {@link org.eclipse.kapua.service.device.management.packages.model.DevicePackage}
     *
     * @param scopeId                 The scope {@link KapuaId} of the target device
     * @param deviceId                The device {@link KapuaId} of the target device
     * @param packageUninstallRequest The {@link DevicePackageUninstallRequest} for this request
     * @param packageUninstallOptions The The {@link DevicePackageUninstallOptions} for this request
     * @throws KapuaException if error occurs during processing
     * @since 1.1.0
     */
    void uninstallExec(KapuaId scopeId, KapuaId deviceId, DevicePackageUninstallRequest packageUninstallRequest, DevicePackageUninstallOptions packageUninstallOptions) throws KapuaException;

    /**
     * Gets the {@link DevicePackageUninstallOperation}.
     *
     * @param scopeId  The scope {@link KapuaId} of the target device
     * @param deviceId The device {@link KapuaId} of the target device
     * @param timeout  The timeout in milliseconds for the request to complete
     * @return
     * @throws KapuaException if error occurs during processing
     * @since 1.0.0
     */
    DevicePackageUninstallOperation uninstallStatus(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException;
}
