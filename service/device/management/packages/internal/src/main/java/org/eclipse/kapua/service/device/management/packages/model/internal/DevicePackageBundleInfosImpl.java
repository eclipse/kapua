/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfo;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfos;

/**
 * Package bundle informations list container.
 *
 * @since 1.0
 *
 */
public class DevicePackageBundleInfosImpl implements DevicePackageBundleInfos {

    List<DevicePackageBundleInfo> bundleInfos;

    @Override
    public List<DevicePackageBundleInfo> getBundlesInfos() {
        if (bundleInfos == null) {
            bundleInfos = new ArrayList<>();
        }
        return bundleInfos;
    }

}
