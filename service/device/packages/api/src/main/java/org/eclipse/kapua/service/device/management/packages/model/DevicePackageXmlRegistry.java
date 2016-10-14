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

package org.eclipse.kapua.service.device.management.packages.model;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.packages.DevicePackageFactory;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;

@XmlRegistry
public class DevicePackageXmlRegistry {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DevicePackageFactory factory = locator.getFactory(DevicePackageFactory.class);
    
    public DevicePackage newDevicePackage() {
        return factory.newDeviceDeploymentPackage();
    }
    
    public DevicePackages newDevicePackages() {
        return factory.newDeviceDeploymentPackages();
    }
    
    public DevicePackageBundleInfo newDevicePackageBundleInfo() {
        return factory.newDevicePackageBundleInfo();
    }
    
    public DevicePackageBundleInfos newDevicePackageBundleInfos() {
        return factory.newDevicePackageBundleInfos();
    }

    public DevicePackageDownloadRequest newDevicePackageDownloadRequest() { return factory.newPackageDownloadRequest(); }

    public DevicePackageUninstallRequest newDevicePackageUninstallRequest() { return factory.newPackageUninstallRequest(); }
}
