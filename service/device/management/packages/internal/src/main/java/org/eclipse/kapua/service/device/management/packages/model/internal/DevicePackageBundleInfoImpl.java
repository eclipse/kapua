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

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfo;

/**
 * {@link DevicePackageBundleInfo} implementation.
 *
 * @since 1.0.0
 */
public class DevicePackageBundleInfoImpl implements DevicePackageBundleInfo {

    public String name;
    public String version;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public DevicePackageBundleInfoImpl() {
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
}
