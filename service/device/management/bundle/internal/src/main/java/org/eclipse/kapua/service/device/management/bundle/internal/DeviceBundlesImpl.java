/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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

    private static final long serialVersionUID = 734716753080998855L;

    private List<DeviceBundle> bundles;

    @Override
    public List<DeviceBundle> getBundles() {
        if (bundles == null) {
            bundles = new ArrayList<>();
        }

        return bundles;
    }
}
