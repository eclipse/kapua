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
package org.eclipse.kapua.service.device.management.packages.model;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.packages.DevicePackageFactory;
import org.eclipse.kapua.service.device.management.packages.model.download.AdvancedPackageDownloadOptions;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DevicePackageXmlRegistry} class.
 *
 * @since 1.0.0
 */
@XmlRegistry
public class DevicePackageXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DevicePackageFactory DEVICE_PACKAGE_FACTORY = LOCATOR.getFactory(DevicePackageFactory.class);

    /**
     * Creates a new device package instance
     *
     * @return
     * @since 1.0.0
     */
    public DevicePackage newDevicePackage() {
        return DEVICE_PACKAGE_FACTORY.newDeviceDeploymentPackage();
    }

    /**
     * Creates a new device packages instance
     *
     * @return
     * @since 1.0.0
     */
    public DevicePackages newDevicePackages() {
        return DEVICE_PACKAGE_FACTORY.newDeviceDeploymentPackages();
    }

    /**
     * Creates a new device package bundle information instance
     *
     * @return
     * @since 1.0.0
     */
    public DevicePackageBundleInfo newDevicePackageBundleInfo() {
        return DEVICE_PACKAGE_FACTORY.newDevicePackageBundleInfo();
    }

    /**
     * Creates a new device package bundle informations instance
     *
     * @return
     * @since 1.0.0
     */
    public DevicePackageBundleInfos newDevicePackageBundleInfos() {
        return DEVICE_PACKAGE_FACTORY.newDevicePackageBundleInfos();
    }

    /**
     * Creates a new device package download request instance
     *
     * @return
     */
    public DevicePackageDownloadRequest newDevicePackageDownloadRequest() {
        return DEVICE_PACKAGE_FACTORY.newPackageDownloadRequest();
    }


    /**
     * Creates a new device package download request instance
     *
     * @return
     * @since 1.1.0
     */
    public AdvancedPackageDownloadOptions newAdvancedPackageDownloadOptions() {
        return DEVICE_PACKAGE_FACTORY.newAdvancedPackageDownloadOptions();
    }


    /**
     * Creates a new device package uninstall request instance
     *
     * @return
     * @since 1.0.0
     */
    public DevicePackageUninstallRequest newDevicePackageUninstallRequest() {
        return DEVICE_PACKAGE_FACTORY.newPackageUninstallRequest();
    }
}
