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
package org.eclipse.kapua.service.device.management.packages.model.internal;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfos;

import java.util.Date;

/**
 * {@link DevicePackage} implementation.
 *
 * @since 1.0.0
 */
public class DevicePackageImpl implements DevicePackage {

    private String name;
    private String version;
    private DevicePackageBundleInfos bundleInfos;
    private Date installDate;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public DevicePackageImpl() {
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
    public DevicePackageBundleInfos getBundleInfos() {
        if (bundleInfos == null) {
            bundleInfos = new DevicePackageBundleInfosImpl();
        }

        return bundleInfos;
    }

    @Override
    public void setBundleInfos(DevicePackageBundleInfos bundleInfos) {
        this.bundleInfos = bundleInfos;
    }

    @Override
    public Date getInstallDate() {
        return installDate;
    }

    @Override
    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }
}
