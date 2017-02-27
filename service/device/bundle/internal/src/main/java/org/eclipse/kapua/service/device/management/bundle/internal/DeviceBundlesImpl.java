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
package org.eclipse.kapua.service.device.management.bundle.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;

/**
 * Device bundles list entity implementation.
 *
 * @since 1.0
 *
 */
public class DeviceBundlesImpl implements DeviceBundles {

    private List<DeviceBundle> bundles;

    @Override
    public List<DeviceBundle> getBundles() {
        if (bundles == null) {
            bundles = new ArrayList<>();
        }

        return bundles;
    }
}
