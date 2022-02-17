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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model.internal;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DevicePackages} implementation.
 *
 * @since 1.0.0
 */
public class DevicePackagesImpl implements DevicePackages {

    private static final long serialVersionUID = 2450088980495469562L;

    public List<DevicePackage> deploymentPackages;

    @Override
    public List<DevicePackage> getPackages() {
        if (deploymentPackages == null) {
            deploymentPackages = new ArrayList<>();
        }

        return deploymentPackages;
    }

}
