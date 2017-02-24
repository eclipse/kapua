/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;

/**
 * Device packages list container.
 * 
 * @since 1.0
 *
 */
public class DevicePackagesImpl implements DevicePackages
{
    public List<DevicePackage> deploymentPackages;

    @Override
    public List<DevicePackage> getPackages()
    {
        if (deploymentPackages == null) {
            deploymentPackages = new ArrayList<>();
        }

        return deploymentPackages;
    }

}
